package com.trc.domain.refund;

import java.io.Serializable;

import com.tscp.mvne.PaymentTransaction;

public class RefundRequest implements Serializable {
	private static final long serialVersionUID = -1239932989891382899L;
	private PaymentTransaction paymentTransaction;
	private RefundCode code;
	private String jcaptcha;
	private String notes;
	private boolean verified;

	public RefundRequest() {
		// do nothing
	}

	public RefundRequest(PaymentTransaction paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}

	public PaymentTransaction getPaymentTransaction() {
		return paymentTransaction;
	}

	public void setPaymentTransaction(
			PaymentTransaction paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}

	public RefundCode getCode() {
		return code;
	}

	public void setCode(
			RefundCode code) {
		this.code = code;
	}

	public String getJcaptcha() {
		return jcaptcha;
	}

	public void setJcaptcha(
			String jcaptcha) {
		this.jcaptcha = jcaptcha;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(
			String notes) {
		this.notes = notes;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(
			boolean verified) {
		this.verified = verified;
	}

}