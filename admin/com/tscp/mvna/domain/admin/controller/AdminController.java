package com.tscp.mvna.domain.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.model.ResultModel;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"newInternalUser",
		"availableRoles" })
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	protected SessionRegistry sessionRegistry;
	@Autowired
	protected UserManager userManager;

	@RequestMapping(value = {
			"",
			"/",
			"home" }, method = RequestMethod.GET)
	public ModelAndView showHome() {
		ResultModel model = new ResultModel("admin/home");

		List<Object> activePrincipals = sessionRegistry.getAllPrincipals();
		List<User> activeUsers = new ArrayList<User>();
		List<List<SessionInformation>> userSessionInfo = new ArrayList<List<SessionInformation>>();

		for (Object principal : activePrincipals) {
			activeUsers.add((User) principal);
			userSessionInfo.add(sessionRegistry.getAllSessions((User) principal, false));
		}

		model.addAttribute("userSessionInfo", userSessionInfo);
		model.addAttribute("activeUsers", activeUsers);
		return model.getSuccess();
	}

}