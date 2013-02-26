package com.tscp.util.logger.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.session.SessionManager;

@Component
public class LoggingHelper {
	@Autowired
	private UserManager userManager;

	private String getUserStamp(
			User user) {
		StringBuilder userStamp = new StringBuilder();
		if (user != null) {
			// userStamp.append(user.getGreatestRole().getRole() + ":");
			userStamp.append(user.getUserId());
			userStamp.append("(").append(user.getUsername()).append(")|");
		}
		return userStamp.toString();
	}

	public String getUserStamp() {
		User user = userManager.getCurrentUser();
		User internalUser = userManager.getLoggedInUser();

		StringBuilder userStamp = new StringBuilder("[").append(SessionManager.getCurrentSession().getId()).append("] ");

		if (internalUser != null && internalUser.getUserId() > 0)
			userStamp.append(getUserStamp(internalUser));

		if (user.isAuthenticated() && user.getUserId() > 0)
			userStamp.append(getUserStamp(user));

		return userStamp.toString();
	}
}