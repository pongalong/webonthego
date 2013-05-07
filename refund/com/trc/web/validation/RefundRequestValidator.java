package com.trc.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;

@Component
public class RefundRequestValidator implements Validator {

	@Override
	public boolean supports(
			Class<?> myClass) {
		return RefundRequest.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target, Errors errors) {
		RefundRequest refundRequest = (RefundRequest) target;
		checkCode(refundRequest.getCode(), errors);
		checkNotes(refundRequest.getNotes(), errors);
		checkInfoVerified(refundRequest.isVerified(), errors);
	}

	protected void checkCode(
			RefundCode refundCode, Errors errors) {
		if (refundCode.equals(RefundCode.UNSELECTED))
			errors.rejectValue("code", "refund.code.required", "You must choose a refund code");
	}

	protected void checkNotes(
			String refundNote, Errors errors) {
		if (refundNote == null || refundNote.trim().isEmpty())
			errors.rejectValue("notes", "refund.notes.required", "You must enter the refund note");
	}

	protected void checkInfoVerified(
			Boolean verified, Errors errors) {
		if (!verified)
			errors.rejectValue("verified", "refund.verified.required", "You must verify the user's information");
	}

}