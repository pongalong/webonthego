package com.tscp.mvna.domain.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.management.PaymentManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.UserManager;
import com.trc.service.email.VelocityEmailService;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.session.cache.CacheKey;
import com.trc.web.session.cache.CacheManager;
import com.trc.web.validation.UserUpdateValidator;
import com.tscp.mvne.CreditCard;
import com.tscp.util.logger.DevLogger;

@Controller
@RequestMapping("/profile")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER" })
public class ProfileController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private UserUpdateValidator userUpdateValidator;
	@Autowired
	private VelocityEmailService velocityEmailService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showProfile(
			@ModelAttribute("USER") User user) {

		ResultModel model = new ResultModel("account/profile/profile");

		List<CreditCard> paymentMethods;
		List<String> encodedPaymentIds = new ArrayList<String>();
		try {
			paymentMethods = paymentManager.getCreditCards(user);
			for (CreditCard creditCard : paymentMethods) {
				try {
					encodedPaymentIds.add(CacheManager.getEncryptor().encryptIntUrlSafe(creditCard.getPaymentid()));
				} catch (UnsupportedEncodingException e) {
					DevLogger.error("Exception encoding IDs in PaymentController", e);
				}
			}
			model.addAttribute("encodedPaymentIds", encodedPaymentIds);
			model.addAttribute(CacheKey.PAYMENT_METHODS, paymentMethods);
		} catch (PaymentManagementException e) {
			e.printStackTrace();
		}

		return model.getSuccess();
	}

	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_MANAGER','isAtleast')")
	@RequestMapping(value = "/user/disable", method = RequestMethod.GET)
	public String disableUser(
			@ModelAttribute("USER") User user) {
		userManager.disableUser(user);
		return "redirect:/profile";
	}

	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_MANAGER','isAtleast')")
	@RequestMapping(value = "/user/enable", method = RequestMethod.GET)
	public String enableUser(
			@ModelAttribute("USER") User user) {
		userManager.enableUser(user);
		return "redirect:/profile";
	}

}