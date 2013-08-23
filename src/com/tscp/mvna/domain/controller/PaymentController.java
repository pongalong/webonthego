package com.tscp.mvna.domain.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.trc.exception.management.PaymentManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.PaymentHistory;
import com.trc.web.validation.CreditCardValidator;
import com.tscp.mvna.config.Config;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;
import com.tscp.mvna.web.session.security.UrlSafeEncryptor;
import com.tscp.mvne.CreditCard;

@Controller
@RequestMapping("/account/payment")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"PaymentHistory",
		"PaymentMethodCollection",
		"creditCard" })
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
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
			@ModelAttribute("PaymentHistory") PaymentHistory paymentHistory) {
		return showPaymentHistory(paymentHistory, 1);
	}

	@RequestMapping(value = "/history/{page}", method = RequestMethod.GET)
	public ModelAndView showPaymentHistory(
			@ModelAttribute("PaymentHistory") PaymentHistory paymentHistory, @PathVariable("page") int page) {
		paymentHistory.setCurrentPageNum(page);
		ClientPageView view = new ClientPageView("account/payment/history");
		return view;
	}

	@RequestMapping(value = "/methods/edit/{encodedPaymentId}", method = RequestMethod.GET)
	public ModelAndView updatePaymentMethod(
			@ModelAttribute("Encrypter") UrlSafeEncryptor encryptor, @PathVariable("encodedPaymentId") String encodedPaymentId) {

		ClientPageView view = new ClientPageView("account/payment/update/creditcard");

		try {
			int decodedPaymentId = 0;
			try {

				// TODO VERIFIY ENCRYPTOR IS FETCHED
				decodedPaymentId = encryptor.decryptIntUrlSafe(encodedPaymentId);
			} catch (UnsupportedEncodingException e) {
				logger.error("Exception decrypting ID in PaymentController", e);
			} catch (IOException e) {
				logger.error("Exception decrypting ID in PaymentController", e);
			}

			CreditCard cardToUpdate = paymentManager.getCreditCard(decodedPaymentId);
			view.addObject("creditCard", cardToUpdate);
			return view;
		} catch (PaymentManagementException e) {
			return view.dataFetchException();
		}
	}

	@RequestMapping(value = "/methods/edit/{encodedPaymentId}", method = RequestMethod.POST)
	public ModelAndView postUpdatePaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard, BindingResult result) {

		ClientFormView view = new ClientFormView("profile", "account/payment/update/creditcard");

		creditCardValidator.validate(creditCard, result);

		if (result.hasErrors())
			return view.validationFailed();

		try {
			paymentManager.updateCreditCard(user, creditCard);
			return view.redirect();
		} catch (PaymentManagementException e) {
			return view.exception();
		}

	}

	@RequestMapping(value = "/methods/remove/{encodedPaymentId}", method = RequestMethod.GET)
	public ModelAndView deletePaymentMethod(
			@ModelAttribute("Encrypter") UrlSafeEncryptor encryptor, @PathVariable("encodedPaymentId") String encodedPaymentId) {

		ClientPageView view = new ClientPageView("account/payment/remove/creditcard");

		int decodedPaymentId = 0;
		try {
			decodedPaymentId = encryptor.decryptIntUrlSafe(encodedPaymentId);
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception decrypting ID in PaymentController", e);
		} catch (IOException e) {
			logger.error("Exception decrypting ID in PaymentController", e);
		}

		try {
			view.addObject("creditCard", paymentManager.getCreditCard(decodedPaymentId));
			return view;
		} catch (PaymentManagementException e) {
			return view.dataFetchException();
		}
	}

	@RequestMapping(value = "/methods/remove/{encodedPaymentId}", method = RequestMethod.POST)
	public ModelAndView postDeletePaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard) {

		ClientPageView view = new ClientPageView("profile");

		try {
			paymentManager.removeCreditCard(user, creditCard.getPaymentid());
			return view.redirect();
		} catch (PaymentManagementException e) {
			return view.exception();
		}
	}

	@RequestMapping(value = "/methods/add", method = RequestMethod.GET)
	public ModelAndView addPaymentMethod() {
		ClientPageView view = new ClientPageView("account/payment/add/creditcard");
		view.addObject("creditCard", new CreditCard());
		return view;
	}

	@RequestMapping(value = "/methods/add", method = RequestMethod.POST)
	public ModelAndView postAddPaymentMethod(
			@ModelAttribute("USER") User user, @ModelAttribute("creditCard") CreditCard creditCard, BindingResult result) {

		ClientFormView model = new ClientFormView("profile", "account/payment/add/creditcard");

		creditCardValidator.validate(creditCard, result);
		if (result.hasErrors())
			return model.validationFailed();

		try {
			creditCard.setIsDefault(creditCard.getIsDefault() == null ? "N" : creditCard.getIsDefault());
			paymentManager.addCreditCard(user, creditCard);
			return model.redirect();
		} catch (PaymentManagementException e) {
			return model.exception();
		}

	}

}