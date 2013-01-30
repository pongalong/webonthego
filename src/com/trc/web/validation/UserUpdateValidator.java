package com.trc.web.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.security.UpdateEmail;
import com.trc.user.security.UpdatePassword;
import com.trc.user.security.UpdateUser;

@Component
public class UserUpdateValidator implements Validator {
	@Autowired
	private UserManager userManager;

	@Override
	public boolean supports(
			Class<?> myClass) {
		return UpdateUser.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target,
			Errors errors) {

		// this should not be called
	}

	public void validateNewPassword(
			UpdatePassword updatePassword,
			Errors errors) {

		if (!ValidationUtil.matches(updatePassword.getNewPassword(), updatePassword.getConfirmNewPassword())) {
			errors.rejectValue("confirmNewPassword", "password.mismatch", "Your passwords do not match");
			return;
		}

		if (ValidationUtil.isEmpty(updatePassword.getNewPassword())) {
			errors.rejectValue("newPassword", "password.required", "You must enter a password");
		} else if (!ValidationUtil.isBetween(updatePassword.getNewPassword(), UserValidator.MIN_PASS_SIZE, UserValidator.MAX_PASS_SIZE)) {
			errors.rejectValue("newPassword", "password.size", "Your password must be " + UserValidator.MIN_PASS_SIZE + " to " + UserValidator.MAX_PASS_SIZE
					+ " characters");
		} else if (!ValidationUtil.isAlphaNumeric(updatePassword.getNewPassword())) {
			errors.rejectValue("newPassword", "password.invalid", "Your password must be alphanumeric");
		}
	}

	public void validateNewEmail(
			UpdateEmail updateEmail,
			Errors errors) {

		if (!ValidationUtil.matches(updateEmail.getNewEmail(), updateEmail.getConfirmNewEmail())) {
			errors.rejectValue("confirmNewEmail", "email.mismatch", "Your e-mails do not match");
			return;
		}

		if (ValidationUtil.isEmpty(updateEmail.getNewEmail())) {
			errors.rejectValue("newEmail", "email.required", "You must enter an e-mail address");
		} else if (!EmailValidator.checkEmail(updateEmail.getNewEmail())) {
			errors.rejectValue("newEmail", "email.invalid", "E-mail address is invalid");
		} else if (!userManager.isEmailAvailable(updateEmail.getNewEmail())) {
			String[] args = { updateEmail.getNewEmail() };
			errors.rejectValue("newEmail", "email.unavailable", args, "E-mail address is unavailable");
		}
	}

	public void validatePasswordChange(
			UpdatePassword updatePassword,
			Errors errors,
			User user) {

		if (!ValidationUtil.isCorrectPassword(updatePassword.getOldPassword(), user.getPassword())) {
			errors.rejectValue("oldPassword", "password.incorrect", "Incorrect Password");
			return;
		}

		validateNewPassword(updatePassword, errors);
	}

	public void validateEmailChange(
			UpdateEmail updateEmail,
			Errors errors,
			User user) {

		if (!ValidationUtil.isCorrectPassword(updateEmail.getOldPassword(), user.getPassword())) {
			errors.rejectValue("oldPassword", "password.incorrect", "Incorrect Password");
			return;
		}

		validateNewEmail(updateEmail, errors);
	}

}