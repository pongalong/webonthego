package com.tscp.mvna.domain.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.config.Config;
import com.trc.exception.management.PaymentManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.PaymentHistory;
import com.trc.web.model.ResultModel;
import com.trc.web.session.cache.CacheKey;
import com.trc.web.session.cache.CacheManager;
import com.trc.web.validation.CreditCardValidator;
import com.tscp.mvne.CreditCard;
import com.tscp.util.logger.DevLogger;

@Controller
@RequestMapping("/account/payment")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"PAYMENT_HISTORY",
		"PAYMENT_METHODS",
		"creditCard" })
public class PaymentController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private CreditCardValidator creditCardValidator;

	@ModelAttribute
	private void paymentReferenceData(
			ModelMap modelMap) {
		modelMap.addAttribute("states", Config.states.entrySet());
		modelMap.addAttribute("months", Config.months.entrySet());
		modelMap.addAttribute("years", Config.yearsFuture.entrySet());
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ModelAndView showPaymentHistory(
			@ModelAttribute("PAYMENT_HISTORY") PaymentHistory paymentHistory) {
		return showPaymentHistory(paymentHistory, 1);
	}

	@RequestMapping(value = "/history/{page}", method = RequestMethod.GET)
	public ModelAndView showPaymentHistory(
			@ModelAttribute("PAYMENT_HISTORY") PaymentHistory paymentHistory, @PathVariable("page") int page) {

		ResultModel model = new ResultModel("account/payment/history");

		paymentHistory.setCurrentPageNum(page);
		return model.getSuccess();
	}

	@Deprecated
	// @RequestMapping(value = "/methods", method = RequestMethod.GET)
	public ModelAndView showPaymentMethods() {
		DevLogger.debug("showPaymentMethods");
		User user = userManager.getCurrentUser();
		ResultModel model = new ResultModel("payment/methods");
		try {
			List<CreditCard> paymentMethods = paymentManager.getCreditCards(user);
			List<String> encodedPaymentIds = new ArrayList<String>();
			for (CreditCard creditCard : paymentMethods) {
				try {
					encodedPaymentIds.add(CacheManager.getEncryptor().encryptIntUrlSafe(creditCard.getPaymentid()));
				} catch (UnsupportedEncodingException e) {
					DevLogger.error("Exception encoding IDs in PaymentController", e);
				}
			}
			model.addAttribute("encodedPaymentIds", encodedPaymentIds);
			model.addAttribute(CacheKey.PAYMENT_METHODS, paymentMethods);
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/methods/edit/{encodedPaymentId}", method = RequestMethod.GET)
	public ModelAndView updatePaymentMethod(
			@PathVariable("encodedPaymentId") String encodedPaymentId) {

		ResultModel model = new ResultModel("account/payment/update/creditcard");

		try {
			int decodedPaymentId = 0;
			try {
				decodedPaymentId = CacheManager.getEncryptor().decryptIntUrlSafe(encodedPaymentId);
			} catch (UnsupportedEncodingException e) {
				DevLogger.error("Exception decrypting ID in PaymentController", e);
			} catch (IOException e) {
				DevLogger.error("Exception decrypting ID in PaymentController", e);
			}

			CreditCard cardToUpdate = paymentManager.getCreditCard(decodedPaymentId);
			model.addAttribute("creditCard", cardToUpdate);
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/methods/edit/{encodedPaymentId}", method = RequestMethod.POST)
	public ModelAndView postUpdatePaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard, BindingResult result) {

		ResultModel model = new ResultModel("redirect:/profile", "account/payment/update/creditcard");

		creditCardValidator.validate(creditCard, result);
		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {
				paymentManager.updateCreditCard(user, creditCard);
				return model.getSuccess();
			} catch (PaymentManagementException e) {
				return model.getException();
			}
		}
	}

	@RequestMapping(value = "/methods/remove/{encodedPaymentId}", method = RequestMethod.GET)
	public ModelAndView deletePaymentMethod(
			@PathVariable("encodedPaymentId") String encodedPaymentId) {

		ResultModel model = new ResultModel("account/payment/remove/creditcard");

		int decodedPaymentId = 0;
		try {
			decodedPaymentId = CacheManager.getEncryptor().decryptIntUrlSafe(encodedPaymentId);
		} catch (UnsupportedEncodingException e) {
			DevLogger.error("Exception decrypting ID in PaymentController", e);
		} catch (IOException e) {
			DevLogger.error("Exception decrypting ID in PaymentController", e);
		}

		try {
			CreditCard creditCard = paymentManager.getCreditCard(decodedPaymentId);
			model.addAttribute("creditCard", creditCard);
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/methods/remove/{encodedPaymentId}", method = RequestMethod.POST)
	public ModelAndView postDeletePaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard) {

		ResultModel model = new ResultModel("redirect:/profile");

		try {
			paymentManager.removeCreditCard(user, creditCard.getPaymentid());
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getException();
		}
	}

	@RequestMapping(value = "/methods/add", method = RequestMethod.GET)
	public ModelAndView addPaymentMethod() {

		ResultModel model = new ResultModel("account/payment/add/creditcard");

		model.addAttribute("creditCard", new CreditCard());
		return model.getSuccess();
	}

	@RequestMapping(value = "/methods/add", method = RequestMethod.POST)
	public ModelAndView postAddPaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard, BindingResult result) {

		ResultModel model = new ResultModel("redirect:/profile", "account/payment/add/creditcard");

		creditCardValidator.validate(creditCard, result);
		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {
				creditCard.setIsDefault(creditCard.getIsDefault() == null ? "N" : creditCard.getIsDefault());
				paymentManager.addCreditCard(user, creditCard);
				return model.getSuccess();
			} catch (PaymentManagementException e) {
				return model.getException();
			}
		}
	}

}