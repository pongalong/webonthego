package com.tscp.mvna.domain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.trc.user.account.AccountDetailCollection;
import com.trc.web.validation.DeviceValidator;
import com.tscp.mvna.service.gateway.WebserviceAdapter;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;
import com.tscp.mvna.web.session.cache.CachedAttributeNotFound;
import com.tscp.mvne.Device;
import com.tscp.mvne.NetworkInfo;

@Controller
@RequestMapping("/devices")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"accountDetailCollection",
		"label",
		"accountDetail",
		"newDevice" })
public class DeviceController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private DeviceValidator deviceInfoValidator;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showDevices(
			@ModelAttribute AccountDetailCollection accountDetailCollection) {
		logger.debug("showDevices has @ModelAttribute {}", accountDetailCollection);
		return new ClientPageView("account/device/devices");
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView renameDevice(
			@ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/rename/prompt");

		try {
			AccountDetail accountDetail = accountDetails.findByDevice(encodedDeviceId);
			view.addObject("label", accountDetail.getDeviceInfo().getLabel());
			view.addObject("accountDetail", accountDetail);
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postRenameDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("label") String oldLabel, @ModelAttribute("accountDetail") AccountDetail accountDetail, BindingResult result) {

		ClientFormView view = new ClientFormView("account/device/rename/success", "account/device/rename/prompt");

		result.pushNestedPath("deviceInfo");
		deviceInfoValidator.checkDeviceLabel(accountDetail.getDeviceInfo().getLabel(), result);
		result.popNestedPath();

		if (result.hasErrors()) {
			accountDetail.getDeviceInfo().setLabel(oldLabel);
			return view.validationFailed();
		}

		try {
			deviceManager.updateDeviceInfo(user, accountDetail.getDeviceInfo());
			view.addObject("oldLabel", oldLabel);
			view.addObject("newLabel", accountDetail.getDeviceInfo().getLabel());
			return view;
		} catch (DeviceManagementException e) {
			result.reject("device.update.label.error", null, "There was an error renaming your device");
			return view.formError();
		}
	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showChangeTopUp(
			@ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {
		ClientPageView view = new ClientPageView("account/device/topup/change/prompt");
		view.addObject("accountDetail", accountDetails.findByDevice(encodedDeviceId));
		return view;
	}

	@RequestMapping(value = "/topup/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postChangeTopUp(
			@PathVariable String encodedDeviceId, @ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ClientFormView view = new ClientFormView("account/device/topup/change/success", "account/device/topup/change/prompt");

		try {
			accountDetail.setTopUp(accountDetail.getTopUp());
			accountManager.setTopup(user, new Double(accountDetail.getTopUp()), accountDetail.getAccount());
			return view;
		} catch (AccountManagementException e) {
			view.addObject("accountDetail", accountDetail);
			errors.rejectValue("topUp", "account.topUp.change.error");
			return view.formError();
		}
	}

	@Deprecated
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showSwapDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/swap/prompt");

		try {
			view.addObject("accountDetail", accountDetails.findByDevice(encodedDeviceId));
			view.addObject("newDevice", new Device());
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@Deprecated
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postSwapDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @ModelAttribute("newDevice") Device newDevice, Errors errors) {

		ClientFormView view = new ClientFormView("account/device/swap/success", "account/device/swap/prompt");

		try {
			// the existing device is in the accountDetail object
			Device oldDevice = accountDetail.getDeviceInfo();

			// fetch the old device and give it the new values so all other properties will be filled
			// TODO make a device wrapper class that implements cloneable
			Device newDeviceCopy = deviceManager.getDeviceInfo(user, accountDetail.getDeviceInfo().getId());
			newDeviceCopy.setLabel(newDevice.getLabel());
			newDeviceCopy.setValue(newDevice.getValue());

			deviceManager.swapDevice(user, oldDevice, newDeviceCopy);
			return view;
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.swap.error");
			view.addObject("accountDetail", accountDetail);
			return view.formError();
		}
	}

	@PreAuthorize("hasPermission('ROLE_MANAGER','minimumRole')")
	@RequestMapping(value = "/suspend/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showSuspendDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/suspend/prompt");

		try {
			AccountDetail accountDetail = accountDetails.findByDevice(encodedDeviceId);
			view.addObject("accountDetail", accountDetail);
			view.addObject("accessFeeDate", accountManager.getLastAccessFeeDate(user, accountDetail.getAccount()));
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_MANAGER','minimumRole')")
	@RequestMapping(value = "/suspend/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postSuspendDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/suspend/success");

		try {
			deviceManager.suspendService(user.getUserId(), accountDetail.getAccount().getAccountNo(), accountDetail.getDeviceInfo().getId());
			return view;
		} catch (DeviceManagementException e) {
			return view.exception();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/restore/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showRestoreDevice(
			@ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/restore/prompt");

		try {
			view.addObject("accountDetail", accountDetails.findByDevice(encodedDeviceId));
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/restore/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postRestoreDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @PathVariable String encodedDeviceId) {

		ClientFormView view = new ClientFormView("devices", "account/device/restore/prompt");

		try {
			deviceManager.restoreService(user.getUserId(), accountDetail.getAccount().getAccountNo(), accountDetail.getDeviceInfo().getId());
			return view.redirect();
		} catch (DeviceManagementException e) {
			return view.exception();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showDisconnectDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/disconnect/prompt");

		try {
			AccountDetail accountDetail = accountDetails.findByDevice(encodedDeviceId);
			view.addObject("accountDetail", accountDetail);
			view.addObject("accessFeeDate", accountManager.getLastAccessFeeDate(user, accountDetail.getAccount()));
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/disconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postDisconnectDevice(
			@ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ClientFormView view = new ClientFormView("account/device/disconnect/success", "account/device/disconnect/prompt");

		try {
			NetworkInfo networkInfo = deviceManager.getNetworkInfo(accountDetail.getDeviceInfo().getValue(), null);
			if (!deviceManager.compareEsn(accountDetail.getDeviceInfo(), networkInfo)) {
				errors.rejectValue("value", "device.deactivate.error");
				return view.formError();
			} else {
				deviceManager.disconnectService(WebserviceAdapter.toServiceInstance(networkInfo));
				return view;
			}
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.deactivate.error");
			return view.formError();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView showReinstallDevice(
			@ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable String encodedDeviceId) {

		ClientPageView view = new ClientPageView("account/device/reconnect/prompt");

		try {
			view.addObject("accountDetail", accountDetails.findByDevice(encodedDeviceId));
			return view;
		} catch (CachedAttributeNotFound e) {
			return view.dataFetchException();
		}
	}

	@PreAuthorize("hasPermission('ROLE_AGENT','minimumRole')")
	@RequestMapping(value = "/reconnect/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView postReinstallDevice(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, Errors errors) {

		ClientFormView view = new ClientFormView("devices", "account/device/reconnect/prompt");

		try {
			deviceManager.reinstallCustomerDevice(user, accountDetail.getDeviceInfo());
			return view.redirect();
		} catch (DeviceManagementException e) {
			errors.rejectValue("value", "device.reinstall.error");
			return view.formError();
		}
	}

}