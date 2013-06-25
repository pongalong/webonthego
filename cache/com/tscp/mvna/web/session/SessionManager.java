package com.tscp.mvna.web.session;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tscp.mvna.security.encryption.StringEncryptor;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

public class SessionManager {

	protected static final HttpSession getCurrentSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	protected static final void set(
			String key, Object obj) {
		HttpSession session = getCurrentSession();
		if (session != null)
			session.setAttribute(key, obj);
	}

	protected static final Object get(
			String key) {
		HttpSession session = getCurrentSession();
		return session != null ? session.getAttribute(key) : null;
	}

	protected static final void clear(
			String key) {
		HttpSession session = getCurrentSession();
		if (session != null)
			session.removeAttribute(key);
	}

	public static final String getSessionId() {
		return getCurrentSession().getId();
	}

	@Loggable(value = LogLevel.TRACE)
	public static final StringEncryptor getEncryptor() {
		StringEncryptor encryptor = (StringEncryptor) get(SessionKey.ENCRYPTOR.toString());
		return encryptor == null ? new StringEncryptor(SessionManager.getSessionId()) : encryptor;
	}
}