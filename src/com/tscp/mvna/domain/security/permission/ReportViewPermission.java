package com.tscp.mvna.domain.security.permission;

import org.springframework.security.core.Authentication;

import com.trc.user.authority.ROLE;

public class ReportViewPermission extends MinimumRolePermission {

	@Override
	public boolean isAllowed(
			Authentication authentication, Object targetDomainObject) {

		return super.isAllowed(authentication, ROLE.ROLE_MANAGER);
	}
}
