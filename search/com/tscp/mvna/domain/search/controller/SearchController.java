package com.tscp.mvna.domain.search.controller;

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

import com.trc.manager.UserManager;
import com.trc.user.EmptyUser;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvna.domain.affiliate.SourceCode;
import com.tscp.mvna.domain.affiliate.manager.SourceCodeManager;
import com.tscp.mvna.domain.search.SearchResponse;
import com.tscp.mvna.domain.search.SearchResult;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@RequestMapping("/search")
@SessionAttributes({ "USER" })
public class SearchController {
	@Autowired
	protected UserManager userManager;
	@Autowired
	protected SourceCodeManager sourceCodeManager;
	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	protected ModelAndView switchToUser(
			@ModelAttribute("USER") User user, @RequestParam("admin-search-param-id") String userId) {

		user = userId.equals("0") ? new EmptyUser() : userManager.getUserById(Integer.parseInt(userId));
		ResultModel model = user.getUserId() < 0 ? new ResultModel("redirect:/") : new ResultModel("redirect:/account");

		model.addAttribute(SessionKey.USER, user);
		cacheManager.refreshCache(user);
		return model.getSuccess();
	}

	/**
	 * Search for users
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "getjson/user/email/ajax", method = RequestMethod.GET)
	public @ResponseBody
	SearchResponse searchByEmailAjax(
			@RequestParam("param") String email) {

		List<User> searchResults = userManager.searchCustomersByEmail(email);
		SearchResult[] users = new SearchResult[searchResults.size()];

		String label;

		for (int i = 0; i < searchResults.size(); i++) {
			label = searchResults.get(i).isEnabled() ? searchResults.get(i).getEmail() : searchResults.get(i).getEmail() + " (disabled)";
			users[i] = new SearchResult(searchResults.get(i).getUserId(), label, searchResults.get(i).getUsername());
		}

		return new SearchResponse(searchResults.size() > 0, users);
	}

	/**
	 * Searches for source codes
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "getjson/sourcecode/code/ajax", method = RequestMethod.GET)
	public @ResponseBody
	SearchResponse searchSourceCode(
			@RequestParam("param") String code) {

		List<SourceCode> searchResults = sourceCodeManager.searchByCode(code);
		SearchResult[] codes = new SearchResult[searchResults.size()];

		for (int i = 0; i < searchResults.size(); i++)
			codes[i] = new SearchResult(searchResults.get(i).getId(), searchResults.get(i).getCode(), searchResults.get(i).getName());

		return new SearchResponse(searchResults.size() > 0, codes);
	}

}