package com.tscp.mvna.web.controller.model;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.tscp.mvna.config.Config;

public class ClientFormView extends FormView {
	private String client = Config.client;

	/* ************************************
	 * Constructors
	 */

	public ClientFormView(String viewName, String validationFailed, Map<String, ?> model) {
		super(viewName, validationFailed, model);
	}

	public ClientFormView(String viewName, String validationFailed) {
		super(viewName, validationFailed);
	}

	/* ************************************
	 * Getters and Setters
	 */

	public void setClient(
			String client) {
		this.client = client;
	}

	@Override
	public void setViewName(
			String viewName) {
		super.setViewName(client + "/" + viewName);
	}

	@Deprecated
	public ModelAndView getSuccess() {
		return this;
	}

}