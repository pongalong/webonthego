package com.trc.manager;

import com.trc.exception.management.AccountManagementException;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.AccountDetailCollection;
import com.trc.user.account.PaymentHistory;
import com.trc.user.account.UsageHistory;
import com.tscp.mvne.Account;
import com.tscp.mvne.Device;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

public interface AccountManagerModel {

	@Loggable(value = LogLevel.INFO)
	public Account getUnlinkedAccount(
			User user) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public Account createShellAccount(
			User user) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public UsageHistory getUsageHistory(
			User user, int accountNo) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public PaymentHistory getPaymentHistory(
			User user) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public void updateEmail(
			Account account) throws AccountManagementException;

	public Account getAccount(
			int accountNumber) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public AccountDetail getAccountDetail(
			User user, Device device) throws AccountManagementException;

	@Loggable(value = LogLevel.INFO)
	public AccountDetailCollection getAccountDetails(
			User user) throws AccountManagementException;
}
