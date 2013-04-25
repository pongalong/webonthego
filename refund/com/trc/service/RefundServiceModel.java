package com.trc.service;

import com.trc.exception.service.RefundServiceException;
import com.tscp.mvne.PaymentTransaction;

public interface RefundServiceModel {

	//public List<KenanPayment> getPayments(Account account) throws RefundServiceException;
	
	public void refundPayment(int accountNo, int transId, String amount, int trackingId, String refundBy, int refundCode, String notes) throws RefundServiceException;

	public PaymentTransaction getPaymentTransaction(int custId, int transId) throws RefundServiceException;

}