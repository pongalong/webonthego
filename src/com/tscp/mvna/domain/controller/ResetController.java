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
import com.trc.user.security.SecurityQuestion;
import com.trc.user.security.UpdatePassword;
import com.trc.user.security.VerifyIdentity;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.EmailValidator;
import com.trc.web.validation.UserUpdateValidator;

@Controller
@RequestMapping("/reset")
@SessionAttributes({ "verifyIdentity" })
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
			HttpSession session,
			@ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity,
			Errors errors) {

		ResultModel model = new ResultModel("account/reset/password/request/success", "account/reset/password/request/prompt");

		if (!EmailValidator.checkEmail(verifyIdentity.getEmail())) {
			errors.rejectValue("email", "email.invalid", "Not a valid email address.");
			return model.getError();
		}

		storeEmail(session, verifyIdentity.getEmail());
		User user = userManager.getUserByEmail(verifyIdentity.getEmail());

		if (user == null) {
			// errors.reject("email.notExist", "User &quot;" +
			// verifyIdentity.getEmail() + "&quot; not found");
			// return model.getError();
			return model.getSuccess();
		} else {
			Map<Object, Object> mailModel = new HashMap<Object, Object>();
			mailModel.put("key", session.getId());
			mailModel.put("user", user);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(verifyIdentity.getEmail());
			message.setFrom("no-reply@webonthego.com");
			message.setSubject("A request to reset your password has been made.");

			try {
				velocityEmailService.send("passwordReset", message, mailModel);
			} catch (EmailException e) {
				e.printStackTrace();
			}

		}
		return model.getSuccess();
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.GET)
	public ModelAndView showVerification(
			HttpSession session,
			@ModelAttribute("verifyIdentity") VerifyIdentity verifyIdentity,
			@PathVariable("key") String key) {

		ResultModel model = new ResultModel("account/reset/password/verify/security");

		if (isValidKey(session, key)) {
			String email = retrieveEmail(session);
			User user = userManager.getUserByEmail(email);
			if (user != null) {
				SecurityQuestion hint = securityQuestionManager.getSecurityQuestion(user.getSecurityQuestionAnswer().getId());
				model.addAttribute("question", hint.getQuestion());
			}
			return model.getSuccess();
		} else {
			return model.getTimeout();
		}
	}

	@RequestMapping(value = "/password/verify/{key}", method = RequestMethod.POST)
	public ModelAndView postVerification(
			HttpSession session,
			@PathVariable("key") String key,
			@ModelAttribute VerifyIdentity verifyIdentity,
			Errors errors) {

		ResultModel model = new ResultModel("redirect:/reset/password/update/" + key, "account/reset/password/verify/security");
		if (isValidKey(session, key)) {
			String email = (String) session.getAttribute(getEmailKey(session));
			User user = userManager.getUserByEmail(email);
			if (!isCorrectAnswer(user, verifyIdentity)) {
				errors.reject("securtyQuestion.answer.incorrect", "Your response did not match your saved answer");
				SecurityQuestion hint = securityQuestionManager.getSecurityQuestion(user.getSecurityQuestionAnswer().getId());
				model.addAttribute("question", hint.getQuestion());
				return model.getError();
			} else {
				return model.getSuccess();
			}
		} else {
			return model.getTimeout();
		}
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.GET)
	public ModelAndView updatePassword(
			HttpSession session,
			@ModelAttribute("updatePassword") UpdatePassword updatePassword,
			@PathVariable("key") String key) {

		ResultModel model = new ResultModel("account/reset/password/update/prompt");

		if (isValidKey(session, key))
			return model.getSuccess();
		else
			return model.getTimeout();
	}

	@RequestMapping(value = "/password/update/{key}", method = RequestMethod.POST)
	public ModelAndView postUpdatePassword(
			HttpSession session,
			@PathVariable("key") String key,
			@ModelAttribute("updatePassword") UpdatePassword updatePassword,
			BindingResult result) {

		ResultModel model = new ResultModel("account/reset/password/update/success", "account/reset/password/update/prompt");

		if (isValidKey(session, key)) {
			String email = retrieveEmail(session);

			userUpdateValidator.validateNewPassword(updatePassword, result);

			if (result.hasErrors()) {
				return model.getError();
			} else {
				User user = userManager.getUserByEmail(email);
				user.setPassword(Md5Encoder.encode(updatePassword.getNewPassword()));
				userManager.updateUser(user);
				return model.getSuccess();
			}

		} else {
			return model.getTimeout();
		}
	}

	/* ********************************************************************************************************
	 * Helper Methods
	 * ********************************************************************************************************
	 */

	private boolean isValidKey(
			HttpSession session,
			String key) {
		return key.equals(session.getId());
	}

	private boolean isCorrectAnswer(
			User user,
			VerifyIdentity verifyIdentity) {
		return user.getSecurityQuestionAnswer().getAnswer().equalsIgnoreCase(verifyIdentity.getHintAnswer().trim());
	}

	private String getEmailKey(
			HttpSession session) {
		return "resetEmail" + session.getId();
	}

	private void storeEmail(
			HttpSession session,
			String email) {
		session.setAttribute(getEmailKey(session), email);
	}

	private String retrieveEmail(
			HttpSession session) {
		return (String) session.getAttribute(getEmailKey(session));
	}

}