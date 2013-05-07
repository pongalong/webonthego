package com.trc.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;
import com.trc.exception.management.RefundManagementException;
import com.trc.exception.service.RefundServiceException;
import com.trc.service.PaymentService;
import com.trc.service.RefundService;
import com.trc.user.User;
import com.trc.user.account.PaymentHistory;
import com.trc.web.session.cache.CacheKey;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvne.PaymentTransaction;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class RefundManager implements RefundManagerModel {
	@Autowired
	private RefundService refundService;
	@Autowired
	PaymentService paymentService;
	@Autowired
	private AccountManager accountManager;

	@PreAuthorize("isAuthenticated() and hasPermission(#user, 'canRefund')")
	public void refundPayment(
			User user, RefundRequest refundRequest) throws RefundManagementException {
		try {
			PaymentTransaction pt = refundRequest.getPaymentTransaction();

			synchronized (pt) {
				refundPayment(pt.getAccountNo(), pt.getTransId(), pt.getPaymentAmount(), String.valueOf(pt.getBillingTrackingId()), user, refundRequest.getCode(), refundRequest.getNotes());
			}

			PaymentHistory paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
			CacheManager.set(CacheKey.PAYMENT_HISTORY, paymentHistory); // need to refresh "PAYMENT_HISTORY" in cache
		} catch (Exception e) {
			throw new RefundManagementException("Refund failed due to: " + e.getMessage());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	private void refundPayment(
			int accountNo, int transId, String amount, String trackingId, User user, RefundCode refundCode, String notes) throws RefundManagementException {
		try {
			refundService.refundPayment(accountNo, transId, amount, Integer.parseInt(trackingId), user.getUsername(), refundCode.getValue(), notes);
		} catch (RefundServiceException e) {
			throw new RefundManagementException(e.getMessage(), e.getCause());
		}
	}

	public PaymentTransaction getPaymentTransaction(
			int custId, int transId) throws RefundManagementException {
		try {
			return refundService.getPaymentTransaction(custId, transId);
		} catch (RefundServiceException e) {
			throw new RefundManagementException(e.getMessage(), e.getCause());
		}
	}
}