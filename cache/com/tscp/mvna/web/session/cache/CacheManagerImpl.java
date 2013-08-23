package com.tscp.mvna.web.session.cache;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.AccountDetailCollection;
import com.tscp.mvna.web.session.SessionManagerImpl;

@Component
public class CacheManagerImpl extends SessionManagerImpl implements CacheManager {
	private static final Logger logger = LoggerFactory.getLogger(CacheManagerImpl.class);
	@Autowired
	private AccountManager accountManager;

	@Override
	public final void cache(
			Cacheable cacheable) {
		cacheable.updateCachedTime();
		super.set(cacheable.getCacheKey(), cacheable);
	}

	@Override
	public final Object fetch(
			Cacheable cacheable) {

		Cacheable fromCache = (Cacheable) super.get(cacheable.getCacheKey());

		if (fromCache == null)
			return null;

		if (fromCache.isStale()) {
			clear(cacheable);
			return null;
		}

		return fromCache;
	}

	@Override
	public final void clear(
			Cacheable cacheable) {
		super.remove(cacheable.getCacheKey());
	}

	@Override
	public void createCache(
			User user) {
		clear();
		buildCache();
	}

	private void buildCache() {

		User user = getTargetUser();

		if (user.getUserId() <= 0)
			return;

		logger.info("Building cache for {}", user);

		try {
			AccountDetailCollection accountDetailSet = accountManager.getAccountDetails(user);
			logger.debug("... loaded {} accounts", accountDetailSet.size());

			for (AccountDetail ad : accountDetailSet) {
				try {
					ad.setEncodedAccountNum(getEncryptor().encryptIntUrlSafe(ad.getAccount().getAccountNo()));
					ad.setEncodedDeviceId(getEncryptor().encryptIntUrlSafe(ad.getDeviceInfo().getId()));
				} catch (UnsupportedEncodingException e) {
					logger.error("Exception encrypting IDs for {}", user, e);
				}

				try {
					ad.setUsageHistory(accountManager.getUsageHistory(user, ad.getAccount().getAccountNo()));
				} catch (AccountManagementException e) {
					logger.error("Exception building UsageHistory for", user, e);
				}

			}

			cache(accountDetailSet);
		} catch (AccountManagementException e) {
			logger.error("Exception building AccountDetailCollection for {}", user, e);
		}

		try {
			cache(accountManager.getPaymentHistory(user));
		} catch (AccountManagementException e) {
			logger.error("Exception building PaymentHistory for {}", user, e);
		}
	}

	@Override
	public void refresh() {
		clear();
		buildCache();
	}

	private void clear() {
		Enumeration<String> cacheKeys = super.getSession().getAttributeNames();
		String key;
		Object obj;
		while (cacheKeys.hasMoreElements()) {
			key = cacheKeys.nextElement();
			obj = super.get(key);
			if (obj instanceof Cacheable)
				super.remove(key);
		}
	}

	@Override
	public User getTargetUser() {
		return (User) super.get(TARGET_USER);
	}

	@Override
	public void setTargetUser(
			User user) {
		super.set(TARGET_USER, user);
	}

}
