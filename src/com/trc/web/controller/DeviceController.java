package com.trc.web.controller;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.DeviceManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.DeviceManager;
import com.trc.manager.UserManager;
import com.trc.security.encryption.SessionEncrypter;
import com.trc.service.gateway.TSCPMVNAUtil;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.Device;
import com.tscp.mvne.NetworkInfo;

@Controller
@RequestMapping("/devices")
public class DeviceController {
  @Autowired
  private UserManager userManager;
  @Autowired
  private AccountManager accountManager;
  @Autowired
  private DeviceManager deviceManager;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView showDevices() {
    ResultModel model = new ResultModel("devices/devices");
    User user = userManager.getCurrentUser();
    try {
      List<AccountDetail> devices = accountManager.getAccountDetailList(user);
      encodeDeviceIds(devices);
      model.addObject("devices", devices);
      return model.getSuccess();
    } catch (AccountManagementException e) {
      return model.getAccessException();
    }
  }

  @RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.GET)
  public ModelAndView renameDevice(@PathVariable String encodedDeviceId) {
    ResultModel model = new ResultModel("devices/rename");
    User user = userManager.getCurrentUser();
    try {
      Device deviceToRename = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      SessionManager.set(SessionKey.DEVICE_RENAME, deviceToRename);
      model.addObject("deviceInfo", deviceToRename);
      return model.getSuccess();
    } catch (DeviceManagementException e) {
      return model.getAccessException();
    }
  }

  @RequestMapping(value = "/rename/{encodedDeviceId}", method = RequestMethod.POST)
  public ModelAndView postRenameDevice(@PathVariable String encodedDeviceId, @ModelAttribute Device deviceInfo, Errors errors) {
    ResultModel model = new ResultModel("devices/renameSuccess", "devices/rename");
    User user = userManager.getCurrentUser();
    String newDeviceLabel = deviceInfo.getLabel();
    try {
      deviceInfo = (Device) SessionManager.get(SessionKey.DEVICE_RENAME);
      if (deviceInfo == null) {
        deviceInfo = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      }
      model.addObject("oldDeviceLabel", deviceInfo.getLabel());
      deviceInfo.setLabel(newDeviceLabel);
      deviceManager.updateDeviceInfo(user, deviceInfo);
      model.addObject("newDeviceLabel", deviceInfo.getLabel());
      return model.getSuccess();
    } catch (DeviceManagementException e) {
      errors.reject("device.update.label.error", null, "There was an error renaming your device");
      return model.getError();
    }
  }

  @RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.GET)
  public ModelAndView showSwapDevice(@PathVariable String encodedDeviceId) {
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

  @RequestMapping(value = "/swap/{encodedDeviceId}", method = RequestMethod.POST)
  public ModelAndView postSwapDevice(@PathVariable String encodedDeviceId, @ModelAttribute Device deviceInfo, Errors errors) {
    ResultModel model = new ResultModel("devices/swapEsnSuccess", "devices/swapEsn");
    User user = userManager.getCurrentUser();
    int deviceId = SessionEncrypter.decryptId(encodedDeviceId);
    String newEsn = deviceInfo.getValue();
    String newDeviceLabel = deviceInfo.getLabel();
    try {
      Device oldDeviceInfo = (Device) SessionManager.get(SessionKey.DEVICE_SWAP);
      Device newDeviceInfo = TSCPMVNAUtil.clone(oldDeviceInfo);
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

  @RequestMapping(value = "/deactivate/{encodedDeviceId}", method = RequestMethod.GET)
  public ModelAndView showDeactivateDevice(@PathVariable String encodedDeviceId) {
    ResultModel model = new ResultModel("devices/deactivatePrompt");
    User user = userManager.getCurrentUser();
    try {
      Device deviceInfo = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      Account account = accountManager.getAccount(deviceInfo.getAccountNo());
      XMLGregorianCalendar accessFeeDate = accountManager.getLastAccessFeeDate(user, account);
      SessionManager.set(SessionKey.DEVICE_DEACTIVATE, deviceInfo);
      SessionManager.set(SessionKey.DEVICE_ACCOUNT, account);
      SessionManager.set(SessionKey.DEVICE_ACCESSFEEDATE, accessFeeDate);
      model.addObject("accessFeeDate", accessFeeDate);
      model.addObject("account", account);
      model.addObject("deviceInfo", deviceInfo);
      return model.getSuccess();
    } catch (DeviceManagementException e) {
      return model.getAccessException();
    } catch (AccountManagementException e) {
      return model.getAccessException();
    }
  }

  @RequestMapping(value = "/deactivate/{encodedDeviceId}", method = RequestMethod.POST)
  public ModelAndView postDeactivateDevice(@PathVariable String encodedDeviceId, @ModelAttribute Device deviceInfo, Errors errors) {
    ResultModel model = new ResultModel("devices/deactivateSuccess", "devices/deactivatePrompt");
    User user = userManager.getCurrentUser();
    Device deviceInfoLookup = (Device) SessionManager.get(SessionKey.DEVICE_DEACTIVATE);
    Account account = (Account) SessionManager.get(SessionKey.DEVICE_ACCOUNT);
    XMLGregorianCalendar accessFeeDate = (XMLGregorianCalendar) SessionManager.get(SessionKey.DEVICE_ACCESSFEEDATE);
    try {
      if (deviceInfoLookup == null) {
        deviceInfoLookup = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      }
      if (account == null) {
        account = accountManager.getAccount(deviceInfoLookup.getAccountNo());
      }
      if (accessFeeDate == null) {
        accessFeeDate = accountManager.getLastAccessFeeDate(user, account);
      }
    } catch (DeviceManagementException e) {
      return model.getAccessException();
    } catch (AccountManagementException e) {
      return model.getAccessException();
    }

    try {
      NetworkInfo networkInfo = deviceManager.getNetworkInfo(deviceInfoLookup.getValue(), null);
      if (!deviceManager.compareEsn(deviceInfoLookup, networkInfo)) {
        model.addObject("accessFeeDate", accessFeeDate);
        model.addObject("account", account);
        model.addObject("deviceInfo", deviceInfoLookup);
        errors.rejectValue("value", "device.deactivate.error");
        return model.getError();
      } else {
        deviceManager.disconnectService(TSCPMVNAUtil.toServiceInstance(networkInfo));
        model.addObject("label", deviceInfoLookup.getLabel());
        return model.getSuccess();
      }
    } catch (DeviceManagementException e) {
      model.addObject("accessFeeDate", accessFeeDate);
      model.addObject("account", account);
      model.addObject("deviceInfo", deviceInfoLookup);
      errors.rejectValue("value", "device.deactivate.error");
      return model.getError();
    }
  }

  @RequestMapping(value = "/reinstall/{encodedDeviceId}", method = RequestMethod.GET)
  public ModelAndView showReinstallDevice(@PathVariable String encodedDeviceId) {
    ResultModel model = new ResultModel("devices/reinstallPrompt");
    User user = userManager.getCurrentUser();
    try {
      Device deviceToReactivate = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      SessionManager.set(SessionKey.DEVICE_REACTIVATE, deviceToReactivate);
      model.addObject("deviceInfo", deviceToReactivate);
      return model.getSuccess();
    } catch (DeviceManagementException e) {
      return model.getAccessException();
    }
  }

  @RequestMapping(value = "/reinstall/{encodedDeviceId}", method = RequestMethod.POST)
  public ModelAndView postReinstallDevice(@PathVariable String encodedDeviceId, @ModelAttribute Device deviceInfo, Errors errors) {
    ResultModel model = new ResultModel("redirect:/devices", "devices/reinstallPrompt");
    User user = userManager.getCurrentUser();
    try {
      Device deviceToReactivate = (Device) SessionManager.get(SessionKey.DEVICE_REACTIVATE);
      if (deviceToReactivate == null) {
        deviceToReactivate = deviceManager.getDeviceInfo(user, SessionEncrypter.decryptId(encodedDeviceId));
      }
      deviceManager.reinstallCustomerDevice(user, deviceToReactivate);
      return model.getSuccess();
    } catch (DeviceManagementException e) {
      errors.rejectValue("value", "device.reinstall.error");
      return model.getError();
    }
  }

  @RequestMapping(value = "/topUp/{encodedDeviceId}", method = RequestMethod.GET)
  public ModelAndView showChangeTopUp(@PathVariable String encodedDeviceId) {
    ResultModel model = new ResultModel("devices/changeTopUp");
    User user = userManager.getCurrentUser();
    try {
      int deviceId = SessionEncrypter.decryptId(encodedDeviceId);
      AccountDetail accountDetail = accountManager.getAccountDetail(user, deviceId);
      // SessionManager.set(SessionKey.DEVICE_ACCOUNTDETAIL, accountDetail);
      model.addObject("accountDetail", accountDetail);
      return model.getSuccess();
    } catch (AccountManagementException e) {
      return model.getAccessException();
    }
  }

  @RequestMapping(value = "/topUp/{encodedDeviceId}", method = RequestMethod.POST)
  public ModelAndView postChangeTopUp(@PathVariable String encodedDeviceId, @ModelAttribute AccountDetail accountDetail, Errors errors) {
    ResultModel model = new ResultModel("devices/changeTopUpSuccess", "devices/changeTopUp");
    User user = userManager.getCurrentUser();
    int deviceId = SessionEncrypter.decryptId(encodedDeviceId);
    String newTopUp = accountDetail.getTopUp();
    accountDetail = (AccountDetail) SessionManager.get(SessionKey.DEVICE_ACCOUNTDETAIL);
    try {
      if (accountDetail == null) {
        accountDetail = accountManager.getAccountDetail(user, deviceId);
      }
      accountDetail.setTopUp(newTopUp);
      accountManager.setTopUp(user, new Double(newTopUp), accountDetail.getAccount());
      model.addObject("accountDetail", accountDetail);
      return model.getSuccess();
    } catch (AccountManagementException e) {
      model.addObject("accountDetail", accountDetail);
      errors.rejectValue("topUp", "account.topUp.change.error");
      return model.getError();
    }
  }

  private void encodeDeviceIds(List<AccountDetail> accountDetailList) {
    for (AccountDetail accountDetail : accountDetailList) {
      accountDetail.setEncodedDeviceId(SessionEncrypter.encryptId(accountDetail.getDeviceInfo().getId()));
    }
  }

}