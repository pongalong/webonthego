package com.tscp.mvna.domain.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.EmailException;
import com.trc.manager.SecurityQuestionManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.security.UpdatePassword;
import com.trc.user.security.VerifyIdentity;
import com.trc.web.validation.EmailValidator;
import com.trc.web.validation.UserUpdateValidator;
import com.tscp.mvna.security.encryption.Md5Encoder;
import com.tscp.mvna.service.email.VelocityEmailService;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/reset")
@SessionAttributes({
		"verifyIdentity",
		"securityQuestion",
		"updatePassword",
		"userToReset" })
public class ResetController {
	private static final Logger logger = LoggerFactory.getLogger(ResetController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private SecurityQuestionManager securityQuestionManager;
	@Autowired
	private UserUpdateValidator userUpdateValidator;
	@Autowired
	private VelocityEmailService velocityEmailService;

	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public ModelAndView showRequest() {
		ClientPageView view = new ClientPageView("account/reset/password/request/prompt");
		view.addObject("verifyIdentity", new VerifyIdentity());
		return view;
	}

	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public ModelAndView postRequest(
			HttpSession session, @ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity, Errors errors) {

		ClientFormView view = new ClientFormView("account/reset/password/request/success", "account/reset/password/request/prompt");

		if (!EmailValidator.checkEmail(verifyIdentity.getEmail())) {
			errors.rejectValue("email", "email.invalid", "Not a valid email address.");
			return view.validationFailed();
		}

		User userToReset = userManager.getUserByEmail(verifyIdentity.getEmail());

		if (userToReset == null) {
			return view;
		} else {
			sendVerificationEmail(session.getId(), userToReset);
			view.addObject("userToReset", userToReset);
			return view;
		}
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.GET)
	public ModelAndView showVerification(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset) {

		ClientPageView view = new ClientPageView("account/reset/password/verify/security");

		if (isValidKey(session, key)) {
			view.addObject("securityQuestion", securityQuestionManager.getSecurityQuestion(userToReset.getSecurityQuestionAnswer().getId()));
			return view;
		} else {
			return view.timeout();
		}
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.POST)
	public ModelAndView postVerification(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset, @ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity, Errors errors) {

		ClientFormView view = new ClientFormView("reset/password/update/" + key, "account/reset/password/verify/security");

		if (!isValidKey(session, key))
			return view.timeout();
		else if (!isCorrectAnswer(userToReset, verifyIdentity)) {
			errors.reject("securtyQuestion.answer.incorrect", "Your response did not match your saved answer");
			return view.formError();
		} else {
			view.addObject("updatePassword", new UpdatePassword());
			return view.redirect();
		}
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.GET)
	public ModelAndView updatePassword(
			HttpSession session, @ModelAttribute("updatePassword") UpdatePassword updatePassword, @PathVariable("key") String key) {

		ClientPageView view = new ClientPageView("account/reset/password/update/prompt");

		if (isValidKey(session, key))
			return view;
		else
			return view.timeout();
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.POST)
	public ModelAndView postUpdatePassword(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset, @ModelAttribute("updatePassword") UpdatePassword updatePassword, BindingResult result) {

		ClientFormView view = new ClientFormView("account/reset/password/update/success", "account/reset/password/update/prompt");

		if (!isValidKey(session, key))
			return view.timeout();

		userUpdateValidator.validateNewPassword(updatePassword, result);

		if (result.hasErrors())
			return view.validationFailed();

		userToReset.setPassword(Md5Encoder.encode(updatePassword.getNewPassword()));
		userManager.updateUser(userToReset);
		return view;
	}

	/* **********************************************************************************
	 * Helper Methods
	 */

	private boolean isValidKey(
			HttpSession session, String key) {
		return key.equals(session.getId());
	}

	private boolean isCorrectAnswer(
			User user, VerifyIdentity verifyIdentity) {
		return user.getSecurityQuestionAnswer().getAnswer().equalsIgnoreCase(verifyIdentity.getHintAnswer().trim());
	}

	private void sendVerificationEmail(
			String key, User user) {
		Map<String, Object> mailModel = new HashMap<String, Object>();
		mailModel.put("key", key);
		mailModel.put("user", user);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setFrom("no-reply@webonthego.com");
		message.setSubject("A request to reset your password has been made.");
		logger.debug("Sending verifcation email with url ../reset/password/verify/" + key);
		try {
			velocityEmailService.send("passwordReset", message, mailModel);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}