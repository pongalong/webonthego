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
import com.trc.security.encryption.Md5Encoder;
import com.trc.user.SecurityQuestionAnswer;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.InternalUserValidator;

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

		ResultModel model = new ResultModel("admin/users/create/prompt");
		model.addAttribute("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));
		model.addAttribute("newInternalUser", new User());
		return model.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postCreateUser(
			HttpServletRequest request, @ModelAttribute("availableRoles") ArrayList<ROLE> availableRoles, @ModelAttribute("newInternalUser") User newInternalUser, BindingResult result) {

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

	/* *********************************************************************************
	 * View User groups with different ROLEs
	 */

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView showUsersPrompt(
			@ModelAttribute("CONTROLLING_USER") User internalUser) {

		ResultModel model = new ResultModel("admin/users/view/prompt");
		model.addAttribute("availableRoles", ROLE.getRolesBelow(internalUser.getGreatestAuthority().getRole(), true));

		return model.getSuccess();
	}

	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public String showUsersPost(
			HttpServletRequest request) {
		return "redirect:/admin/user/view/" + request.getParameter("user_role");
	}

	@RequestMapping(value = "/view/{role}", method = RequestMethod.GET)
	public ModelAndView showUsersWithRole(
			@PathVariable("role") ROLE requestedRole) {

		ResultModel model = new ResultModel("admin/users/view/success");
		model.addAttribute("requestedRole", requestedRole);
		model.addAttribute("members", userManager.getAllUsersWithRole(requestedRole));

		return model.getSuccess();
	}

	/* *********************************************************************************
	 * User manipulation (enable/disable etc)
	 */

	@RequestMapping(value = "/toggle/{userId}", method = RequestMethod.GET)
	public String toggleUser(
			ModelMap map, @PathVariable("userId") int userId, @ModelAttribute("requestedRole") ROLE requestedRole) {

		User user = userManager.getUserById(userId);

		if (user.isEnabled())
			userManager.disableUser(user);
		else
			userManager.enableUser(user);

		// removing the attribute to stop it from appearing in the URL in the redirect
		map.remove("requestedRole");

		return "redirect:/admin/user/view/" + requestedRole;
	}

}