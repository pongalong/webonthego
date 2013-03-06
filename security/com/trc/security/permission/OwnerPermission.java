package com.trc.security.permission;

import org.springframework.security.core.Authentication;

import com.trc.user.User;

public class OwnerPermission extends Permission {

	@Override
	public boolean isAllowed(
			Authentication authentication,
			Object targetDomainObject) {

		boolean hasPermission = false;

		if (isAuthenticated(authentication)) {
			User user = (User) authentication.getPrincipal();
			hasPermission = isSelf(user, targetDomainObject) ? true : isUserOwnedObject(user, targetDomainObject);
		}

		return hasPermission;
	}

	protected boolean isUserOwnedObject(
			User user,
			Object targetDomainObject) {
		return targetDomainObject instanceof UserOwnedObject && ((UserOwnedObject) targetDomainObject).getOwnerId() == user.getUserId();
	}

	protected boolean isSelf(
			User user,
			Object targetDomainObject) {
		return targetDomainObject instanceof User && ((User) targetDomainObject).getUserId() == user.getUserId();
	}

}