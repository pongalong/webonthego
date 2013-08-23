package com.tscp.mvna.domain.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.support.knowledgebase.Article;
import com.trc.domain.support.knowledgebase.Category;
import com.trc.exception.management.SupportManagementException;
import com.trc.manager.SupportManager;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/support/faq")
@SessionAttributes({ "categoryList" })
public class FAQController {
	private static final Logger logger = LoggerFactory.getLogger(FAQController.class);
	@Autowired
	private SupportManager supportManager;

	@ModelAttribute
	public void faqReferenceData(
			ModelMap map) {
		try {
			map.addAttribute("categoryList", supportManager.getAllCategories());
		} catch (SupportManagementException e) {
			logger.error("Error fetching categoryList in FAQController", e);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getAllCategories() {
		return "support/faq/faq";
	}

	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	public ModelAndView getArticlesByCategory(
			@PathVariable int categoryId) {
		List<Article> articleList = null;
		Category category = null;
		ClientPageView view = new ClientPageView("support/faq/article");
		try {
			category = supportManager.getCategoryById(categoryId);
			articleList = supportManager.getArticlesByCategory(categoryId);
		} catch (SupportManagementException te) {
			return view.dataFetchException();
		}
		view.addObject("category", category);
		view.addObject("articleList", articleList);
		return view;
	}

	/**
	 * This method is used to retrieve an article by using article id
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
	public ModelAndView getArticleById(
			@PathVariable int articleId) {
		List<Article> articleList = new ArrayList<Article>();
		Article article;
		ClientPageView view = new ClientPageView("support/faq/article");
		try {
			article = supportManager.getArticleById(articleId);
			articleList.add(article);
		} catch (SupportManagementException te) {
			return view.dataFetchException();
		}
		view.addObject("articleList", articleList);
		return view;
	}

	/**
	 * This method is used to insert an article
	 * 
	 */
	@PreAuthorize("hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "/create/article", method = RequestMethod.GET)
	public ModelAndView insertArticle(
			@ModelAttribute("categoryList") List<Category> categoryList) {

		ClientPageView view = new ClientPageView("admin/support/faq/create");
		view.addObject("article", new Article());
		return view;
	}

	/**
	 * This method is used to insert an article to the database
	 * 
	 * @return ModelAndView
	 */
	@PreAuthorize("hasPermission('ROLE_ADMIN','minimumRole')")
	@RequestMapping(value = "/create/article", method = RequestMethod.POST)
	public ModelAndView processInsertArticle(
			@ModelAttribute("article") Article article) {

		ClientPageView view = new ClientPageView("support/faq/faq");

		try {
			int articleId = supportManager.insertArticle(article);
			view.setViewName("support/faq/article/" + articleId);
			return view.redirect();
		} catch (SupportManagementException te) {
			return view.exception();
		}

	}

	// @PreAuthorize("hasPermission('ROLE_ADMIN','minimumRole')")
	// @RequestMapping(value = "/create/category", method = RequestMethod.GET)
	public ModelAndView insertCategory(
			@ModelAttribute("category") Category category) {
		category = new Category();
		return new ClientPageView("/admin/support/faq/createArticle");
	}

	/**
	 * This method is used to create a category in db
	 * 
	 * @return ModelAndView
	 */
	// @PreAuthorize("hasPermission('ROLE_ADMIN','minimumRole')")
	// @RequestMapping(value = "/create/category", method = RequestMethod.POST)
	public ModelAndView processInsertCategory(
			@ModelAttribute("category") Category category, @RequestParam(value = "categoryName", required = true) String categoryName) {

		ClientPageView view = new ClientPageView("support/faq/faq");

		category = new Category();
		category.setTitle(categoryName);

		try {
			int categoryId = supportManager.createCategory(category);
			view.setViewName("support/faq/category/" + categoryId);
			return view.redirect();
		} catch (SupportManagementException e) {
			return view.exception();
		}
	}
}
