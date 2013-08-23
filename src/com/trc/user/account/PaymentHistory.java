package com.trc.user.account;

import java.io.Serializable;
import java.util.List;

import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.cache.Cacheable;
import com.tscp.mvne.PaymentRecord;
import com.tscp.util.Paginator;

/**
 * PaymentHistory is a paginated list of the user's paymentRecords.
 * 
 * @author Tachikoma
 * 
 */
public class PaymentHistory extends Paginator<PaymentRecord> implements Cacheable, Serializable {
	private static final long serialVersionUID = -5541427186661512811L;

	/* **************************************
	 * Constructors
	 */

	public PaymentHistory() {
		// do nothing
	}

	public PaymentHistory(List<PaymentRecord> paymentRecords) {
		super(paymentRecords);
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