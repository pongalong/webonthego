package com.tscp.mvna.domain.registration.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tscp.mvna.domain.registration.RegistrationTerms;

@Component
public class RegistrationTermsValidator implements Validator {

	@Override
	public boolean supports(
			Class<?> myClass) {
		return RegistrationTerms.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target, Errors errors) {
		RegistrationTerms terms = (RegistrationTerms) target;
		if (!terms.isAcceptTerms()) {
			errors.rejectValue("acceptTerms", "terms.required", "You must accept the terms and conditions");
		}
	}

}
