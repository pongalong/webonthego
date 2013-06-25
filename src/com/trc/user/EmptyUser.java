package com.trc.user;

import com.trc.user.authority.AnonymousAuthority;
import com.tscp.mvna.web.session.SessionManager;

public class EmptyUser extends User {
	private static final long serialVersionUID = 4908326064981885494L;

	public EmptyUser() {
		setUserId(-1);
		getRoles().add(new AnonymousAuthority(this));
		setUsername(SessionManager.getSessionId());
	}

}