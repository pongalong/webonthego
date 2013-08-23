package com.trc.user.account;

import java.util.ArrayList;

import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.cache.Cacheable;
import com.tscp.mvne.CreditCard;

public class PaymentMethodCollection extends ArrayList<CreditCard> implements Cacheable {
	private static final long serialVersionUID = -7523524827847558570L;
	private long cachedTime = System.currentTimeMillis();
	private boolean invalidated;

	@Override
	public String getCacheKey() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean isStale() {
		return System.currentTimeMillis() - cachedTime > CacheManager.expirationTime;
	}

	@Override
	public boolean isInvalidated() {
		return invalidated;
	}

	public void setInvalidated(
			boolean invalidated) {
		this.invalidated = invalidated;
	}

	@Override
	public void updateCachedTime() {
		cachedTime = System.currentTimeMillis();
	}

}
