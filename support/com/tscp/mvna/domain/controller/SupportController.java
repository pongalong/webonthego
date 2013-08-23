package com.tscp.mvna.domain.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.support.knowledgebase.Article;
import com.trc.exception.management.SupportManagementException;
import com.trc.manager.SupportManager;
import com.trc.manager.UserManager;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/support")
public class SupportController {
	@Autowired
	private SupportManager supportManager;
	@Autowired
	private UserManager userManager;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView support() {
		return new ClientPageView("support/support");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView supportSearchPost(
			@RequestParam(value = "keyword", required = true) String keyword) {

		ClientPageView view = new ClientPageView("support/faq/article");

		List<Article> articleList = new ArrayList<Article>();
		if (keyword != null && !keyword.equals("")) {
			try {
				articleList = supportManager.searchArticlesByKeyword(keyword);
			} catch (SupportManagementException te) {
				return view.dataFetchException();
			}
			view.addObject("articleList", articleList);
			return view;
		} else {
			throw new IllegalArgumentException("No keyword specified.");
		}
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView download() {
		return new ClientPageView("support/download/download");
	}

}