package com.tscp.mvna.domain.registration.manager;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContextHolder;

import com.trc.exception.GatewayException;
import com.trc.manager.UserManager;
import com.trc.service.EmailService;
import com.trc.user.User;
import com.tscp.mvna.domain.affiliate.manager.SourceCodeManager;
import com.tscp.mvna.domain.registration.Registration;
import com.tscp.mvna.domain.registration.affiliate.AffiliateRegistration;
import com.tscp.mvna.security.encryption.Md5Encoder;
import com.tscp.mvna.service.email.VelocityEmailService;

@Component
public class RegistrationManager {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationManager.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private SourceCodeManager sourceCodeManager;
	@Autowired
	private VelocityEmailService velocityEmailService;
	@Autowired
	private EmailService emailService;

	public User createUser(
			Registration registration) {
		User user = new User();
		user.setEmail(registration.getLogin().getEmail().getValue());
		user.setUsername(user.getEmail());
		user.setPassword(Md5Encoder.encode(registration.getLogin().getPassword().getValue()));
		user.setSecurityQuestionAnswer(registration.getSecurity().getSecurityQuestionAnswer());
		user.setDateEnabled(new Date());
		user.setEnabled(true);

		if (registration instanceof AffiliateRegistration) {
			int id = ((AffiliateRegistration) registration).getSourceCode().getId();
			user.setSourceCode(sourceCodeManager.get(id));
			logger.debug("setting created by user to " + userManager.getLoggedInUser().getEmail());
			user.setCreatedBy(userManager.getLoggedInUser());
		}

		userManager.saveUser(user);
		userManager.setCurrentUser(user);

		user.getContactInfo().setFirstName(user.getEmail());

		return user;
	}

	public void autoLogin(
			User user) {
		HttpServletRequest request = (HttpServletRequest) RequestContextHolder.getRequestContext().getExternalContext().getNativeRequest();
		userManager.autoLogin(user, authenticationManager);
	}

	protected void autoLogin(
			User user, HttpServletRequest request, AuthenticationManager authenticationManager) {

		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			logger.debug("auto-login running for user " + user.getEmail());
			userManager.autoLogin(user, request, authenticationManager);
		} else {
			logger.debug("user is already logged in, skipping auto-login");
		}
	}

	public void sendAccountNotice(
			User user) {
		try {
			emailService.sendRegistrationEmail(user);
		} catch (GatewayException e) {
			logger.error("Error sending registration email to user " + user.getEmail());
		}
	}

	// @Deprecated
	// @Loggable(value = LogLevel.TRACE)
	// public void sendActivationEmail(User user) throws EmailException {
	// try {
	// if (user.getUserId() == 0) {
	// user = userManager.getUserByUsername(user.getUsername());
	// }
	// SimpleMailMessage myMessage = new SimpleMailMessage();
	// myMessage.setTo(user.getEmail());
	// myMessage.setFrom("no-reply@webonthego.com");
	// myMessage.setSubject("Your WebOnTheGo Account");
	// Map<Object, Object> mailModel = new HashMap<Object, Object>();
	// mailModel.put("user", user);
	// mailModel.put("code", user.getPassword().substring(0, 8));
	// mailModel.put("password", user.getPassword());
	// velocityEmailService.send("welcome", myMessage, mailModel);
	// } catch (EmailException e) {
	// e.printStackTrace();
	// throw e;
	// }
	// }
}
