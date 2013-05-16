package com.tscp.mvna.domain.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.config.Config;
import com.trc.manager.UserManager;
import com.trc.user.EmptyUser;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.cache.CacheManager;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@RequestMapping("/admin/search")
@SessionAttributes({ "USER" })
public class SearchController {
	@Autowired
	protected UserManager userManager;
	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView showUser(
			@ModelAttribute("USER") User user, @RequestParam String admin_search_id) {

		ResultModel model = new ResultModel("redirect:/account");

		if (!Config.ADMIN)
			return model.getAccessDenied();

		if (!admin_search_id.equals("0")) {
			user = userManager.getUserById(Integer.parseInt(admin_search_id));
		} else {
			user = new EmptyUser();
			model.setSuccessViewName("redirect:/");
		}

		model.addAttribute(SessionKey.USER, user);
		cacheManager.refreshCache(user);
		return model.getSuccess();
	}

	/**
	 * Uses getJson to perform ajax searches. This is much more efficient but currently unused.
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "getjson/email/ajax", method = RequestMethod.GET)
	public @ResponseBody
	SearchResponse searchByEmailAjax(
			@RequestParam String email) {

		List<User> searchResults = userManager.searchCustomersByEmail(email);
		UserSearchResult[] users = new UserSearchResult[searchResults.size()];

		for (int i = 0; i < searchResults.size(); i++)
			users[i] = new UserSearchResult(searchResults.get(i));

		return new SearchResponse(searchResults.size() > 0, users);
	}

	/**
	 * Returns a list of UserSearchResults for the getJson method to perform ajax fetches.
	 * 
	 * @author Jonathan
	 * 
	 */
	public static class SearchResponse {
		private UserSearchResult[] users;
		private boolean success;

		public UserSearchResult[] getUsers() {
			return users;
		}

		public boolean isSuccess() {
			return success;
		}

		public SearchResponse(boolean success, UserSearchResult[] users) {
			this.success = success;
			this.users = users;
		}
	}

	/**
	 * Return object representing a User in the getJson method to perform ajax fetches.
	 * 
	 * @author Jonathan
	 * 
	 */
	public static class UserSearchResult {
		private int id;
		private String username;

		public UserSearchResult(User user) {
			this.id = user.getUserId();
			this.username = user.getUsername();
		}

		public int getId() {
			return id;
		}

		public void setId(
				int id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(
				String username) {
			this.username = username;
		}

	}
}
