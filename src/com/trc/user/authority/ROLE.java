package com.trc.user.authority;

import java.util.ArrayList;
import java.util.List;

public enum ROLE {
	ROLE_SU("Superuser"),
	ROLE_ADMIN("Administrator"),
	ROLE_MANAGER("Manager"),
	ROLE_ACCOUNTING_MANAGER("Accounting Manager"),
	ROLE_ACCOUNTING("Accounting"),
	ROLE_AGENT("Service Agent"),
	ROLE_SALES("Sales Agent"),
	ROLE_USER("User"),
	ROLE_ANONYMOUS("Anonymous");

	private String name;
	private static ROLE[] internalRoles = {
			ROLE_SU,
			ROLE_ADMIN,
			ROLE_MANAGER,
			ROLE_ACCOUNTING_MANAGER,
			ROLE_ACCOUNTING,
			ROLE_AGENT,
			ROLE_SALES };
	private static ROLE[] externalRoles = {
			ROLE_USER,
			ROLE_ANONYMOUS };

	private ROLE(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ROLE[] getInternalRoles() {
		return internalRoles;
	}

	public static ROLE[] getExternalRoles() {
		return externalRoles;
	}

	/* *********************************************************************************************
	 * Ranking and Ordering Methods
	 * *********************************************************************************************
	 */

	public static List<ROLE> getRolesAbove(
			ROLE role, boolean eq) {
		List<ROLE> greaterRoles = new ArrayList<ROLE>();
		for (ROLE r : ROLE.values())
			if (r.ordinal() <= role.ordinal()) {
				if (eq)
					greaterRoles.add(r);
				else if (r.ordinal() < role.ordinal())
					greaterRoles.add(r);
			}

		return greaterRoles;
	}

	public static List<ROLE> getRolesBelow(
			ROLE role, boolean eq) {
		List<ROLE> lesserRoles = new ArrayList<ROLE>();
		for (ROLE r : ROLE.values()) {
			if (r.ordinal() >= role.ordinal())
				if (eq)
					lesserRoles.add(r);
				else if (r.ordinal() > role.ordinal())
					lesserRoles.add(r);
		}
		return lesserRoles;
	}

	/* *********************************************************************************************
	 * Helper methods. Mostly for HQL.
	 * *********************************************************************************************
	 */

	public static String[] getInternalRolesAsString() {
		String[] internalRolesAsString = new String[internalRoles.length];
		for (int i = 0; i < internalRoles.length; i++)
			internalRolesAsString[i] = internalRoles[i].toString();
		return internalRolesAsString;
	}

}