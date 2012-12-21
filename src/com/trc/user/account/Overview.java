package com.trc.user.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.security.encryption.SessionEncrypter;
import com.trc.user.User;
import com.tscp.mvne.Account;
import com.tscp.mvne.Device;
import com.tscp.mvne.UsageDetail;

/**
 * Overview contains all accountDetails and the paymentHistory for the given
 * user.
 * 
 * @author Tachikoma
 * 
 */
public class Overview {
	private List<AccountDetail> accountDetails;
	private PaymentHistory paymentHistory;

	public Overview(AccountManager accountManager, List<Device> devices, User user) {
		this.accountDetails = new ArrayList<AccountDetail>();
		AccountDetail accountDetail;
		Account account;
		try {
			this.paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
			for (Device deviceInfo : devices) {
				account = accountManager.getAccount(deviceInfo.getAccountNo());
				accountDetail = new AccountDetail();
				accountDetail.setAccount(account);
				accountDetail.setDeviceInfo(deviceInfo);
				accountDetail.setTopUp(accountManager.getTopUp(user, account).getTopupAmount());

				List<UsageDetail> usageDetail = accountManager.getChargeHistory(user, account.getAccountNo());
				UsageHistory uh = new UsageHistory(usageDetail, user, account.getAccountNo());
				accountDetail.setUsageHistory(uh);
				this.accountDetails.add(accountDetail);
			}
		} catch (AccountManagementException e) {
			e.printStackTrace();
		}
	}

	public PaymentHistory getPaymentDetails() {
		return paymentHistory;
	}

	public void setPaymentDetails(PaymentHistory paymentDetails) {
		this.paymentHistory = paymentDetails;
	}

	public AccountDetail getAccountDetail(int accountNum) {
		for (AccountDetail accountDetail : accountDetails) {
			if (accountDetail.getAccount().getAccountNo() == accountNum) {
				return accountDetail;
			}
		}
		return null;
	}

	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(List<AccountDetail> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public Overview encodeAll() {
		for (AccountDetail accountDetail : accountDetails) {
			accountDetail.setEncodedAccountNum(SessionEncrypter.encryptId(accountDetail.getAccount().getAccountNo()));
			accountDetail.setEncodedDeviceId(SessionEncrypter.encryptId(accountDetail.getDeviceInfo().getId()));
		}
		return this;
	}

	public Overview encodeDeviceId() {
		for (AccountDetail accountDetail : accountDetails) {
			accountDetail.setEncodedDeviceId(SessionEncrypter.encryptId(accountDetail.getDeviceInfo().getId()));
		}
		return this;
	}

	public Overview encodeAccountNo() {
		for (AccountDetail accountDetail : accountDetails) {
			accountDetail.setEncodedAccountNum(SessionEncrypter.encryptId(accountDetail.getAccount().getAccountNo()));
		}
		return this;
	}

}