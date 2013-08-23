package com.tscp.mvna.web.controller.model;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public abstract class PageView extends ModelAndView {
	protected static final Logger logger = LoggerFactory.getLogger(PageView.class);
	private static String accessDenied = "exception/accessDenied";
	private static String dataFetchException = "exception/dataException";
	private static String uncaughtException = "exception/uncaughtException";
	private static String timeout = "exception/timeout";
	private boolean redirect;

	/* ************************************
	 * Constructors
	 */

	public PageView() {
		super();
	}

	public PageView(String viewName, Map<String, ?> model) {
		super(viewName, model);
	}

	public PageView(String viewName) {
		super(viewName);
	}

	public void setRedirect(
			boolean redirect) {
		this.redirect = redirect;
	}

	@Override
	public void setViewName(
			String viewName) {
		super.setViewName((redirect ? "redirect:/" : "") + viewName);
	}

	/* ************************************
	 * Return ModelAndView Methods
	 */

	public ModelAndView redirect() {
		redirect = true;
		setViewName(getViewName());
		return this;
	}

	public ModelAndView accessDenied() {
		setViewName(accessDenied);
		return this;
	}

	public ModelAndView dataFetchException() {
		setViewName(dataFetchException);
		return this;
	}

	public ModelAndView exception() {
		setViewName(uncaughtException);
		return this;
	}

	public ModelAndView timeout() {
		setViewName(timeout);
		return this;
	}

}