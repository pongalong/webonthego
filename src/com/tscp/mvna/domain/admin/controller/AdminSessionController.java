package com.tscp.mvna.domain.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.trc.manager.UserManager;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"newInternalUser",
		"availableRoles" })
@RequestMapping("/admin/session")
public class AdminSessionController {
	@Autowired
	protected SessionRegistry sessionRegistry;
	@Autowired
	private UserManager userManager;

	@RequestMapping(value = "/logout/{userId}", method = RequestMethod.GET)
	public String forceLogout(
			@PathVariable("userId") int userId) {

		userManager.forceLogout(sessionRegistry, userId);

		return "redirect:/admin";
	}
}
