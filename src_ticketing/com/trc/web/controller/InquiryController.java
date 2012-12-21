package com.trc.web.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.exception.management.TicketManagementException;
import com.trc.manager.TicketManager;
import com.trc.web.model.ResultModel;

@Controller
@RequestMapping("/support/inquire")
public class InquiryController {
	@Autowired
	private TicketManager ticketManager;

	@RequestMapping(method = RequestMethod.GET)
	public String showInqiuryPage() {
		return "support/inquire/overview";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showInquiryForm() {
		ResultModel resultModel = new ResultModel("support/inquire/create");
		resultModel.addObject("ticket", new InquiryTicket());
		resultModel.addObject("categoryList", Arrays.asList(TicketCategory.values()));
		return resultModel.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postInquiryForm(@ModelAttribute InquiryTicket ticket) {
		ResultModel resultModel = new ResultModel("support/inquire/success");
		try {
			if (ticket != null) {
				ticketManager.openTicket(ticket);
				resultModel.addObject("ticket", ticket);
				return resultModel.getSuccess();
			} else {
				return resultModel.getException();
			}
		} catch (TicketManagementException te) {
			return resultModel.getAccessException();
		}
	}

}
