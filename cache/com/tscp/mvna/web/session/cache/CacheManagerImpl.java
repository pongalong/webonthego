package com.tscp.mvna.web.session.cache;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.manager.AccountManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetailCollection;
import com.trc.user.account.PaymentHistory;
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

		if (fromCache.isStale() || fromCache.isInvalidated()) {
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

		AccountDetailCollection accountDetails = accountManager.getAccountDetailCollection(user);
		logger.debug("... loaded {} accounts", accountDetails.size());

		PaymentHistory paymentHistory = accountManager.getPaymentHistory(user);
		logger.debug("... loaded {} PaymentRecords", paymentHistory.size());
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