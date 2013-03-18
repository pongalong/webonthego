package com.tscp.domain.security.permission;

import org.springframework.security.core.Authentication;

import com.trc.user.User;
import com.trc.user.authority.ROLE;

public class RoleAtLeastPermission extends Permission {

	@Override
	public boolean isAllowed(
			Authentication authentication,
			Object targetDomainObject) {

		boolean hasPermission = false;

		if (isAuthenticated(authentication)) {
			User user = (User) authentication.getPrincipal();
			String role = (String) targetDomainObject;
			hasPermission = user.getGreatestAuthority().compare(ROLE.valueOf(role)) >= 0;
		}

		return hasPermission;
	}

}
