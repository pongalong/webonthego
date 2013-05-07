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

	private ROLE(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

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

	public static ROLE[] getInternalRoles() {
		return new ROLE[] {
				ROLE_SU,
				ROLE_ADMIN,
				ROLE_MANAGER,
				ROLE_AGENT,
				ROLE_SALES };
	}

	public static ROLE[] getExternalRoles() {
		return new ROLE[] {
				ROLE_USER,
				ROLE_ANONYMOUS };
	}

	/* *********************************************************************************************
	 * Helper methods. Mostly for HQL.
	 * *********************************************************************************************
	 */

	public static String[] getInternalRolesAsString() {
		return new String[] {
				ROLE_SU.toString(),
				ROLE_ADMIN.toString(),
				ROLE_MANAGER.toString(),
				ROLE_AGENT.toString(),
				ROLE_SALES.toString() };
	}

}