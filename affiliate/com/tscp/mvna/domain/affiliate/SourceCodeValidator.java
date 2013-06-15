package com.tscp.mvna.domain.affiliate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SourceCodeValidator implements Validator {

	@Override
	public boolean supports(
			Class<?> myClass) {
		return SourceCode.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "affiliate.source.name.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "affiliate.source.code.required");
	}

}
