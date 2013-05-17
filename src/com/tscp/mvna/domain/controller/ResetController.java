package com.tscp.mvna.domain.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.trc.security.encryption.Md5Encoder;
import com.trc.service.email.VelocityEmailService;
import com.trc.user.User;
import com.trc.user.security.UpdatePassword;
import com.trc.user.security.VerifyIdentity;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.EmailValidator;
import com.trc.web.validation.UserUpdateValidator;
import com.tscp.util.logger.DevLogger;

@Controller
@RequestMapping("/reset")
@SessionAttributes({
		"verifyIdentity",
		"securityQuestion",
		"updatePassword",
		"userToReset" })
public class ResetController {
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
		ResultModel model = new ResultModel("account/reset/password/request/prompt");
		model.addAttribute("verifyIdentity", new VerifyIdentity());
		return model.getSuccess();
	}

	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public ModelAndView postRequest(
			HttpSession session, @ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity, Errors errors) {

		ResultModel model = new ResultModel("account/reset/password/request/success", "account/reset/password/request/prompt");

		if (!EmailValidator.checkEmail(verifyIdentity.getEmail())) {
			errors.rejectValue("email", "email.invalid", "Not a valid email address.");
			return model.getError();
		}

		User userToReset = userManager.getUserByEmail(verifyIdentity.getEmail());

		if (userToReset == null) {
			return model.getSuccess();
		} else {
			sendVerificationEmail(session.getId(), userToReset);
			model.addAttribute("userToReset", userToReset);
			return model.getSuccess();
		}
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.GET)
	public ModelAndView showVerification(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset) {

		ResultModel model = new ResultModel("account/reset/password/verify/security");

		if (isValidKey(session, key)) {
			model.addAttribute("securityQuestion", securityQuestionManager.getSecurityQuestion(userToReset.getSecurityQuestionAnswer().getId()));
			return model.getSuccess();
		} else {
			return model.getTimeout();
		}
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.POST)
	public ModelAndView postVerification(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset, @ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity, Errors errors) {

		ResultModel model = new ResultModel("redirect:/reset/password/update/" + key, "account/reset/password/verify/security");

		if (!isValidKey(session, key))
			return model.getTimeout();
		else if (!isCorrectAnswer(userToReset, verifyIdentity)) {
			errors.reject("securtyQuestion.answer.incorrect", "Your response did not match your saved answer");
			return model.getError();
		} else {
			model.addAttribute("updatePassword", new UpdatePassword());
			return model.getSuccess();
		}
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.GET)
	public ModelAndView updatePassword(
			HttpSession session, @ModelAttribute("updatePassword") UpdatePassword updatePassword, @PathVariable("key") String key) {

		ResultModel model = new ResultModel("account/reset/password/update/prompt");

		if (isValidKey(session, key))
			return model.getSuccess();
		else
			return model.getTimeout();
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.POST)
	public ModelAndView postUpdatePassword(
			HttpSession session, @PathVariable("key") String key, @ModelAttribute("userToReset") User userToReset, @ModelAttribute("updatePassword") UpdatePassword updatePassword, BindingResult result) {

		ResultModel model = new ResultModel("account/reset/password/update/success", "account/reset/password/update/prompt");

		if (!isValidKey(session, key))
			return model.getTimeout();

		userUpdateValidator.validateNewPassword(updatePassword, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			userToReset.setPassword(Md5Encoder.encode(updatePassword.getNewPassword()));
			userManager.updateUser(userToReset);
			return model.getSuccess();
		}

	}

	/* ********************************************************************************************************
	 * Helper Methods
	 * ********************************************************************************************************
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
		Map<Object, Object> mailModel = new HashMap<Object, Object>();
		mailModel.put("key", key);
		mailModel.put("user", user);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setFrom("no-reply@webonthego.com");
		message.setSubject("A request to reset your password has been made.");
		DevLogger.log("http://localhost:8080/reset/password/verify/" + key);
		try {
			velocityEmailService.send("passwordReset", message, mailModel);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}