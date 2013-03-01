package com.trc.user.authority;

public enum ROLE {
	ROLE_SU, ROLE_ADMIN, ROLE_MANAGER, ROLE_AGENT, ROLE_SALES, ROLE_USER, ROLE_ANONYMOUS;

	public static ROLE[] getInternalRoles() {
		return new ROLE[] { ROLE_SU, ROLE_ADMIN, ROLE_MANAGER, ROLE_AGENT, ROLE_SALES };
	}

	public static String[] getInternalRolesAsString() {
		return new String[] { ROLE_SU.toString(), ROLE_ADMIN.toString(), ROLE_MANAGER.toString(), ROLE_AGENT.toString(), ROLE_SALES.toString() };
	}

	public static ROLE[] getExternalRoles() {
		return new ROLE[] { ROLE_USER, ROLE_ANONYMOUS };
	}
}
