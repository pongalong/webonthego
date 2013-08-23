package com.trc.user.account;

import java.util.ArrayList;

import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.cache.CacheableCollection;

public class AccountDetailCollection extends ArrayList<AccountDetail> implements CacheableCollection<AccountDetail> {
	private static final long serialVersionUID = -6526828794634799516L;

	@Override
	public String getCacheKey() {
		String simpleName = this.getClass().getSimpleName();
		simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
		return simpleName;
		// return this.getClass().getSimpleName();
	}

	public AccountDetail find(
			String needle) {
		for (AccountDetail candidate : this)
			if (candidate.getEncodedAccountNum().equals(needle))
				return candidate;
		return null;
	}

	public AccountDetail find(
			int needle) {
		for (AccountDetail candidate : this)
			if (candidate.getAccount().getAccountNo() == needle)
				return candidate;
		return null;
	}

	public AccountDetail findByDevice(
			int needle) {
		for (AccountDetail candidate : this)
			if (candidate.getDeviceInfo().getId() == needle)
				return candidate;
		return null;
	}

	public AccountDetail findByDevice(
			String needle) {
		for (AccountDetail candidate : this)
			if (candidate.getEncodedDeviceId().equals(needle))
				return candidate;
		return null;
	}

	private long cachedTime = System.currentTimeMillis();
	private boolean invalidated;

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

	@Override
	public void update(
			AccountDetail newObject, AccountDetail oldObject) {
		super.remove(oldObject);
		super.add(newObject);
	}

}