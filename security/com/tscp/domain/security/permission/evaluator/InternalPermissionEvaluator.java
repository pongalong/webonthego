package com.tscp.domain.security.permission.evaluator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.tscp.domain.security.permission.Permission;
import com.tscp.domain.security.permission.exception.PermissionNotDefinedException;
import com.tscp.util.logger.DevLogger;

public class InternalPermissionEvaluator implements PermissionEvaluator {
	private Map<String, Permission> permissionNameToPermissionMap = new HashMap<String, Permission>();

	protected InternalPermissionEvaluator() {
		// prevent instantiation
	}

	public InternalPermissionEvaluator(Map<String, Permission> permissionNameToPermissionMap) {
		this.permissionNameToPermissionMap = permissionNameToPermissionMap;
	}

	@Override
	public boolean hasPermission(
			Authentication authentication,
			Object targetDomainObject,
			Object permissionName) {

		boolean hasPermission = false;

		if (authentication != null && permissionName instanceof String) {
			Permission p = getPermission((String) permissionName);
			DevLogger.debug("fetched permission " + permissionName);
			hasPermission = p.isAllowed(authentication, targetDomainObject);
		}

		return hasPermission;
	}

	@Override
	public boolean hasPermission(
			Authentication authentication,
			Serializable targetId,
			String targetType,
			Object permissionName) {

		// not yet implemented
		throw new PermissionNotDefinedException("Id and Class permissions are not supported by " + this.getClass().toString());
	}

	private Permission getPermission(
			String permissionKey) {

		Permission permission = permissionNameToPermissionMap.get(permissionKey);

		if (permission == null)
			throw new PermissionNotDefinedException("No permission with key " + permissionKey + " is defined in " + this.getClass().toString());

		return permission;
	}
}
