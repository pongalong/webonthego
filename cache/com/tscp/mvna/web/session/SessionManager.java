package com.tscp.mvna.web.session;

import javax.annotation.PostConstruct;

import com.trc.user.User;
import com.tscp.mvna.web.session.security.Encryptor;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

public interface SessionManager {
	public static final String USER = "CONTROLLING_USER";

	@PostConstruct
	public void init();

	public String getSessionId();

	public Object get(
			SessionObject sessionObject);

	public void set(
			SessionObject sessionObject);

	public void remove(
			SessionObject sessionObject);

	public Encryptor getEncryptor();

	public User getUser();
	
	public void setUser(User user);

	public User getTargetUser();
	
	public void setTargetUser(User user);

	@Loggable(value = LogLevel.TRACE)
	public void createSession(
			User user);

}