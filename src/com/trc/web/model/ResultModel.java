package com.trc.web.model;

import org.springframework.web.servlet.ModelAndView;

public class ResultModel {
	private ModelAndView model = new ModelAndView();
	private String successViewName;
	private String errorViewName;
	private String accessDeniedViewName = "exception/accessDenied";
	private String accessExceptionViewName = "exception/dataAccessException";
	private String exceptionViewName = "exception/uncaughtException";
	private String timeoutViewName = "exception/timeout";

	public ResultModel(String successView, String errorView) {
		setSuccessViewName(successView);
		setErrorViewName(errorView);
		model.setViewName(errorView);
	}

	public ResultModel(String view) {
		setSuccessViewName(view);
		setErrorViewName(view);
		model.setViewName(view);
	}

	ModelAndView getModel() {
		return model;
	}

	void setModel(
			ModelAndView model) {
		this.model = model;
	}

	public ModelAndView addAttribute(
			String attributeName,
			Object attributeValue) {
		return getModel().addObject(attributeName, attributeValue);
	}

	public ModelAndView addAttribute(
			Enum attributeName,
			Object attributeValue) {
		return getModel().addObject(attributeName.toString(), attributeValue);
	}

	public ModelAndView getSuccess() {
		model.setViewName(successViewName);
		return model;
	}

	public ModelAndView getError() {
		model.setViewName(errorViewName);
		return model;
	}

	public ModelAndView getException() {
		model.setViewName(exceptionViewName);
		return model;
	}

	public ModelAndView getAccessException() {
		model.setViewName(accessExceptionViewName);
		return model;
	}

	public ModelAndView getTimeout() {
		model.setViewName(timeoutViewName);
		return model;
	}

	public ModelAndView getAccessDenied() {
		model.setViewName(accessDeniedViewName);
		return model;
	}

	/* ****************************************************************************************************
	 * Getters/Setters
	 * ****************************************************************************************************
	 */

	public String getSuccessViewName() {
		return successViewName;
	}

	public void setSuccessViewName(
			String successViewName) {
		this.successViewName = successViewName;
	}

	public String getErrorViewName() {
		return errorViewName;
	}

	public void setErrorViewName(
			String errorViewName) {
		this.errorViewName = errorViewName;
	}

	public String getAccessExceptionViewName() {
		return accessExceptionViewName;
	}

	public void setAccessExceptionViewName(
			String accessExceptionViewName) {
		this.accessExceptionViewName = accessExceptionViewName;
	}

	public String getAccessDeniedViewName() {
		return accessDeniedViewName;
	}

	public void setAccessDeniedViewName(
			String accessDeniedViewName) {
		this.accessDeniedViewName = accessDeniedViewName;
	}

	public String getExceptionViewName() {
		return exceptionViewName;
	}

	public void setExceptionViewName(
			String exceptionViewName) {
		this.exceptionViewName = exceptionViewName;
	}

	public String getTimeoutViewName() {
		return timeoutViewName;
	}

	public void setTimeoutViewName(
			String timeoutViewName) {
		this.timeoutViewName = timeoutViewName;
	}

}
