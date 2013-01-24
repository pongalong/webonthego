package com.trc.web.controller;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.DeviceManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.DeviceManager;
import com.trc.manager.UserManager;
import com.trc.security.encryption.SessionEncrypter;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.util.logger.DevLogger;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.trc.web.validation.DeviceInfoValidator;
import com.tscp.mvne.Device;
import com.tscp.mvne.NetworkInfo;

@Controller
@RequestMapping("/devices")
@SessionAttributes({ "user", "label", "accessFeeDate", "accountDetail", "accountDetails" })
public class DeviceController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private DeviceInfoValidator deviceInfoValidator;

	@ModelAttribute
	private void paymentReferenceData(
			ModelMap modelMap) {
		try {
			User user = userManager.getCurrentUser();
			List<AccountDetail> accountDetails = accountManager.getAccountDetailList(user);
			encodeDeviceIds(accountDetails);
			modelMap.addAttribute("user", user);
			modelMap.addAttribute("accountDetails", accountDetails);
		} catch (AccountManagementException e) {
			DevLogger.error(e.getMessage(), e);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showDevices(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetails") List<AccountDetail> accountDetails) {
		ResultModel model = new ResultModel("devices/devices");
		model.addObject("accountDetails", accountDetails);
		return model.getSuccess();
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView renameDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetails") List<AccountDetail> accountDetails,
			@PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("devices/rename");
		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			model.addObject("label", accountDetail.getDeviceInfo().getLabel());
			model.addObject("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (SessionAttributeNotFoundException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postRenameDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetail") AccountDetail accountDetail,
			BindingResult result,
			@ModelAttribute("label") String oldLabel,
			Errors errors) {
		ResultModel model = new ResultModel("devices/renameSuccess", "devices/rename");
		try {
			result.pushNestedPath("deviceInfo");
			deviceInfoValidator.checkDeviceLabel(accountDetail.getDeviceInfo().getLabel(), result);
			result.popNestedPath();
			if (result.hasErrors()) {
				accountDetail.getDeviceInfo().setLabel(oldLabel);
				return model.getError();
			} else {
				deviceManager.updateDeviceInfo(user, accountDetail.getDeviceInfo());
				model.addObject("oldLabel", oldLabel);
				model.addObject("newLabel", accountDetail.getDeviceInfo().getLabel());
				return model.getSuccess();
			}
		} catch (DeviceManagementException e) {
			errors.reject("device.update.label.error", null, "There was an error renaming your device");
			return model.getError();
		}
	}

	@Deprecated
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showSwapDevice(
			@PathVariable String encodedDeviceId) {
		ResultModel model = new ResultModel("devices/swapEsn");
		User user = userManager.getCurrentUser();
		try {
			Device deviceToSwap = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
			SessionManager.set(SessionKey.DEVICE_SWAP, deviceToSwap);
			model.addObject("deviceInfo", deviceToSwap);
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			return model.getAccessException();
		}
	}

	@Deprecated
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postSwapDevice(
			@PathVariable String encodedDeviceId,
			@ModelAttribute Device deviceInfo,
			Errors errors) {
		ResultModel model = new ResultModel("devices/swapEsnSuccess", "devices/swapEsn");
		User user = userManager.getCurrentUser();
		int deviceId = SessionEncrypter.decryptId(encodedDeviceId);
		String newEsn = deviceInfo.getValue();
		String newDeviceLabel = deviceInfo.getLabel();
		try {
			Device oldDeviceInfo = (Device) SessionManager.get(SessionKey.DEVICE_SWAP);
			Device newDeviceInfo = WebserviceAdapter.clone(oldDeviceInfo);
			if (oldDeviceInfo == null) {
				oldDeviceInfo = deviceManager.getDeviceInfo(user, deviceId);
			}
			if (newDeviceInfo == null) {
				newDeviceInfo = deviceManager.getDeviceInfo(user, deviceId);
			}
			newDeviceInfo.setValue(newEsn);
			newDeviceInfo.setLabel(newDeviceLabel);
			deviceManager.swapDevice(user, oldDeviceInfo, newDeviceInfo);
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.swap.error");
			model.addObject("deviceInfo", deviceInfo);
			return model.getError();
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showDeactivateDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetails") List<AccountDetail> accountDetails,
			@PathVariable String encodedDeviceId) {
		ResultModel model = new ResultModel("devices/disconnect/prompt");
		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			XMLGregorianCalendar accessFeeDate = accountManager.getLastAccessFeeDate(user, accountDetail.getAccount());
			model.addObject("accountDetail", accountDetail);
			model.addObject("accessFeeDate", accessFeeDate);
			return model.getSuccess();
		} catch (SessionAttributeNotFoundException e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postDeactivateDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetail") AccountDetail accountDetail,
			@ModelAttribute("accessFeeDate") XMLGregorianCalendar accessFeeDate,
			Errors errors) {
		ResultModel model = new ResultModel("devices/disconnect/success", "devices/disconnect/prompt");
		model.addObject("accountDetail", accountDetail);
		model.addObject("accessFeeDate", accessFeeDate);
		try {
			NetworkInfo networkInfo = deviceManager.getNetworkInfo(accountDetail.getDeviceInfo().getValue(), null);
			if (!deviceManager.compareEsn(accountDetail.getDeviceInfo(), networkInfo)) {
				errors.rejectValue("value", "device.deactivate.error");
				return model.getError();
			} else {
				deviceManager.disconnectService(WebserviceAdapter.toServiceInstance(networkInfo));
				return model.getSuccess();
			}
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.deactivate.error");
			return model.getError();
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showReinstallDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetails") List<AccountDetail> accountDetails,
			@PathVariable String encodedDeviceId) {
		ResultModel model = new ResultModel("devices/reconnect/prompt");
		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			model.addObject("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (SessionAttributeNotFoundException e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postReinstallDevice(
			@ModelAttribute("user") User user,
			@ModelAttribute("accountDetail") AccountDetail accountDetail,
			Errors errors) {
		ResultModel model = new ResultModel("redirect:/devices", "devices/reconnect/prompt");
		try {
			deviceManager.reinstallCustomerDevice(user, accountDetail.getDeviceInfo());
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.reinstall.error");
			return model.getError();
		}
	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showChangeTopUp(
			@ModelAttribute("user") User user,
			@PathVariable String encodedDeviceId) {
		ResultModel model = new ResultModel("devices/changeTopUp");
		try {
			int deviceId = SessionEncrypter.decryptId(encodedDeviceId);
			AccountDetail accountDetail = accountManager.getAccountDetail(user, deviceId);
			model.addObject("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (AccountManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postChangeTopUp(
			@ModelAttribute("user") User user,
			@PathVariable String encodedDeviceId,
			@ModelAttribute("accountDetail") AccountDetail accountDetail,
			Errors errors) {
		ResultModel model = new ResultModel("devices/changeTopUpSuccess", "devices/changeTopUp");
		try {
			accountDetail.setTopUp(accountDetail.getTopUp());
			accountManager.setTopUp(user, new Double(accountDetail.getTopUp()), accountDetail.getAccount());
			model.addObject("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (AccountManagementException e) {
			model.addObject("accountDetail", accountDetail);
			errors.rejectValue("topUp", "account.topUp.change.error");
			return model.getError();
		}
	}

	/* ****************************************************************************************************************
	 * Helper Methods
	 * ****************************************************************************************************************
	 */

	private void encodeDeviceIds(
			List<AccountDetail> accountDetailList) {
		for (AccountDetail accountDetail : accountDetailList) {
			accountDetail.setEncodedDeviceId(SessionEncrypter.encryptId(accountDetail.getDeviceInfo().getId()));
		}
	}

	private AccountDetail getAccountDetailFromSession(
			List<AccountDetail> accountDetails,
			String encodedId) throws SessionAttributeNotFoundException {
		int deviceId = SessionEncrypter.decryptId(encodedId);
		for (AccountDetail ad : accountDetails)
			if (ad.getDeviceInfo().getId() == deviceId)
				return ad;
		throw new SessionAttributeNotFoundException("Could not find AccountDetail for Device " + deviceId);
	}

}