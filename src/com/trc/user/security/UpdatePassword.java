package com.trc.user.security;

public class UpdatePassword extends UpdateUser {
	private String newPassword;
	private String confirmNewPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(
			String password) {
		this.newPassword = password;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(
			String confirmPassword) {
		this.confirmNewPassword = confirmPassword;
	}

	public boolean isEmpty() {
		return newPassword == null;
	}

	public void clear() {
		newPassword = null;
		confirmNewPassword = null;
	}

}