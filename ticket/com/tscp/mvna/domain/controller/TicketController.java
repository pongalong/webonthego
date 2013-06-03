package com.tscp.mvna.domain.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.exception.EmailException;
import com.trc.exception.management.TicketManagementException;
import com.trc.manager.TicketManager;
import com.trc.manager.UserManager;
import com.trc.service.email.VelocityEmailService;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.TicketValidator;

@Controller
@RequestMapping("/support/ticket")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"INTERNAL_USERS",
		"ticket",
		"ticketNote",
		"ticketCategories",
		"contactEmail" })
public class TicketController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private TicketValidator ticketValidator;
	@Autowired
	private UserManager userManager;
	@Autowired
	private VelocityEmailService velocityEmailService;

	@ModelAttribute
	public void ticketReferenceData(
			ModelMap map) {
		map.addAttribute("ticketCategories", ticketManager.getRootTicketCategories());
		map.addAttribute("priorityList", Arrays.asList(TicketPriority.values()));
		map.addAttribute("statusList", Arrays.asList(TicketStatus.values()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home() {
		ResultModel model = new ResultModel("support/ticket/home");
		return model.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showTicketForm(
			@ModelAttribute("USER") User user, @ModelAttribute("CONTROLLING_USER") User controllingUser) {

		ResultModel model = new ResultModel("support/ticket/create/create", "support/ticket/exception/error_select_user");

		if (user.getUserId() == 0)
			return model.getError();

		Ticket ticket = ticketManager.buildMyTicketType(controllingUser);

		if (ticket instanceof AdminTicket) {
			((AdminTicket) ticket).setCustomerId(user.getUserId());
			((AdminTicket) ticket).setCreatorId(controllingUser.getUserId());
		} else if (ticket instanceof AgentTicket) {
			((AgentTicket) ticket).setCustomerId(user.getUserId());
			((AgentTicket) ticket).setCreatorId(controllingUser.getUserId());
		}

		model.addAttribute("ticket", ticket);
		return model.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postTicketForm(
			@ModelAttribute("ticket") Ticket ticket, BindingResult result) {

		ResultModel model = new ResultModel("support/ticket/create/success", "support/ticket/create/create");

		ticketValidator.validate(ticket, result);

		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {
				int ticketId = ticketManager.openTicket(ticket);
				ticket.setId(ticketId);
				return model.getSuccess();
			} catch (TicketManagementException e) {
				return model.getException();
			}
		}
	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showUpdateTicket(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @PathVariable int ticketId) {

		ResultModel model = new ResultModel("support/ticket/update");

		List<User> internalUsers = userManager.getAllInternalUsers();
		model.addAttribute("INTERNAL_USERS", internalUsers);

		Ticket ticket;

		// fetch the customer's email
		try {
			ticket = ticketManager.getTicketById(ticketId);

			// customer ticket
			if (ticket instanceof CustomerTicket) {
				CustomerTicket cTicket = (CustomerTicket) ticket;
				if (cTicket.getCustomerId() != controllingUser.getUserId())
					model.addAttribute("customer", userManager.getUserById(cTicket.getCustomerId()));
				model.addAttribute("assignee", userManager.getUserById(cTicket.getAssigneeId()));
				model.addAttribute("ticket", cTicket);
			}
			// agent ticket
			if (ticket instanceof AgentTicket) {
				AgentTicket aTicket = (AgentTicket) ticket;
				if (aTicket.getCreatorId() == controllingUser.getUserId())
					model.addAttribute("creator", controllingUser);
				else
					model.addAttribute("creator", userManager.getUserById(aTicket.getCreatorId()));
				model.addAttribute("ticket", aTicket);
			}
			// inquiry ticket
			if (ticket instanceof InquiryTicket) {
				InquiryTicket iTicket = (InquiryTicket) ticket;
				model.addAttribute("ticket", iTicket);
			}

			model.addAttribute("ticket", ticket);
			return model.getSuccess();
		} catch (TicketManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postUpdateTicket(
			@ModelAttribute("ticket") Ticket ticket, BindingResult result) {

		ResultModel model = new ResultModel("redirect:/support/ticket/view/" + ticket.getId(), "support/ticket/update");

		ticketValidator.validate(ticket, result);

		if (result.hasErrors())
			return model.getError();
		else
			try {
				ticketManager.updateTicket(ticket);
				return model.getSuccess();
			} catch (TicketManagementException e) {
				return model.getException();
			}
	}

	@RequestMapping(value = "/reply/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showReplyTicket(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("ticket") Ticket ticket, @PathVariable int ticketId) {

		ResultModel model = new ResultModel("support/ticket/reply/reply");

		String email = "unknown";

		// customer ticket
		if (ticket instanceof CustomerTicket) {
			CustomerTicket cTicket = (CustomerTicket) ticket;
			User customer = userManager.getUserById(cTicket.getCustomerId());
			model.addAttribute("customer", customer);
			model.addAttribute("assignee", userManager.getUserById(cTicket.getAssigneeId()));
			email = customer.getEmail();
		} else if (ticket instanceof InquiryTicket) {
			email = ((InquiryTicket) ticket).getContactEmail();
		}

		TicketNote note = new TicketNote();
		note.setTicket(ticket);
		note.setCreator(controllingUser);

		model.addAttribute("contactEmail", email);
		model.addAttribute("ticketNote", note);

		return model.getSuccess();
	}

	@RequestMapping(value = "/reply/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postReplyTicket(
			@ModelAttribute("contactEmail") String contactEmail, @ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("ticketNote") TicketNote note, BindingResult result, @PathVariable int ticketId) {

		ResultModel model = new ResultModel("redirect:/support/ticket/view/" + ticketId, "support/ticket/reply/reply");

		if (result.hasErrors()) {
			return model.getError();
		} else {
			try {

				Map<Object, Object> mailModel = new HashMap<Object, Object>();
				mailModel.put("ticketNote", note);
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(contactEmail);
				message.setFrom("no-reply@webonthego.com");
				message.setSubject("A response to your ticket #" + note.getTicket().getId());

				try {
					velocityEmailService.send("ticketReply", message, mailModel);
				} catch (EmailException e) {
					e.printStackTrace();
				}

				ticketManager.saveNote(note);
				return model.getSuccess();
			} catch (TicketManagementException e) {
				return model.getException();
			}
		}
	}
}
