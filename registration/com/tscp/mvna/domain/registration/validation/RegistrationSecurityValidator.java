package com.tscp.mvna.domain.registration.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.user.SecurityQuestionAnswer;
import com.trc.web.validation.CaptchaValidator;
import com.trc.web.validation.ValidationUtil;
import com.tscp.mvna.domain.registration.RegistrationSecurity;
import com.tscp.mvna.form.Captcha;

@Component
public class RegistrationSecurityValidator implements Validator {
	@Autowired
	private HttpServletRequest request;
	public static final int MAX_ANS_SIZE = 100;
	public static final int MIN_ANS_SIZE = 3;

	@Override
	public boolean supports(
			Class<?> myClass) {
		return RegistrationSecurity.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target, Errors errors) {
		RegistrationSecurity security = (RegistrationSecurity) target;
		checkSecurityQuestion(security.getSecurityQuestionAnswer(), errors);
		checkCaptcha(security.getCaptcha(), errors);
	}

	protected void checkSecurityQuestion(
			SecurityQuestionAnswer securityQuestionAnswer, Errors errors) {
		errors.pushNestedPath("securityQuestionAnswer");
		int questionId = securityQuestionAnswer.getId();
		String questionAnswer = securityQuestionAnswer.getAnswer();
		if (questionId == 0) {
			errors.rejectValue("id", "securityQuestion.required", "You must select a question");
		} else if (ValidationUtil.isEmpty(questionAnswer)) {
			errors.rejectValue("answer", "securityQuestion.answer.required", "You must provide an answer");
		} else if (!ValidationUtil.isBetween(questionAnswer, MIN_ANS_SIZE, MAX_ANS_SIZE)) {
			errors.rejectValue("answer", "securityQuestion.answer.size", "Your answer must be at least 3 characters");
		}
		errors.popNestedPath();
	}

	protected void checkCaptcha(
			Captcha captcha, Errors errors) {
		CaptchaValidator.validate(request, errors);
		if (errors.hasErrors()) {
			captcha.setValue(null);
		}
	}
}