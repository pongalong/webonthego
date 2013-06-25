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
import com.tscp.mvna.web.controller.model.ResultModel;

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
		ResultModel model = new ResultModel("admin/affiliate/home");
		model.addAttribute("sourceCodeList", sourceCodeManager.getAll());
		return model.getSuccess();
	}

	@RequestMapping(value = "/source/add", method = RequestMethod.GET)
	public ModelAndView addSourceCode() {
		ResultModel model = new ResultModel("admin/affiliate/source/add/prompt");
		model.addAttribute("sourceCode", new SourceCode());
		model.addAttribute("sourceCodeList", sourceCodeManager.getAll());
		return model.getSuccess();
	}

	@RequestMapping(value = "/source/add", method = RequestMethod.POST)
	public ModelAndView addSourceCodePost(
			@ModelAttribute("sourceCode") SourceCode sourceCode, BindingResult result) {
		ResultModel model = new ResultModel("admin/affiliate/source/add/success", "admin/affiliate/source/add/prompt");

		sourceCodeValidator.validate(sourceCode, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			return sourceCodeManager.save(sourceCode) > 0 ? model.getSuccess() : model.getException();
		}
	}

	@RequestMapping(value = "/source/update/{id}", method = RequestMethod.GET)
	public ModelAndView editSourceCode(
			@PathVariable("id") int id) {
		ResultModel model = new ResultModel("admin/affiliate/source/update/prompt");
		model.addAttribute("sourceCode", sourceCodeManager.get(id));
		model.addAttribute("sourceCodeList", sourceCodeManager.getAll());
		return model.getSuccess();
	}

	@RequestMapping(value = "/source/update/{id}", method = RequestMethod.POST)
	public ModelAndView editSourceCodePost(
			@PathVariable("id") int id, @ModelAttribute("sourceCode") SourceCode sourceCode, BindingResult result) {
		ResultModel model = new ResultModel("admin/affiliate/source/update/success", "admin/affiliate/source/update/prompt");

		sourceCodeValidator.validate(sourceCode, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {
				sourceCodeManager.update(sourceCode);
				return model.getSuccess();
			} catch (Exception e) {
				return model.getException();
			}
		}

	}

}