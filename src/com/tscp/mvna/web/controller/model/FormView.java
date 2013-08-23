package com.tscp.mvna.web.controller.model;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

public class FormView extends PageView {
	private String validationFailed;

	/* ************************************
	 * Constructors
	 */

	public FormView(String viewName, String validationFailed, Map<String, ?> model) {
		super(viewName, model);
		this.validationFailed = validationFailed;
	}

	public FormView(String viewName, String validationFailed) {
		super(viewName);
		this.validationFailed = validationFailed;
	}

	/* ************************************
	 * Return ModelAndView Methods
	 */

	public ModelAndView validationFailed() {
		setViewName(validationFailed);
		return this;
	}

	public ModelAndView formError() {
		return validationFailed();
	}

}