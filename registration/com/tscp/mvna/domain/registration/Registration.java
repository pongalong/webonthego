package com.tscp.mvna.domain.registration;

import java.io.Serializable;

public class Registration implements Serializable {
	private static final long serialVersionUID = -204153305944601962L;
	private RegistrationLogin login = new RegistrationLogin();
	private RegistrationSecurity security = new RegistrationSecurity();
	private RegistrationTerms terms = new RegistrationTerms();

	public RegistrationLogin getLogin() {
		return login;
	}

	public void setLogin(RegistrationLogin login) {
		this.login = login;
	}

	public RegistrationSecurity getSecurity() {
		return security;
	}

	public void setSecurity(RegistrationSecurity security) {
		this.security = security;
	}

	public RegistrationTerms getTerms() {
		return terms;
	}

	public void setTerms(RegistrationTerms terms) {
		this.terms = terms;
	}

}
