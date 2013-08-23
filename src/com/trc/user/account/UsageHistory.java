package com.trc.user.account;

import java.io.Serializable;
import java.util.List;

import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.cache.Cacheable;
import com.tscp.mvne.UsageDetail;
import com.tscp.util.Paginator;

/**
 * UsageHistory is a paginated list of the user's usageDetails.
 * 
 * @author Tachikoma
 * 
 */
public class UsageHistory extends Paginator<UsageDetail> implements Cacheable, Serializable {
	private static final long serialVersionUID = -6335833376678017325L;

	/* **************************************
	 * Constructors
	 */

	public UsageHistory() {
		// do nothing
	}

	public UsageHistory(List<UsageDetail> usageDetails) {
		super(usageDetails);
	}

	/* **************************************
	 * Cacheable Methods
	 */

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