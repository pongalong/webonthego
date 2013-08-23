package com.tscp.mvna.domain.admin.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;
import com.trc.exception.management.PaymentManagementException;
import com.trc.exception.management.RefundManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.RefundManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.validation.JCaptchaValidator;
import com.trc.web.validation.RefundRequestValidator;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.PaymentTransaction;

@Controller
@RequestMapping("/admin/refund")
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"AccountDetailCollection",
		"refundRequest" })
public class RefundController {
	private static final Logger logger = LoggerFactory.getLogger(RefundController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private RefundManager refundManager;
	@Autowired
	private RefundRequestValidator refundRequestValidator;
	@Autowired
	private AccountManager accountManager;

	@ModelAttribute
	protected void refundReferenceData(
			ModelMap modelMap) {
		modelMap.addAttribute("refundCodes", Arrays.asList(RefundCode.values()));
	}

	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "{transId}", method = RequestMethod.GET)
	public ModelAndView showRefund(
			@ModelAttribute("USER") User user, @PathVariable int transId) {

		ClientPageView view = new ClientPageView("/admin/payment/refund/prompt");

		try {
			PaymentTransaction paymentTransaction = refundManager.getPaymentTransaction(user.getUserId(), transId);
			RefundRequest refundRequest = new RefundRequest(paymentTransaction);
			view.addObject("refundRequest", refundRequest);
			return view;
		} catch (RefundManagementException e) {
			return view.exception();
		}
	}

	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "{transId}", method = RequestMethod.POST)
	public ModelAndView processRefund(
			HttpServletRequest request, @ModelAttribute("USER") User user, @ModelAttribute("refundRequest") RefundRequest refundRequest, BindingResult result, @PathVariable int transId) {

		ClientFormView view = new ClientFormView("account/payment/history", "admin/payment/refund/prompt");

		JCaptchaValidator.validate(request, result);
		refundRequestValidator.validate(refundRequest, result);

		if (result.hasErrors())
			return view.validationFailed();

		try {
			refundManager.refundPayment(user, refundRequest);
			return view.redirect();
		} catch (RefundManagementException e) {
			logger.debug("Exception while refunding payment", e);
			return view.exception();
		}

	}

	private boolean isRefundable(
			PaymentTransaction paymentTransaction) throws RefundManagementException {

		CreditCard creditCard = null;

		if (paymentTransaction.getTransId() < 0)
			throw new RefundManagementException("Transaction ID is required");
		if (paymentTransaction.getBillingTrackingId() < 0)
			throw new RefundManagementException("Tracking ID is required");

		try {
			paymentManager.getCreditCard(paymentTransaction.getPmtId());
		} catch (PaymentManagementException pe) {
			throw new RefundManagementException("No Credit Card found with payment ID " + paymentTransaction.getPmtId() + ". " + pe.getMessage());
		}

		return creditCard != null;
	}

}
