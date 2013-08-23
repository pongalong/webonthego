package com.tscp.mvna.web.session.cache;

import com.trc.user.User;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

public interface CacheManager {
	public static final String TARGET_USER = "USER";
	public static long expirationTime = 90000;

	@Loggable(value = LogLevel.TRACE)
	public Object fetch(
			Cacheable cacheable);

	@Loggable(value = LogLevel.TRACE)
	public void cache(
			Cacheable cacheable);

	@Loggable(value = LogLevel.TRACE)
	public void clear(
			Cacheable cacheable);

	@Loggable(value = LogLevel.TRACE)
	public void refresh();

	@Loggable(value = LogLevel.TRACE)
	public User getTargetUser();

	@Loggable(value = LogLevel.TRACE)
	public void setTargetUser(
			User user);

	@Loggable(value = LogLevel.TRACE)
	public void createCache(
			User user);

}