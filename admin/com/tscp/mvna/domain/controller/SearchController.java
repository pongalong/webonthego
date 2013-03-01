package com.tscp.mvna.domain.controller;

import java.util.List;
import java.util.Vector;

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
@PreAuthorize("hasAnyRole('ROLE_AGENT', 'ROLE_MANAGER','ROLE_ADMIN', 'ROLE_SU')")
@RequestMapping("/search")
@SessionAttributes({ "USER" })
public class SearchController {
	@Autowired
	protected UserManager userManager;
	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView search(
			@RequestParam String param) {

		ResultModel model = new ResultModel("admin/search/jquery_username");

		if (!Config.ADMIN)
			return model.getAccessDenied();

		List<User> searchResults = new Vector<User>();
		try {
			int numericSearch = Integer.parseInt(param);
			searchResults.add(userManager.getUserById(numericSearch));
			searchResults.add(userManager.searchByAccountNo(numericSearch));
		} catch (NumberFormatException e) {
			// searchResults.addAll(userManager.searchByEmail(param));
			searchResults.addAll(userManager.searchCustomersByEmail(param));
		}

		model.addAttribute("searchResults", searchResults);
		return model.getSuccess();
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView showUser(
			@ModelAttribute("USER") User user,
			@RequestParam String admin_search_id) {

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

		if (!Config.ADMIN)
			return new SearchResponse(false, new UserSearchResult[0]);

		List<User> searchResults = userManager.searchCustomersByEmail(email);

		UserSearchResult[] users = new UserSearchResult[searchResults.size()];

		for (int i = 0; i < searchResults.size(); i++)
			users[i] = new UserSearchResult(searchResults.get(i));

		return new SearchResponse(searchResults.size() > 0, users);
	}

	/**
	 * Return class for the getJson method to perform ajax searches.
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
