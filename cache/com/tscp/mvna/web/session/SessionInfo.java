package com.tscp.mvna.web.session;

import java.util.List;

import org.springframework.security.core.session.SessionInformation;

import com.trc.user.User;

public class SessionInfo {
	private User user;
	private List<SessionInformation> sessionInformation;

	public SessionInfo() {
		// default
	}

	public SessionInfo(User user, List<SessionInformation> sessionInformation) {
		this.user = user;
		this.sessionInformation = sessionInformation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(
			User user) {
		this.user = user;
	}

	public List<SessionInformation> getSessionInformation() {
		return sessionInformation;
	}

	public void setSessionInformation(
			List<SessionInformation> sessionInformation) {
		this.sessionInformation = sessionInformation;
	}

}