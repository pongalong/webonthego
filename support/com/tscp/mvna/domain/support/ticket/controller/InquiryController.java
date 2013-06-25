package com.tscp.mvna.domain.support.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tscp.mvna.domain.support.ticket.InquiryTicket;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;
import com.tscp.mvna.domain.support.ticket.manager.TicketManager;
import com.tscp.mvna.domain.support.ticket.validation.TicketValidator;
import com.tscp.mvna.web.controller.model.ResultModel;

@Controller
@RequestMapping("/support/inquire")
public class InquiryController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private TicketValidator ticketValidator;

	@ModelAttribute
	public void ticketReferenceData(
			ModelMap map) {
		map.addAttribute("ticketCategories", ticketManager.getRootTicketCategories());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showInquiryForm(
			@ModelAttribute("ticket") InquiryTicket ticket) {
		return "support/inquire/create";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView postInquiryForm(
			@ModelAttribute("ticket") InquiryTicket ticket, BindingResult result) {

		ResultModel model = new ResultModel("support/inquire/success", "support/inquire/create");

		ticketValidator.validate(ticket, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {
				ticketManager.openTicket(ticket);
				return model.getSuccess();
			} catch (TicketManagementException te) {
				return model.getAccessException();
			}
		}
	}

}