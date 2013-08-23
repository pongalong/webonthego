package com.trc.user.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public Overview(AccountDetailCollection accountDetails, PaymentHistory paymentHistory) {
		this.accountDetails = accountDetails;
		this.paymentHistory = paymentHistory;
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