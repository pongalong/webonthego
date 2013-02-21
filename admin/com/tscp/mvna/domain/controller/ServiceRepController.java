package com.tscp.mvna.domain.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trc.config.Config;
import com.trc.user.User;
import com.trc.user.authority.ROLE;
import com.trc.web.model.ResultModel;

@Controller
@PreAuthorize("hasAnyRole('ROLE_SERVICEREP','ROLE_MANAGER','ROLE_ADMIN')")
@RequestMapping("/servicerep")
public class ServiceRepController extends MemberController {

	@Override
	public ModelAndView showMembers() {
		return showMembers("Service Representative", userManager.getAllUsersWithRole(ROLE.ROLE_AGENT));
	}

	@Override
	public ModelAndView showHome() {
		ResultModel model = new ResultModel("admin/home");
		if (!Config.ADMIN) {
			return model.getAccessDenied();
		}
		List<Object> activePrincipals = sessionRegistry.getAllPrincipals();
		List<User> activeUsers = new ArrayList<User>();
		List<List<SessionInformation>> userSessionInfo = new ArrayList<List<SessionInformation>>();
		User activeUser;
		for (Object principal : activePrincipals) {
			activeUser = (User) principal;
			activeUsers.add(activeUser);
			userSessionInfo.add(sessionRegistry.getAllSessions(activeUser, false));
		}
		model.addAttribute("userSessionInfo", userSessionInfo);
		model.addAttribute("activeUsers", activeUsers);
		return model.getSuccess();
	}

}
