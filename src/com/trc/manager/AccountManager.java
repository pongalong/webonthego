package com.trc.manager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.AccountCreationException;
import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.DeviceManagementException;
import com.trc.exception.service.AccountServiceException;
import com.trc.service.AccountService;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.AccountDetailCollection;
import com.trc.user.account.Overview;
import com.trc.user.account.PaymentHistory;
import com.trc.user.account.UsageHistory;
import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CustInfo;
import com.tscp.mvne.CustTopUp;
import com.tscp.mvne.Device;
import com.tscp.mvne.UsageDetail;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class AccountManager implements AccountManagerModel {
	private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private CacheManager cacheManager;

	@Override
	public Account getUnlinkedAccount(
			User user) throws AccountManagementException {
		try {
			return accountService.getUnlinkedAccount(user);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e);
		}
	}

	@Override
	public Account createShellAccount(
			User user) throws AccountCreationException {
		try {
			return accountService.createShellAccount(user);
		} catch (AccountServiceException e) {
			throw new AccountCreationException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public AccountDetail getAccountDetail(
			User user, Device device) throws AccountManagementException {
		return getAccountDetail(user, device, false);
	}

	@Override
	public AccountDetailCollection getAccountDetails(
			User user) throws AccountManagementException {

		AccountDetailCollection cachedCollection = (AccountDetailCollection) cacheManager.fetch(new AccountDetailCollection());
		if (cachedCollection != null && !cachedCollection.isInvalidated()) {
			logger.debug("returning cachedColllection");
			return cachedCollection;
		}

		List<Device> devices;
		try {
			devices = deviceManager.getDeviceInfoList(user);
		} catch (DeviceManagementException e) {
			devices = new ArrayList<Device>();
		}

		AccountDetailCollection accountDetails = new AccountDetailCollection();
		try {
			for (Device device : devices)
				accountDetails.add(getAccountDetail(user, device));
		} catch (AccountManagementException e) {
			throw e;
		}

		cacheManager.cache(accountDetails);
		return accountDetails;
	}

	public CustInfo getCustInfo(
			User user) throws AccountManagementException {
		try {
			return accountService.getCustInfo(user);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	public AccountDetail getAccountDetail(
			User user, int deviceId) throws AccountManagementException {
		try {
			return getAccountDetail(user, deviceManager.getDeviceInfo(user, deviceId));
		} catch (DeviceManagementException e) {
			throw new AccountManagementException(e);
		}
	}

	public AccountDetail getAccountDetailAndUsage(
			User user, Device device) throws AccountManagementException {
		return getAccountDetail(user, device, true);
	}

	private AccountDetail getAccountDetail(
			User user, Device device, boolean getUsage) throws AccountManagementException {

		AccountDetailCollection cachedCollection = (AccountDetailCollection) cacheManager.fetch(new AccountDetailCollection());
		AccountDetail cachedObject = cachedCollection == null ? null : cachedCollection.findByDevice(device.getId());
		if (cachedObject != null && !cachedObject.isInvalidated()) {
			logger.debug("returning cachedObject");
			return cachedObject;
		}

		Account account;
		CustTopUp topup;

		try {
			account = getAccount(device.getAccountNo());
		} catch (AccountManagementException e) {
			throw e;
		}

		try {
			topup = getTopup(user, account);
		} catch (AccountManagementException e) {
			throw e;
		}

		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setDeviceInfo(device);
		accountDetail.setAccount(account);
		accountDetail.setTopUp(topup.getTopupAmount());

		if (getUsage)
			accountDetail.setUsageHistory(getUsageHistory(user, account.getAccountNo()));

		if (cachedCollection != null && cachedObject != null)
			cachedCollection.update(accountDetail, cachedObject);

		return accountDetail;
	}

	@Override
	public Account getAccount(
			int accountNo) throws AccountManagementException {

		Account account = null;
		AccountDetailCollection accountDetails = (AccountDetailCollection) cacheManager.fetch(new AccountDetailCollection());
		if (accountDetails != null)
			account = accountDetails.find(accountNo).getAccount();

		try {
			return account != null ? account : accountService.getAccount(accountNo);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public UsageHistory getUsageHistory(
			User user, int accountNo) throws AccountManagementException {
		try {
			return new UsageHistory(accountService.getChargeHistory(user, accountNo));
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	public XMLGregorianCalendar getLastAccessFeeDate(
			User user, Account account) {

		UsageHistory usageHistory;
		try {
			usageHistory = getUsageHistory(user, account.getAccountNo());
		} catch (AccountManagementException e) {
			usageHistory = new UsageHistory();
		}

		for (UsageDetail usageDetail : usageHistory.getRecords())
			if (usageDetail.getUsageType().equals("Access Fee"))
				return usageDetail.getEndTime();

		return null;
	}

	@Loggable(value = LogLevel.INFO)
	public Overview getOverview(
			User user) {
		List<Device> devices;
		try {
			devices = deviceManager.getDeviceInfoList(user);
		} catch (DeviceManagementException e) {
			devices = new ArrayList<Device>();
		}
		return new Overview(this, devices, user);
	}

	@Override
	public PaymentHistory getPaymentHistory(
			User user) throws AccountManagementException {
		try {
			return new PaymentHistory(accountService.getPaymentRecords(user));
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	public CustTopUp getTopup(
			User user, Account account) throws AccountManagementException {
		try {
			return accountService.getTopUp(user, account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	public CustTopUp setTopup(
			User user, double amount, Account account) throws AccountManagementException {
		try {
			return accountService.setTopUp(user, amount, account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void updateEmail(
			Account account) throws AccountManagementException {
		try {
			accountService.updateEmail(account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

}
