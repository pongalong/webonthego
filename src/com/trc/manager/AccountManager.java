package com.trc.manager;

import java.io.UnsupportedEncodingException;
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
import com.tscp.mvna.web.session.SessionManager;
import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CustInfo;
import com.tscp.mvne.CustTopUp;
import com.tscp.mvne.Device;
import com.tscp.mvne.UsageDetail;

@Component
public class AccountManager implements AccountManagerModel {
	private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private SessionManager sessionManager;

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

	/* **************************************
	 * Overview Methods
	 */

	public Overview getOverview(
			User user) {
		return new Overview(getAccountDetailCollection(user), getPaymentHistory(user));
	}

	/* **************************************
	 * Account Methods
	 */

	@Override
	public Account getAccount(
			int accountNo) throws AccountManagementException {

		AccountDetail cachedObject = fromCache(accountNo);
		if (cachedObject != null && !cachedObject.isInvalidated())
			return cachedObject.getAccount();

		try {
			return accountService.getAccount(accountNo);
		} catch (AccountServiceException e) {
			throw new AccountManagementException(e.getMessage(), e.getCause());
		}
	}

	/* ****************************************
	 * AccountDetail Methods
	 */

	private AccountDetailCollection fromCache() {
		return (AccountDetailCollection) cacheManager.fetch(new AccountDetailCollection());
	}

	private AccountDetail fromCache(
			int accountNo) {
		AccountDetailCollection cachedCollection = fromCache();
		AccountDetail cachedItem = cachedCollection == null ? null : cachedCollection.find(accountNo);
		return cachedItem != null && !cachedItem.isInvalidated() ? cachedItem : null;
	}

	private void cache(
			AccountDetail accountDetail) {
		AccountDetailCollection collection = fromCache();
		if (collection != null)
			collection.update(accountDetail);
		else
			cacheManager.cache(new AccountDetailCollection(accountDetail));
	}

	@Override
	public AccountDetail getAccountDetail(
			User user, Device device) throws AccountManagementException {

		AccountDetail cachedObject = fromCache(device.getAccountNo());
		if (cachedObject != null)
			return cachedObject;

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
		accountDetail.setDevice(device);
		accountDetail.setAccount(account);
		accountDetail.setTopUp(topup.getTopupAmount());
		accountDetail.setUsageHistory(getUsageHistory(user, account.getAccountNo()));

		try {
			accountDetail.setEncodedAccountNum(sessionManager.getEncryptor().encryptIntUrlSafe(account.getAccountNo()));
			accountDetail.setEncodedDeviceId(sessionManager.getEncryptor().encryptIntUrlSafe(device.getId()));
		} catch (UnsupportedEncodingException e) {
			logger.error("Error encoding identifiers for {}", accountDetail, e);
		}

		cache(accountDetail);
		return accountDetail;
	}

	public AccountDetailCollection getAccountDetailCollection(
			User user) {

		AccountDetailCollection collection = fromCache();
		if (collection != null)
			return collection;

		logger.debug("Building AccountDetailCollection from scratch");
		List<Device> devices = null;
		Device device = null;
		collection = new AccountDetailCollection();
		try {
			devices = deviceManager.getDeviceInfoList(user);
		} catch (DeviceManagementException e) {
			logger.error("Error getting devices for {}", user);
			return collection;
		}

		try {
			if (devices != null)
				for (Device d : devices) {
					device = d;
					collection.add(getAccountDetail(user, device));
				}
		} catch (AccountManagementException e) {
			logger.error("Error getting AccountDetail for {}", device);
			return collection;
		}

		logger.debug("Caching AccountDetailCollection");
		cacheManager.cache(collection);
		return collection;
	}

	/* ****************************************
	 * UsageHistory Methods
	 */

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

		for (UsageDetail usageDetail : usageHistory)
			if (usageDetail.getUsageType().equals("Access Fee"))
				return usageDetail.getEndTime();

		return null;
	}

	/* ****************************************
	 * PaymentHistory Methods
	 */

	@Override
	public PaymentHistory getPaymentHistory(
			User user) {
		try {
			PaymentHistory paymentHistory = new PaymentHistory(accountService.getPaymentRecords(user));
			cacheManager.cache(paymentHistory);
			return paymentHistory;
		} catch (AccountServiceException e) {
			logger.error("Error fetching PaymentRecords for PaymentHistory", e);
		}
		return new PaymentHistory();
	}

	/* ****************************************
	 * Customer Information Methods
	 */

	public CustInfo getCustInfo(
			User user) throws AccountManagementException {
		try {
			return accountService.getCustInfo(user);
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
