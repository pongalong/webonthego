package com.trc.web.session;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionManager {

	public static final HttpSession getCurrentSession() {
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
		if (session != null)
			return session.getAttribute(key);
		else
			return null;
	}

	protected static final void clear(
			String key) {
		HttpSession session = getCurrentSession();
		if (session != null)
			session.removeAttribute(key);
	}

}