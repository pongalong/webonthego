package com.trc.user.security;

public class UpdateEmail extends UpdateUser {
	private String newEmail;
	private String confirmNewEmail;
	private boolean notificationSent;
	private boolean success;

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(
			String email) {
		this.newEmail = email;
	}

	public String getConfirmNewEmail() {
		return confirmNewEmail;
	}

	public void setConfirmNewEmail(
			String confirmEmail) {
		this.confirmNewEmail = confirmEmail;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(
			boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(
			boolean success) {
		this.success = success;
	}

	public boolean isEmpty() {
		return newEmail == null;
	}

	public void clear() {
		newEmail = null;
		confirmNewEmail = null;
		notificationSent = false;
		success = false;
	}

}