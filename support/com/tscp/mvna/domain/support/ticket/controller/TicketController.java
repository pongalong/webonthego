package com.tscp.mvna.domain.support.ticket.controller;

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

import com.trc.exception.EmailException;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.tscp.mvna.domain.support.ticket.AdminTicket;
import com.tscp.mvna.domain.support.ticket.AgentTicket;
import com.tscp.mvna.domain.support.ticket.CustomerTicket;
import com.tscp.mvna.domain.support.ticket.InquiryTicket;
import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketNote;
import com.tscp.mvna.domain.support.ticket.TicketPriority;
import com.tscp.mvna.domain.support.ticket.TicketStatus;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;
import com.tscp.mvna.domain.support.ticket.manager.TicketManager;
import com.tscp.mvna.domain.support.ticket.validation.TicketValidator;
import com.tscp.mvna.service.email.VelocityEmailService;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

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
		return new ClientPageView("support/ticket/home");
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showTicketForm(
			@ModelAttribute("USER") User user, @ModelAttribute("CONTROLLING_USER") User controllingUser) {

		ClientFormView view = new ClientFormView("support/ticket/create/create", "support/ticket/exception/error_select_user");

		if (user.getUserId() == 0)
			return view.validationFailed();

		Ticket ticket = ticketManager.buildMyTicketType(controllingUser);

		if (ticket instanceof AdminTicket) {
			((AdminTicket) ticket).setCustomerId(user.getUserId());
			((AdminTicket) ticket).setCreatorId(controllingUser.getUserId());
		} else if (ticket instanceof AgentTicket) {
			((AgentTicket) ticket).setCustomerId(user.getUserId());
			((AgentTicket) ticket).setCreatorId(controllingUser.getUserId());
		}

		view.addObject("ticket", ticket);
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postTicketForm(
			@ModelAttribute("ticket") Ticket ticket, BindingResult result) {

		ClientFormView view = new ClientFormView("support/ticket/create/success", "support/ticket/create/create");

		ticketValidator.validate(ticket, result);

		if (result.hasErrors())
			return view.validationFailed();

		try {
			int ticketId = ticketManager.openTicket(ticket);
			ticket.setId(ticketId);
			return view;
		} catch (TicketManagementException e) {
			return view.exception();
		}

	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showUpdateTicket(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @PathVariable int ticketId) {

		ClientPageView view = new ClientPageView("support/ticket/update");

		List<User> internalUsers = userManager.getAllInternalUsers();
		view.addObject("INTERNAL_USERS", internalUsers);

		Ticket ticket;

		// fetch the customer's email
		try {
			ticket = ticketManager.getTicketById(ticketId);

			// customer ticket
			if (ticket instanceof CustomerTicket) {
				CustomerTicket cTicket = (CustomerTicket) ticket;
				if (cTicket.getCustomerId() != controllingUser.getUserId())
					view.addObject("customer", userManager.getUserById(cTicket.getCustomerId()));
				view.addObject("assignee", userManager.getUserById(cTicket.getAssigneeId()));
				view.addObject("ticket", cTicket);
			}
			// agent ticket
			if (ticket instanceof AgentTicket) {
				AgentTicket aTicket = (AgentTicket) ticket;
				if (aTicket.getCreatorId() == controllingUser.getUserId())
					view.addObject("creator", controllingUser);
				else
					view.addObject("creator", userManager.getUserById(aTicket.getCreatorId()));
				view.addObject("ticket", aTicket);
			}
			// inquiry ticket
			if (ticket instanceof InquiryTicket) {
				InquiryTicket iTicket = (InquiryTicket) ticket;
				view.addObject("ticket", iTicket);
			}

			view.addObject("ticket", ticket);
			return view;
		} catch (TicketManagementException e) {
			return view.dataFetchException();
		}
	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postUpdateTicket(
			@ModelAttribute("ticket") Ticket ticket, BindingResult result) {

		ClientFormView model = new ClientFormView("support/ticket/view/" + ticket.getId(), "support/ticket/update");

		ticketValidator.validate(ticket, result);

		if (result.hasErrors())
			return model.validationFailed();

		try {
			ticketManager.updateTicket(ticket);
			return model.redirect();
		} catch (TicketManagementException e) {
			return model.exception();
		}
	}

	@RequestMapping(value = "/reply/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showReplyTicket(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("ticket") Ticket ticket, @PathVariable int ticketId) {

		ClientPageView view = new ClientPageView("support/ticket/reply/reply");

		String email = "unknown";

		// customer ticket
		if (ticket instanceof CustomerTicket) {
			CustomerTicket cTicket = (CustomerTicket) ticket;
			User customer = userManager.getUserById(cTicket.getCustomerId());
			view.addObject("customer", customer);
			view.addObject("assignee", userManager.getUserById(cTicket.getAssigneeId()));
			email = customer.getEmail();
		} else if (ticket instanceof InquiryTicket) {
			email = ((InquiryTicket) ticket).getContactEmail();
		}

		TicketNote note = new TicketNote();
		note.setTicket(ticket);
		note.setCreator(controllingUser);

		view.addObject("contactEmail", email);
		view.addObject("ticketNote", note);

		return view;
	}

	@RequestMapping(value = "/reply/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postReplyTicket(
			@ModelAttribute("contactEmail") String contactEmail, @ModelAttribute("CONTROLLING_USER") User controllingUser, @ModelAttribute("ticketNote") TicketNote note, BindingResult result, @PathVariable int ticketId) {

		ClientFormView view = new ClientFormView("support/ticket/view/" + ticketId, "support/ticket/reply/reply");

		if (result.hasErrors())
			return view.validationFailed();

		try {
			Map<String, Object> mailModel = new HashMap<String, Object>();
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
			return view.redirect();
		} catch (TicketManagementException e) {
			return view.exception();
		}

	}
}
