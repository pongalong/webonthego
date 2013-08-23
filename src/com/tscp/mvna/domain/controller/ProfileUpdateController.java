package com.tscp.mvna.domain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.EmailException;
import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.AccountDetailCollection;
import com.trc.user.security.UpdateEmail;
import com.trc.user.security.UpdatePassword;
import com.trc.web.validation.UserUpdateValidator;
import com.tscp.mvna.security.encryption.Md5Encoder;
import com.tscp.mvna.service.email.VelocityEmailService;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/profile/update")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"AccountDetailCollection",
		"updateEmail",
		"updatePassword" })
public class ProfileUpdateController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileUpdateController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private UserUpdateValidator userUpdateValidator;
	@Autowired
	private VelocityEmailService velocityEmailService;

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ModelAndView showUpdateEmail() {
		ClientPageView view = new ClientPageView("account/profile/update/email");
		view.addObject("updateEmail", new UpdateEmail());
		return view;
	}

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public ModelAndView postUpdateEmail(
			HttpSession session, @ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("USER") User user, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @ModelAttribute("updateEmail") UpdateEmail updateEmail, BindingResult result) {

		ClientFormView view = new ClientFormView("profile?notification_sent=" + updateEmail.getNewEmail(), "account/profile/update/email");

		if (controllingUser != null && controllingUser.getUserId() > 0) {
			view.setViewName("profile");
			userUpdateValidator.validateNewEmail(updateEmail, result);
		} else {
			userUpdateValidator.validateEmailChange(updateEmail, result, user);
		}

		if (result.hasErrors())
			return view.validationFailed();

		if (controllingUser != null && controllingUser.getUserId() > 0)
			updateEmail(user, updateEmail, accountDetails);
		else
			sendUpdateEmailVerificationNotice(user, updateEmail, session.getId());

		return view.redirect();
	}

	@RequestMapping(value = "/email/verify/{sessionId}", method = RequestMethod.GET)
	public ModelAndView confirmUpdateEmail(
			HttpSession session, @ModelAttribute("USER") User user, @ModelAttribute("updateEmail") UpdateEmail updateEmail, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable("sessionId") String sessionId) {

		ClientPageView view = new ClientPageView("profile?updated=email");

		if (session.getId().equals(sessionId))
			updateEmail(user, updateEmail, accountDetails);

		return view.redirect();
	}

	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public ModelAndView updatePassword() {
		ClientPageView view = new ClientPageView("account/profile/update/password");
		view.addObject("updatePassword", new UpdatePassword());
		return view;
	}

	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public ModelAndView postUpdatePassword(
			@ModelAttribute("updatePassword") UpdatePassword updatePassword, BindingResult result, @ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("USER") User user) {

		ClientFormView view = new ClientFormView("profile?updated=password", "account/profile/update/password");

		if (controllingUser != null && controllingUser.getUserId() != -1)
			userUpdateValidator.validateNewPassword(updatePassword, result);
		else
			userUpdateValidator.validatePasswordChange(updatePassword, result, user);

		if (result.hasErrors())
			return view.validationFailed();

		user.setPassword(Md5Encoder.encode(updatePassword.getNewPassword()));
		userManager.updateUser(user);
		return view.redirect();
	}

	private void updateEmail(
			User user, UpdateEmail updateEmail, List<AccountDetail> accountDetails) {

		String oldEmail = user.getEmail();

		try {
			user.setUsername(updateEmail.getNewEmail());
			user.setEmail(updateEmail.getNewEmail());

			logger.debug("updating user with new email " + updateEmail.getNewEmail());
			userManager.updateUser(user);

			for (AccountDetail ad : accountDetails) {
				logger.debug("updating account " + ad.getAccount().getAccountNo() + " with new email " + updateEmail.getNewEmail());
				ad.getAccount().setContactEmail(updateEmail.getNewEmail());
				accountManager.updateEmail(ad.getAccount());
			}

			updateEmail.setSuccess(true);
		} catch (AccountManagementException e) {
			user.setEmail(oldEmail);
			userManager.updateUser(user);
			updateEmail.setSuccess(false);
		}
	}

	private void sendUpdateEmailVerificationNotice(
			User user, UpdateEmail updateEmail, String sessionId) {

		try {
			SimpleMailMessage myMessage = new SimpleMailMessage();
			myMessage.setTo(updateEmail.getNewEmail());
			myMessage.setFrom("no-reply@webonthego.com");
			myMessage.setSubject("Verify Your New Email Address");
			Map<String, Object> mailModel = new HashMap<String, Object>();
			mailModel.put("user", user);
			mailModel.put("sessionId", sessionId);
			velocityEmailService.send("profileUpdate", myMessage, mailModel);
			updateEmail.setNotificationSent(true);
		} catch (EmailException e) {
			updateEmail.setNotificationSent(false);
		}
	}

}