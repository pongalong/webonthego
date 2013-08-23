package com.tscp.mvna.domain.affiliate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.tscp.mvna.domain.affiliate.SourceCode;
import com.tscp.mvna.domain.affiliate.SourceCodeValidator;
import com.tscp.mvna.domain.affiliate.manager.SourceCodeManager;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@RequestMapping("/admin/affiliate")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"sourceCode",
		"sourceCodeList" })
public class AffiliateController {
	@Autowired
	private SourceCodeManager sourceCodeManager;
	@Autowired
	private SourceCodeValidator sourceCodeValidator;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home() {
		ClientPageView view = new ClientPageView("admin/affiliate/home");
		view.addObject("sourceCodeList", sourceCodeManager.getAll());
		return view;
	}

	@RequestMapping(value = "/source/add", method = RequestMethod.GET)
	public ModelAndView addSourceCode() {
		ClientPageView view = new ClientPageView("admin/affiliate/source/add/prompt");
		view.addObject("sourceCode", new SourceCode());
		view.addObject("sourceCodeList", sourceCodeManager.getAll());
		return view;
	}

	@RequestMapping(value = "/source/add", method = RequestMethod.POST)
	public ModelAndView addSourceCodePost(
			@ModelAttribute("sourceCode") SourceCode sourceCode, BindingResult result) {
		ClientFormView view = new ClientFormView("admin/affiliate/source/add/success", "admin/affiliate/source/add/prompt");

		sourceCodeValidator.validate(sourceCode, result);

		if (result.hasErrors())
			return view.validationFailed();

		return sourceCodeManager.save(sourceCode) > 0 ? view : view.exception();
	}

	@RequestMapping(value = "/source/update/{id}", method = RequestMethod.GET)
	public ModelAndView editSourceCode(
			@PathVariable("id") int id) {
		ClientPageView view = new ClientPageView("admin/affiliate/source/update/prompt");
		view.addObject("sourceCode", sourceCodeManager.get(id));
		view.addObject("sourceCodeList", sourceCodeManager.getAll());
		return view;
	}

	@RequestMapping(value = "/source/update/{id}", method = RequestMethod.POST)
	public ModelAndView editSourceCodePost(
			@PathVariable("id") int id, @ModelAttribute("sourceCode") SourceCode sourceCode, BindingResult result) {
		ClientFormView view = new ClientFormView("admin/affiliate/source/update/success", "admin/affiliate/source/update/prompt");

		sourceCodeValidator.validate(sourceCode, result);

		if (result.hasErrors())
			return view.validationFailed();

		try {
			sourceCodeManager.update(sourceCode);
			return view;
		} catch (Exception e) {
			return view.exception();
		}

	}

}