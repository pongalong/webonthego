package com.tscp.mvna.domain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.DeviceManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.tscp.mvna.config.Config;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private DeviceManager deviceManager;

	@RequestMapping(value = {
			"",
			"/",
			"home" }, method = RequestMethod.GET)
	public ModelAndView showHome() {
		User user = userManager.getLoggedInUser();
		return getHomePage(user);
	}

	@RequestMapping(value = "start", method = RequestMethod.GET)
	public ModelAndView showStartPage() {
		return new ClientPageView("start");
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView showLogin() {
		User user = userManager.getLoggedInUser();
		return user.isAuthenticated() ? getHomePage(user) : new ClientPageView("login");
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView logout() {
		return new ClientPageView("j_spring_security_logout").redirect();
	}

	@RequestMapping(value = "timeout", method = RequestMethod.GET)
	public ModelAndView showTimeout() {
		return new ClientPageView().timeout();
	}

	/* ****************************************************************************************************************
	 * Helper Methods
	 * ****************************************************************************************************************
	 */

	private ModelAndView getHomePage(
			User user) {
		return Config.ADMIN && user.isInternalUser() ? getAdminHomePage(user) : getUserHomePage(user);
	}

	private ModelAndView getUserHomePage(
			User user) {
		return user.isAuthenticated() ? new ClientPageView("account").redirect() : new ClientPageView("home");
	}

	private ModelAndView getAdminHomePage(
			User user) {

		Authority authority = user.getGreatestAuthority();

		if (authority.compare(ROLE.ROLE_MANAGER) >= 0) {
			return new ClientPageView("admin/home").redirect();
		} else if (authority.compare(ROLE.ROLE_AGENT) == 0) {
			return new ClientPageView("support/ticket").redirect();
		} else if (authority.compare(ROLE.ROLE_SALES) == 0) {
			return new ClientPageView("sales/home").redirect();
		} else {
			return new ClientPageView("login").redirect();
		}
	}

}