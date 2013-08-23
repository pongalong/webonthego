package com.tscp.mvna.web.session.cache;

public interface CacheableCollection<T> extends Cacheable {

	public void update(
			T newObject, T oldObject);

}