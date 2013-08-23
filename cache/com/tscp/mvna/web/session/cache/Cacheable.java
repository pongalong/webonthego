package com.tscp.mvna.web.session.cache;

public interface Cacheable {

	public String getCacheKey();

	public boolean isStale();

	public boolean isInvalidated();

	public void updateCachedTime();

}