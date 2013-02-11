package com.trc.web.session.cache;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.security.encryption.StringEncryptor;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.PaymentHistory;
import com.trc.user.account.UsageHistory;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.tscp.mvne.UsageDetail;
import com.tscp.util.logger.DevLogger;

@Component
public final class CacheManager extends SessionManager {
	@Autowired
	private AccountManager accountManager;

	public static final void set(
			Enum cacheKey,
			Object obj) {
		SessionManager.set(cacheKey.toString(), obj);
	}

	public static final Object get(
			Enum cacheKey) {
		return SessionManager.get(cacheKey.toString());
	}

	public static final void clear(
			Enum cacheKey) {
		SessionManager.clear(cacheKey.toString());
	}

	public final void clearCache() {
		for (CacheKey cacheKey : CacheKey.values())
			clear(cacheKey);
	}

	public static final StringEncryptor getEncryptor() {
		StringEncryptor encryptor = (StringEncryptor) get(SessionKey.ENCRYPTOR);
		return encryptor;
	}

	public final void refreshCache(
			User user) {

		clearCache();

		if (user.getUserId() <= 0)
			return;

		DevLogger.debug("Refreshing cache for user " + user.getUsername());

		try {
			List<AccountDetail> accountDetails = accountManager.getAllAccountDetails(user);
			DevLogger.trace("... found " + accountDetails.size() + " account details");

			for (AccountDetail ad : accountDetails) {
				try {
					DevLogger.trace("... encoding identifiers");
					ad.setEncodedAccountNum(getEncryptor().encryptIntUrlSafe(ad.getAccount().getAccountNo()));
					ad.setEncodedDeviceId(getEncryptor().encryptIntUrlSafe(ad.getDeviceInfo().getId()));
				} catch (UnsupportedEncodingException e) {
					DevLogger.error("Exception encrypting IDs for user " + user.getUserId(), e);
				}

				try {
					DevLogger.trace("... fetching usage history");
					List<UsageDetail> usageDetails = accountManager.getChargeHistory(user, ad.getAccount().getAccountNo());
					ad.setUsageHistory(new UsageHistory(usageDetails, user, ad.getAccount().getAccountNo()));
				} catch (AccountManagementException e) {
					DevLogger.error("Exception refreshing usage history for user " + user.getUserId(), e);
				}

			}
			set(CacheKey.ACCOUNT_DETAILS, accountDetails);
		} catch (AccountManagementException e) {
			DevLogger.error("Exception refreshing account details for user " + user.getUserId(), e);
		}

		try {
			DevLogger.trace("... fetching payment history");
			PaymentHistory paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
			set(CacheKey.PAYMENT_HISTORY, paymentHistory);
		} catch (AccountManagementException e) {
			DevLogger.error("Exception refreshing paymentHistory for user " + user.getUserId(), e);
		}

	}

}