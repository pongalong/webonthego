package com.tscp.mvna.domain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.config.Config;
import com.trc.manager.DeviceManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private DeviceManager deviceManager;

	@RequestMapping(value = { "", "/", "home" }, method = RequestMethod.GET)
	public String showHome() {
		User user = userManager.getLoggedInUser();
		return getHomePage(user);
	}

	@RequestMapping(value = "start", method = RequestMethod.GET)
	public String showStartPage() {
		return "start";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String showLogin() {
		User user = userManager.getLoggedInUser();
		return user.isAuthenticated() ? getHomePage(user) : "login";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		return "redirect:/j_spring_security_logout";
	}

	@RequestMapping(value = "timeout", method = RequestMethod.GET)
	public String showTimeout() {
		return "exception/timeout";
	}

	/* ****************************************************************************************************************
	 * Helper Methods
	 * ****************************************************************************************************************
	 */

	private String getHomePage(
			User user) {
		return Config.ADMIN && user.isInternalUser() ? getAdminHomePage(user) : getUserHomePage(user);
	}

	private String getUserHomePage(
			User user) {
		return user.isAuthenticated() ? "redirect:/account" : "home";
	}

	private String getAdminHomePage(
			User user) {

		Authority authority = user.getGreatestAuthority();

		if (authority.compare(ROLE.ROLE_MANAGER) >= 0) {
			return "redirect:/admin/home";
		} else if (authority.compare(ROLE.ROLE_AGENT) == 0) {
			return "redirect:/support/ticket";
		} else if (authority.compare(ROLE.ROLE_SALES) == 0) {
			return "redirect:/sales/home";
		} else {
			return "redirect:/login";
		}
	}

}