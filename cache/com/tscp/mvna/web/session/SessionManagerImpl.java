package com.tscp.mvna.web.session;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trc.user.EmptyUser;
import com.trc.user.User;
import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.security.Encryptor;
import com.tscp.mvna.web.session.security.UrlSafeEncryptor;

@Component
public class SessionManagerImpl implements SessionManager {
	private HttpSession session;
	private Encryptor encryptor;

	public final void init() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		session = attr.getRequest().getSession(true);
		encryptor = new UrlSafeEncryptor(session.getId());
	}

	@Override
	public String getSessionId() {
		return session == null ? null : session.getId();
	}

	@Override
	public final Encryptor getEncryptor() {
		return encryptor;
	}

	@Override
	public final Object get(
			SessionObject sessionObject) {
		return session.getAttribute(sessionObject.getSessionKey());
	}

	@Override
	public final void set(
			SessionObject sessionObject) {
		session.setAttribute(sessionObject.getSessionKey(), sessionObject);
	}

	@Override
	public final void remove(
			SessionObject sessionObject) {
		session.removeAttribute(sessionObject.getSessionKey());
	}

	@Override
	public User getUser() {
		return (User) get(USER);
	}

	@Override
	public User getTargetUser() {
		return (User) get(CacheManager.TARGET_USER);
	}

	@Override
	public void createSession(
			User user) {

		// TODO the controlling user and user should both initially be the same
		if (user.isInternalUser()) {
			set(USER, user);
			set(CacheManager.TARGET_USER, new EmptyUser());
		} else {
			set(USER, new EmptyUser());
			set(CacheManager.TARGET_USER, user);
		}

	}

	protected final void set(
			String key, Object obj) {
		session.setAttribute(key, obj);
	}

	protected final Object get(
			String key) {
		return session.getAttribute(key);
	}

	protected final void remove(
			String key) {
		session.removeAttribute(key);
	}

	protected HttpSession getSession() {
		return session;
	}

	@Override
	public void setUser(
			User user) {
		set(USER, user);
	}

	@Override
	public void setTargetUser(
			User user) {
		set(CacheManager.TARGET_USER, user);
	}

}
