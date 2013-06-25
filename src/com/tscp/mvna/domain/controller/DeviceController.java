package com.tscp.mvna.domain.controller;

import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.web.validation.DeviceValidator;
import com.tscp.mvna.service.gateway.WebserviceAdapter;
import com.tscp.mvna.web.controller.model.ResultModel;
import com.tscp.mvna.web.session.cache.CachedAttributeNotFound;
import com.tscp.mvne.Device;
import com.tscp.mvne.NetworkInfo;

@Controller
@RequestMapping("/devices")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"ACCOUNT_DETAILS",
		"label",
		"accountDetail",
		"newDevice" })
public class DeviceController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private DeviceValidator deviceInfoValidator;

	@RequestMapping(method = RequestMethod.GET)
	public String showDevices() {
		return "account/device/devices";
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView renameDevice(
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/rename/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			model.addAttribute("label", accountDetail.getDeviceInfo().getLabel());
			model.addAttribute("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {

			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postRenameDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("label") String oldLabel, @ModelAttribute("accountDetail") AccountDetail accountDetail, BindingResult result) {

		ResultModel model = new ResultModel("account/device/rename/success", "account/device/rename/prompt");

		result.pushNestedPath("deviceInfo");
		deviceInfoValidator.checkDeviceLabel(accountDetail.getDeviceInfo().getLabel(), result);
		result.popNestedPath();
		if (result.hasErrors()) {
			accountDetail.getDeviceInfo().setLabel(oldLabel);
			return model.getError();
		}

		try {
			deviceManager.updateDeviceInfo(user, accountDetail.getDeviceInfo());
			model.addAttribute("oldLabel", oldLabel);
			model.addAttribute("newLabel", accountDetail.getDeviceInfo().getLabel());
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			result.reject("device.update.label.error", null, "There was an error renaming your device");
			return model.getError();
		}
	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showChangeTopUp(
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/topup/change/prompt");

		model.addAttribute("accountDetail", getAccountDetailFromSession(accountDetails, encodedDeviceId));
		return model.getSuccess();

	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postChangeTopUp(
			@PathVariable String encodedDeviceId, @ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ResultModel model = new ResultModel("account/device/topup/change/success", "account/device/topup/change/prompt");

		try {
			accountDetail.setTopUp(accountDetail.getTopUp());
			accountManager.setTopup(user, new Double(accountDetail.getTopUp()), accountDetail.getAccount());
			return model.getSuccess();
		} catch (AccountManagementException e) {
			model.addAttribute("accountDetail", accountDetail);
			errors.rejectValue("topUp", "account.topUp.change.error");
			return model.getError();
		}
	}

	@Deprecated
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','isAtleast')")
	@RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showSwapDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/swap/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);

			Device newDevice = new Device();

			model.addAttribute("accountDetail", accountDetail);
			model.addAttribute("newDevice", newDevice);
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {
			return model.getAccessException();
		}
	}

	@Deprecated
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','isAtleast')")
	@RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postSwapDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @ModelAttribute("newDevice") Device newDevice, Errors errors) {

		ResultModel model = new ResultModel("account/device/swap/success", "account/device/swap/prompt");

		try {
			// the existing device is in the accountDetail object
			Device oldDevice = accountDetail.getDeviceInfo();

			// fetch the old device and give it the new values so all other properties will be filled
			// TODO make a device wrapper class that implements cloneable
			Device newDeviceCopy = deviceManager.getDeviceInfo(user, accountDetail.getDeviceInfo().getId());
			newDeviceCopy.setLabel(newDevice.getLabel());
			newDeviceCopy.setValue(newDevice.getValue());

			deviceManager.swapDevice(user, oldDevice, newDeviceCopy);
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.swap.error");
			model.addAttribute("accountDetail", accountDetail);
			return model.getError();
		}
	}

	@PreAuthorize("hasPermission('ROLE_MANAGER','isAtleast')")
	@RequestMapping(value = "/suspend/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showSuspendDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/suspend/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			XMLGregorianCalendar accessFeeDate = accountManager.getLastAccessFeeDate(user, accountDetail.getAccount());

			model.addAttribute("accountDetail", accountDetail);
			model.addAttribute("accessFeeDate", accessFeeDate);
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_MANAGER','isAtleast')")
	@RequestMapping(value = "/suspend/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postSuspendDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/suspend/success");

		try {
			deviceManager.suspendService(user.getUserId(), accountDetail.getAccount().getAccountNo(), accountDetail.getDeviceInfo().getId());
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			return model.getException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/restore/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showRestoreDevice(
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/restore/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			model.addAttribute("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/restore/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postRestoreDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("redirect:/devices", "account/device/restore/prompt");

		try {
			deviceManager.restoreService(user.getUserId(), accountDetail.getAccount().getAccountNo(), accountDetail.getDeviceInfo().getId());
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			return model.getException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showDisconnectDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/disconnect/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			XMLGregorianCalendar accessFeeDate = accountManager.getLastAccessFeeDate(user, accountDetail.getAccount());
			model.addAttribute("accountDetail", accountDetail);
			model.addAttribute("accessFeeDate", accessFeeDate);
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postDisconnectDevice(
			@ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ResultModel model = new ResultModel("account/device/disconnect/success", "account/device/disconnect/prompt");

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

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showReinstallDevice(
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable String encodedDeviceId) {

		ResultModel model = new ResultModel("account/device/reconnect/prompt");

		try {
			model.addAttribute("accountDetail", getAccountDetailFromSession(accountDetails, encodedDeviceId));
			return model.getSuccess();
		} catch (CachedAttributeNotFound e) {
			return model.getAccessException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','isAtleast')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postReinstallDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ResultModel model = new ResultModel("redirect:/devices", "account/device/reconnect/prompt");

		try {
			deviceManager.reinstallCustomerDevice(user, accountDetail.getDeviceInfo());
			return model.getSuccess();
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.reinstall.error");
			return model.getError();
		}
	}

	/* ****************************************************************************************************************
	 * Helper Methods
	 * ****************************************************************************************************************
	 */

	private AccountDetail getAccountDetailFromSession(
			List<AccountDetail> accountDetails, String encodedDeviceId) throws CachedAttributeNotFound {

		for (AccountDetail ad : accountDetails)
			if (ad.getEncodedDeviceId().equals(encodedDeviceId))
				return ad;

		throw new CachedAttributeNotFound("Could not find AccountDetail for Device");
	}

}