package com.trc.user.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.user.User;
import com.tscp.mvne.Device;

/**
 * Overview contains all accountDetails and the paymentHistory for the given user.
 * 
 * @author Tachikoma
 * 
 */
public class Overview {
	private static final Logger logger = LoggerFactory.getLogger(Overview.class);
	private AccountDetailCollection accountDetails;
	private PaymentHistory paymentHistory;

	public Overview(AccountManager accountManager, List<Device> devices, User user) {

		try {
			accountDetails = accountManager.getAccountDetails(user);
		} catch (AccountManagementException e) {
			logger.error("Exception building AccountDetailCollection for Overview", e);
		}

		try {
			paymentHistory = accountManager.getPaymentHistory(user);
		} catch (AccountManagementException e) {
			logger.error("Exception building PaymentHistory for Overview", e);
		}

	}

	public PaymentHistory getPaymentDetails() {
		return paymentHistory;
	}

	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	public AccountDetail getAccountDetail(
			int accountNo) {
		return accountDetails == null ? null : accountDetails.find(accountNo);
	}

}