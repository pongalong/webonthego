package com.trc.user;

import com.trc.user.authority.AnonymousAuthority;

public class EmptyUser extends User {
	private static final long serialVersionUID = 4908326064981885494L;

	public EmptyUser() {
		setUserId(-1);
		getRoles().add(new AnonymousAuthority(this));
		setUsername(this.getClass().getSimpleName());
	}

}