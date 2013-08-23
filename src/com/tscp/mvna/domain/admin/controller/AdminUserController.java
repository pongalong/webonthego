package com.tscp.mvna.domain.admin.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.UserManager;
import com.trc.user.SecurityQuestionAnswer;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.web.validation.InternalUserValidator;
import com.tscp.mvna.security.encryption.Md5Encoder;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@RequestMapping("/admin/user")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"newInternalUser",
		"requestedRole" })
public class AdminUserController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private InternalUserValidator internalUserValidator;

	/* *********************************************************************************
	 * Create new Users and assign ROLEs
	 */

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createUser(
			@ModelAttribute("CONTROLLING_USER") User internalUser) {

		ClientPageView view = new ClientPageView("admin/users/create/prompt");
		view.addObject("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));
		view.addObject("newInternalUser", new User());
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postCreateUser(
			HttpServletRequest request, @ModelAttribute("availableRoles") ArrayList<ROLE> availableRoles, @ModelAttribute("newInternalUser") User newInternalUser, BindingResult result) {

		ClientFormView view = new ClientFormView("admin/users/create/success", "admin/users/create/prompt");

		String requestedRole = request.getParameter("user_role");

		// TODO set these defaults elsewhere
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

		if (result.hasErrors())
			return view.validationFailed();

		newInternalUser.setPassword(Md5Encoder.encode(newInternalUser.getPassword()));
		userManager.saveUser(newInternalUser);
		return view;
	}

	/* *********************************************************************************
	 * View User groups with different ROLEs
	 */

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView showUsersPrompt(
			@ModelAttribute("CONTROLLING_USER") User internalUser) {

		ClientPageView view = new ClientPageView("admin/users/view/prompt");
		view.addObject("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));
		return view;
	}

	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public ModelAndView showUsersPost(
			HttpServletRequest request) {
		return new ClientPageView("admin/user/view/" + request.getParameter("user_role")).redirect();
	}

	@RequestMapping(value = "/view/{role}", method = RequestMethod.GET)
	public ModelAndView showUsersWithRole(
			@PathVariable("role") ROLE requestedRole) {
		ClientPageView view = new ClientPageView("admin/users/view/success");
		view.addObject("requestedRole", requestedRole);
		view.addObject("members", userManager.getAllUsersWithRole(requestedRole));
		return view;
	}

	/* *********************************************************************************
	 * User manipulation (enable/disable etc)
	 */

	@RequestMapping(value = "/toggle/{userId}", method = RequestMethod.GET)
	public ModelAndView toggleUser(
			ModelMap map, @PathVariable("userId") int userId, @ModelAttribute("requestedRole") ROLE requestedRole) {

		User user = userManager.getUserById(userId);

		if (user.isEnabled())
			userManager.disableUser(user);
		else
			userManager.enableUser(user);

		// removing the attribute to stop it from appearing in the URL in the redirect
		map.remove("requestedRole");

		return new ClientPageView("admin/user/view/" + requestedRole).redirect();
	}

}