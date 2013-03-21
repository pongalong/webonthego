package com.trc.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;
import com.trc.exception.management.PaymentManagementException;
import com.trc.exception.management.RefundManagementException;
import com.trc.exception.service.RefundServiceException;
import com.trc.service.PaymentService;
import com.trc.service.RefundService;
import com.trc.user.User;

import com.tscp.mvne.PaymentTransaction;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class RefundManager implements RefundManagerModel {
  @Autowired
  private RefundService refundService;  
  @Autowired
  PaymentService paymentService;
        
  @PreAuthorize("isAuthenticated() and hasPermission(#user, 'canRefund')")
  public void refundPayment(User user, RefundRequest refundRequest, int transId) throws RefundManagementException {
	 try{
	    synchronized(refundRequest.getPaymentTransaction()) {
              refundPayment(refundRequest.getPaymentTransaction().getAccountNo(), 
              transId, 
              refundRequest.getPaymentTransaction().getPaymentAmount(), 
              String.valueOf(refundRequest.getPaymentTransaction().getBillingTrackingId()), 
    	      user, 
    	      refundRequest.getRefundCode(), 
    	      refundRequest.getNotes());
	      }	    
	 } catch(Exception e){
		 throw new RefundManagementException("Refund failed due to: " + e.getMessage());
	 }
  }
  
  @Loggable(value = LogLevel.TRACE)
  private void refundPayment(int accountNo, int transId, String amount, String trackingId, User user, RefundCode refundCode, String notes) throws RefundManagementException {
     try {
        refundService.refundPayment(accountNo, transId, amount, Integer.parseInt(trackingId), user.getUsername(), refundCode.getValue(), notes);
     } 
     catch (RefundServiceException e) {
        throw new RefundManagementException(e.getMessage(), e.getCause());
     }
  }    
  
  public PaymentTransaction getPaymentTransaction(int custId, int transId) throws RefundManagementException {
	    try {
	      return refundService.getPaymentTransaction(custId, transId);
	    } catch (RefundServiceException e) {
	    	throw new RefundManagementException(e.getMessage(), e.getCause());
	    }
	  }
}