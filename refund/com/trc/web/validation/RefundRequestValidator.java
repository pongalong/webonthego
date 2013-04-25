package com.trc.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;


@Component
public class RefundRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> myClass) {
    return RefundRequest.class.isAssignableFrom(myClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RefundRequest refund = (RefundRequest) target;
    checkRefundCode(refund.getRefundCode(), errors);
    checkRefundNote(refund.getNotes(), errors);
  }

  protected void checkRefundCode(RefundCode refundCode, Errors errors) {
    if (refundCode.equals(RefundCode.UNSELECTED)) {
      errors.rejectValue("refundCode", "refund.refundCode.required", "You must choose a refund code");
    }
  }
  
  protected void checkRefundNote(String refundNote, Errors errors) {
	 if (refundNote == null) {
	    errors.rejectValue("refundNote", "refund.refundNote.required", "You must enter the refund note");
	 }
  }  

}
