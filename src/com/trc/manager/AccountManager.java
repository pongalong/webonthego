package com.trc.manager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.AccountCreationException;
import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.DeviceManagementException;
import com.trc.exception.service.AccountServiceException;
import com.trc.service.AccountService;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.Overview;
import com.trc.user.account.UsageHistory;
import com.trc.web.session.cache.CacheKey;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CustInfo;
import com.tscp.mvne.CustTopUp;
import com.tscp.mvne.Device;
import com.tscp.mvne.PaymentRecord;
import com.tscp.mvne.UsageDetail;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
@SuppressWarnings("unchecked")
public class AccountManager {
	@Autowired
	private AccountService accountService;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private CacheManager cacheManager;

	@Loggable(value = LogLevel.TRACE)
	public Account getUnlinkedAccount(
			User user) throws AccountManagementException {
		try {
			return accountService.getUnlinkedAccount(user);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e);
		}
	}

	@Loggable(value = LogLevel.INFO)
	public Account createShellAccount(
			User user) throws AccountCreationException {
		try {
			return accountService.createShellAccount(user);
		} catch (AccountServiceException e) {
			throw new AccountCreationException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public CustInfo getCustInfo(
			User user) throws AccountManagementException {
		try {
			return accountService.getCustInfo(user);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public AccountDetail getAccountDetail(
			User user, Device device) throws AccountManagementException {
		return getAccountDetail(user, device, false);
	}

	@Loggable(value = LogLevel.TRACE)
	public AccountDetail getAccountDetailAndUsage(
			User user, Device device) throws AccountManagementException {
		return getAccountDetail(user, device, true);
	}

	@Loggable(value = LogLevel.TRACE)
	private AccountDetail getAccountDetail(
			User user, Device device, boolean getUsage) throws AccountManagementException {

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
			accountDetail.setUsageHistory(new UsageHistory(getChargeHistory(user, account.getAccountNo()), user, account.getAccountNo()));
		return accountDetail;
	}

	@Loggable(value = LogLevel.TRACE)
	public AccountDetail getAccountDetail(
			User user, int deviceId) throws AccountManagementException {
		try {
			return getAccountDetail(user, deviceManager.getDeviceInfo(user, deviceId));
		} catch (DeviceManagementException e) {
			throw new AccountManagementException(e);
		}
	}

	@Loggable(value = LogLevel.INFO)
	public List<AccountDetail> getAllAccountDetails(
			User user) throws AccountManagementException {

		List<Device> devices;
		List<AccountDetail> accountDetails;

		try {
			devices = deviceManager.getDeviceInfoList(user);
		} catch (DeviceManagementException e) {
			devices = new ArrayList<Device>();
		}

		accountDetails = new ArrayList<AccountDetail>();
		try {
			for (Device device : devices)
				accountDetails.add(getAccountDetail(user, device));
			saveToCache(accountDetails);
		} catch (AccountManagementException e) {
			throw e;
		}
		return accountDetails;
	}

	@Loggable(value = LogLevel.DEBUG)
	private void saveToCache(
			List<AccountDetail> accountDetails) {
		cacheManager.set(CacheKey.ACCOUNT_DETAILS, accountDetails);
	}

	@Loggable(value = LogLevel.DEBUG)
	private List<AccountDetail> getFromCache() {
		return (List<AccountDetail>) cacheManager.get(CacheKey.ACCOUNT_DETAILS);
	}

	@Loggable(value = LogLevel.TRACE)
	public Account getAccount(
			int accountNumber) throws AccountManagementException {

		List<AccountDetail> accountDetails = getFromCache();
		if (accountDetails != null)
			for (AccountDetail ad : accountDetails)
				if (ad.getAccount().getAccountNo() == accountNumber)
					return ad.getAccount();

		try {
			return accountService.getAccount(accountNumber);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	// @Loggable(value = LogLevel.TRACE)
	// public List<CustAcctMapDAO> getAccountMap(
	// User user) throws AccountManagementException {
	// try {
	// return accountService.getAccountMap(user);
	// } catch (AccountServiceException e) {
	// throw new AccountManagementException(e.getMessage(), e.getCause());
	// }
	// }

	// @Loggable(value = LogLevel.TRACE)
	// public List<Account> getAccounts(
	// User user) throws AccountManagementException {
	// try {
	// List<Account> accountList = new ArrayList<Account>();
	// for (CustAcctMapDAO accountMap : getAccountMap(user))
	// accountList.add(getAccount(accountMap.getAccountNo()));
	// return accountList;
	// } catch (AccountManagementException e) {
	// throw e;
	// }
	// }

	@Loggable(value = LogLevel.TRACE)
	public List<UsageDetail> getChargeHistory(
			User user, int accountNumber) throws AccountManagementException {
		try {
			return accountService.getChargeHistory(user, accountNumber);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	// @Deprecated
	// @Loggable(value = LogLevel.TRACE)
	// public ContactInfo getDefaultContactInfo(
	// User user) throws AccountManagementException, AddressManagementException {
	// List<Account> accountList;
	// try {
	// accountList = getAccounts(user);
	// Account account = accountList.get(0);
	// Address address = addressManager.getDefaultAddress(user);
	// ContactInfo contactInfo = new ContactInfo();
	// contactInfo.setAddress(address);
	// contactInfo.setFirstName(account.getFirstname());
	// contactInfo.setLastName(account.getLastname());
	// contactInfo.setPhoneNumber(account.getContactNumber());
	// return contactInfo;
	// } catch (AccountManagementException e) {
	// throw e;
	// } catch (AddressManagementException e) {
	// throw e;
	// }
	// }

	@Loggable(value = LogLevel.TRACE)
	public XMLGregorianCalendar getLastAccessFeeDate(
			User user, Account account) {

		List<UsageDetail> usageDetails;
		try {
			usageDetails = getChargeHistory(user, account.getAccountNo());
		} catch (AccountManagementException e) {
			usageDetails = new ArrayList<UsageDetail>();
		}

		for (UsageDetail usageDetail : usageDetails)
			if (usageDetail.getUsageType().equals("Access Fee"))
				return usageDetail.getEndTime();

		return null;
	}

	// @Loggable(value = LogLevel.TRACE)
	// public int getNumAccounts(
	// User user) {
	// try {
	// List<CustAcctMapDAO> accounts = getAccountMap(user);
	// return accounts != null ? accounts.size() : 0;
	// } catch (AccountManagementException e) {
	// return 0;
	// }
	// }

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

	@Loggable(value = LogLevel.INFO)
	public List<PaymentRecord> getPaymentRecords(
			User user) throws AccountManagementException {
		try {
			return accountService.getPaymentRecords(user);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.DEBUG)
	public CustTopUp getTopup(
			User user, Account account) throws AccountManagementException {
		try {
			return accountService.getTopUp(user, account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.INFO)
	public CustTopUp setTopup(
			User user, double amount, Account account) throws AccountManagementException {
		try {
			return accountService.setTopUp(user, amount, account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.INFO)
	public void updateEmail(
			Account account) throws AccountManagementException {
		try {
			accountService.updateEmail(account);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

}
