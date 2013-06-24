package com.tscp.mvna.domain.registration;

import java.io.Serializable;

import com.tscp.mvna.form.ConfirmableFormField;

public class RegistrationLogin implements Serializable {
	private static final long serialVersionUID = -4211564087975547505L;
	private ConfirmableFormField email = new ConfirmableFormField();
	private ConfirmableFormField password = new ConfirmableFormField();

	public ConfirmableFormField getEmail() {
		return email;
	}

	public void setEmail(
			ConfirmableFormField email) {
		this.email = email;
	}

	public ConfirmableFormField getPassword() {
		return password;
	}

	public void setPassword(
			ConfirmableFormField password) {
		this.password = password;
	}

}