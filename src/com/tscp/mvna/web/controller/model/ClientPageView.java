package com.tscp.mvna.web.controller.model;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.tscp.mvna.config.Config;

public class ClientPageView extends PageView {
	private String client = Config.CLIENT;

	/* ************************************
	 * Constructors
	 */

	public ClientPageView() {
		super();
	}

	public ClientPageView(String viewName, Map<String, ?> model) {
		super(viewName, model);
	}

	public ClientPageView(String viewName) {
		super(viewName);
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