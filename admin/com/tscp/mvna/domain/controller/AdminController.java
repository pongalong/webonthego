package com.tscp.mvna.domain.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.config.Config;
import com.trc.manager.UserManager;
import com.trc.security.encryption.Md5Encoder;
import com.trc.user.SecurityQuestionAnswer;
import com.trc.user.User;
import com.trc.user.admin.UserControl;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.InternalUserValidator;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@SessionAttributes({ "USER", "CONTROLLING_USER", "newInternalUser", "availableRoles" })
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	protected UserManager userManager;
	@Autowired
	protected SessionRegistry sessionRegistry;
	@Autowired
	private InternalUserValidator internalUserValidator;

	@RequestMapping(value = { "", "/", "home" }, method = RequestMethod.GET)
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

	@RequestMapping(value = "/logout/{userId}", method = RequestMethod.GET)
	public String forceLogout(
			@PathVariable("userId") int userId) {

		userManager.forceLogout(sessionRegistry, userId);

		return "redirect:/admin";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createUser(
			@ModelAttribute("CONTROLLING_USER") User internalUser) {

		ResultModel model = new ResultModel("admin/users/create/prompt");
		model.addAttribute("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));
		model.addAttribute("newInternalUser", new User());
		return model.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postCreateUser(
			HttpServletRequest request,
			@ModelAttribute("availableRoles") ArrayList<ROLE> availableRoles,
			@ModelAttribute("newInternalUser") User newInternalUser,
			BindingResult result) {

		ResultModel model = new ResultModel("admin/users/create/success", "admin/users/create/prompt");

		String requestedRole = request.getParameter("user_role");

		SecurityQuestionAnswer securityQuestionAnswer = new SecurityQuestionAnswer();
		securityQuestionAnswer.setId(1);
		securityQuestionAnswer.setAnswer("Telscape");
		newInternalUser.setSecurityQuestionAnswer(securityQuestionAnswer);

		if (newInternalUser.getUsername() == null || newInternalUser.getUsername().trim().isEmpty())
			newInternalUser.setUsername(newInternalUser.getEmail());

		newInternalUser.setEnabled(true);
		newInternalUser.setDateEnabled(new Date());
		newInternalUser.getRoles().add(new Authority(newInternalUser, ROLE.valueOf(requestedRole)));
		internalUserValidator.validate(newInternalUser, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			newInternalUser.setPassword(Md5Encoder.encode(newInternalUser.getPassword()));
			userManager.saveUser(newInternalUser);
			return model.getSuccess();
		}
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView showUsers(
			@ModelAttribute("CONTROLLING_USER") User internalUser) {

		ResultModel model = new ResultModel("admin/users/view/prompt");
		model.addAttribute("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));
		return model.getSuccess();
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ModelAndView postShowUsers(
			HttpServletRequest request) {

		ResultModel model = new ResultModel("admin/users/view/success", "admin/users/view/prompt");
		String requestedRole = request.getParameter("user_role");

		model.addAttribute("members", userManager.getAllUsersWithRole(ROLE.valueOf(requestedRole)));
		return model.getSuccess();
	}

	@RequestMapping(value = "/toggle/{userId}", method = RequestMethod.GET)
	public String toggleManager(
			@PathVariable("userId") int userId,
			@RequestParam("cmd") UserControl cmd) {

		if (Config.ADMIN) {
			User user = userManager.getUserById(userId);
			switch (cmd) {
				case ENABLE:
					userManager.enableUser(user);
					break;
				case DISABLE:
					userManager.disableUser(user);
					break;
				default:
					break;
			}
			return "redirect:/home";
		} else {
			return "exception/accessDenied";
		}
	}

}