package com.trc.user.account;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.user.User;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvne.Device;
import com.tscp.util.logger.DevLogger;

/**
 * Overview contains all accountDetails and the paymentHistory for the given user.
 * 
 * @author Tachikoma
 * 
 */
public class Overview {
	private List<AccountDetail> accountDetails;
	private PaymentHistory paymentHistory;

	public Overview(AccountManager accountManager, List<Device> devices, User user) {

		try {
			this.paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
		} catch (AccountManagementException e) {
			DevLogger.error("Could not fetch paymentHistory for user " + user.toShortString(), e);
		}

		this.accountDetails = new ArrayList<AccountDetail>();
		AccountDetail accountDetail;
		for (Device device : devices) {
			try {
				accountDetail = accountManager.getAccountDetail(user, device);
				accountDetail.setEncodedAccountNum(CacheManager.getEncryptor().encryptIntUrlSafe(accountDetail.getAccount().getAccountNo()));
				accountDetail.setEncodedDeviceId(CacheManager.getEncryptor().encryptIntUrlSafe(accountDetail.getDeviceInfo().getId()));
				this.accountDetails.add(accountDetail);
			} catch (AccountManagementException e) {
				DevLogger.error("Could not fetch accountDetail for account " + device.getAccountNo(), e);
			} catch (UnsupportedEncodingException e) {
				DevLogger.error("Exception encoding IDs while creating Overview", e);
			}
		}
	}

	public PaymentHistory getPaymentDetails() {
		return paymentHistory;
	}

	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	public AccountDetail getAccountDetail(
			int accountNum) {
		for (AccountDetail accountDetail : accountDetails)
			if (accountDetail.getAccount().getAccountNo() == accountNum)
				return accountDetail;
		return null;
	}

}