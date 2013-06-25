package com.tscp.mvna.web.session.cache;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.user.EmptyUser;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.PaymentHistory;
import com.trc.user.account.UsageHistory;
import com.tscp.mvna.config.Config;
import com.tscp.mvna.security.encryption.StringEncryptor;
import com.tscp.mvna.web.session.SessionKey;
import com.tscp.mvna.web.session.SessionManager;
import com.tscp.mvne.UsageDetail;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class CacheManager extends SessionManager {
	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
	@Autowired
	private AccountManager accountManager;

	@Loggable(value = LogLevel.TRACE)
	public static final void set(
			Enum cacheKey, Object obj) {
		SessionManager.set(cacheKey.toString(), obj);
	}

	@Loggable(value = LogLevel.TRACE)
	public static final Object get(
			Enum cacheKey) {
		return SessionManager.get(cacheKey.toString());
	}

	@Loggable(value = LogLevel.TRACE)
	public static final void clear(
			Enum cacheKey) {
		SessionManager.clear(cacheKey.toString());
	}

	@Loggable(value = LogLevel.INFO)
	public final void clearCache() {
		for (CacheKey cacheKey : CacheKey.values())
			clear(cacheKey);
	}

	@Loggable(value = LogLevel.INFO)
	public void beginSession(
			User user) {
		if (Config.ADMIN && user.isInternalUser()) {
			set(SessionKey.CONTROLLING_USER, user);
			set(SessionKey.USER, new EmptyUser());
		} else {
			set(SessionKey.CONTROLLING_USER, new EmptyUser());
			set(SessionKey.USER, user);
		}
		set(SessionKey.ENCRYPTOR, new StringEncryptor(SessionManager.getCurrentSession().getId()));
		refreshCache(user);
	}

	@Loggable(value = LogLevel.INFO)
	public void refreshCache(
			User user) {

		clearCache();

		if (user.getUserId() <= 0)
			return;

		logger.debug("Refreshing cache for user " + user.getUsername());

		try {
			List<AccountDetail> accountDetails = accountManager.getAllAccountDetails(user);
			logger.debug("... found " + accountDetails.size() + " account details");

			for (AccountDetail ad : accountDetails) {
				try {
					logger.debug("... encoding identifiers");
					ad.setEncodedAccountNum(getEncryptor().encryptIntUrlSafe(ad.getAccount().getAccountNo()));
					ad.setEncodedDeviceId(getEncryptor().encryptIntUrlSafe(ad.getDeviceInfo().getId()));
				} catch (UnsupportedEncodingException e) {
					logger.error("Exception encrypting IDs for user " + user.getUserId(), e);
				}

				try {
					logger.debug("... fetching usage history");
					List<UsageDetail> usageDetails = accountManager.getChargeHistory(user, ad.getAccount().getAccountNo());
					ad.setUsageHistory(new UsageHistory(usageDetails, user, ad.getAccount().getAccountNo()));
				} catch (AccountManagementException e) {
					logger.error("Exception refreshing usage history for user " + user.getUserId(), e);
				}

			}
			set(CacheKey.ACCOUNT_DETAILS, accountDetails);
		} catch (AccountManagementException e) {
			logger.error("Exception refreshing account details for user " + user.getUserId(), e);
		}

		try {
			logger.debug("... fetching payment history");
			PaymentHistory paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
			set(CacheKey.PAYMENT_HISTORY, paymentHistory);
		} catch (AccountManagementException e) {
			logger.error("Exception refreshing paymentHistory for user " + user.getUserId(), e);
		}

	}

}