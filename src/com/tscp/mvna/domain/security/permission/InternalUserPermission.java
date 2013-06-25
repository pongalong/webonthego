package com.tscp.mvna.domain.security.permission;

import org.springframework.security.core.Authentication;

import com.trc.user.User;

public class InternalUserPermission extends Permission {

	@Override
	public boolean isAllowed(
			Authentication authentication,
			Object targetDomainObject) {

		boolean hasPermission = false;

		if (isAuthenticated(authentication)) {
			User user = (User) authentication.getPrincipal();
			hasPermission = user.isInternalUser();
		}

		return hasPermission;
	}

}
