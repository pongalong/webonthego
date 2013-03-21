package com.trc.manager;

import com.trc.domain.refund.RefundRequest;
import com.trc.exception.management.RefundManagementException;
import com.trc.user.User;

public interface RefundManagerModel {
	 //public void refundPayment(int accountNo, int transId, String amount, String trackingId, User user, RefundCode refundCode, String notes) throws RefundManagementException;

	 public void refundPayment(User user, RefundRequest paymentRefund, int transId) throws RefundManagementException;
 
}
