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
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
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
@SessionAttributes({ "USER", "CONTROLLING_USER", "ticket", "ticketNote", "contactEmail" })
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
		map.addAttribute("categoryList", Arrays.asList(TicketCategory.values()));
		map.addAttribute("priorityList", Arrays.asList(TicketPriority.values()));
		map.addAttribute("statusList", Arrays.asList(TicketStatus.values()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showTicketOverviewAndSearch() {
		ResultModel model = new ResultModel("support/ticket/overview");

		AdminTicket ticket = new AdminTicket();
		ticket.setStatus(TicketStatus.NONE);
		ticket.setPriority(TicketPriority.NONE);
		ticket.setCategory(TicketCategory.NONE);

		model.addAttribute("ticket", ticket);
		return model.getSuccess();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView postTicketOverviewAndSearch(
			@ModelAttribute("USER") User user,
			@ModelAttribute("ticket") AdminTicket ticket) {

		ResultModel model = new ResultModel("support/ticket/tickets", "support/ticket/overview");

		try {
			List<Ticket> tickets = ticketManager.searchTickets(user.getUserId(), ticket.getCreatorId(), ticket.getAssigneeId(), ticket.getStatus(),
					ticket.getCategory(), ticket.getPriority(), TicketType.NONE, ticket.getTitle(), ticket.getDescription());

			String ticketSearchContextString = "";
			if (ticket.getCreatorId() != 0)
				ticketSearchContextString += " Created by " + ticket.getCreatorId();
			if (ticket.getAssigneeId() != 0)
				ticketSearchContextString += " Assigned to " + ticket.getAssigneeId();
			if (ticket.getStatus() != TicketStatus.NONE)
				ticketSearchContextString += " with Status " + ticket.getStatus().getDescription();
			if (ticket.getCategory() != TicketCategory.NONE)
				ticketSearchContextString += " in Category " + ticket.getCategory().getDescription();
			if (ticket.getPriority() != TicketPriority.NONE)
				ticketSearchContextString += " with Priority " + ticket.getPriority().getDescription();
			if (ticket.getTitle() != null && !ticket.getTitle().trim().isEmpty())
				ticketSearchContextString += " with Title like " + ticket.getTitle();
			if (ticket.getDescription() != null && !ticket.getDescription().trim().isEmpty())
				ticketSearchContextString += " with Description like " + ticket.getDescription();

			model.addAttribute("ticketList", tickets);
			model.addAttribute("ticketSearchContextString", ticketSearchContextString);
			return model.getSuccess();
		} catch (TicketManagementException e) {
			return model.getAccessException();
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showTicketForm(
			@ModelAttribute("USER") User user,
			@ModelAttribute("CONTROLLING_USER") User controllingUser) {

		ResultModel model = new ResultModel("support/ticket/create/create", "support/ticket/exception/error_select_user");

		if (user.getUserId() == 0)
			return model.getError();

		model.addAttribute("ticket", ticketManager.buildMyTicketType(controllingUser));
		return model.getSuccess();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postTicketForm(
			@ModelAttribute("ticket") Ticket ticket,
			BindingResult result) {

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
			@ModelAttribute("CONTROLLING_USER") User controllingUser,
			@PathVariable int ticketId) {

		ResultModel model = new ResultModel("support/ticket/update");

		List<User> internalUsers = userManager.getAllInternalUsers();
		model.addAttribute("internalUsers", internalUsers);

		Ticket ticket;
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
			@ModelAttribute("ticket") Ticket ticket,
			BindingResult result) {

		ResultModel model = new ResultModel("redirect:/support/ticket/view/ticket/" + ticket.getId(), "support/ticket/update");

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

	@RequestMapping(value = "/view/ticket/{ticketId}", method = RequestMethod.GET)
	public ModelAndView viewTicketById(
			@ModelAttribute("CONTROLLING_USER") User controllingUser,
			@PathVariable int ticketId) {

		ResultModel model = new ResultModel("support/ticket/ticket");

		try {
			Ticket ticket = ticketManager.getTicketById(ticketId);

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

			return model.getSuccess();

		} catch (TicketManagementException e) {
			return model.getAccessException();
		}

	}

	@RequestMapping(value = "/view/inquiry", method = RequestMethod.GET)
	public ModelAndView viewInquiries() {

		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");

		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, 0, 0, null, null, null, TicketType.INQUIRY, null, null);
			resultModel.addAttribute("ticketList", tickets);
			resultModel.addAttribute("ticketSearchContextString", "Inquiries");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/view/creator/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsCreated(
			@ModelAttribute("CONTROLLING_USER") User controllingUser) {

		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");

		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, controllingUser.getUserId(), 0, TicketStatus.NONE, null, null, TicketType.NONE, null, null);
			resultModel.addAttribute("ticketList", tickets);
			resultModel.addAttribute("ticketSearchContextString", "Created By Me");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/view/assignee/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsAssigned(
			@ModelAttribute("CONTROLLING_USER") User controllingUser) {

		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");

		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, 0, controllingUser.getUserId(), TicketStatus.NONE, null, null, TicketType.NONE, null, null);
			resultModel.addAttribute("ticketList", tickets);
			resultModel.addAttribute("ticketSearchContextString", "Assigned to Me");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/reply/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showReplyTicket(
			@ModelAttribute("CONTROLLING_USER") User controllingUser,
			@ModelAttribute("ticket") Ticket ticket,
			@PathVariable int ticketId) {

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
			@ModelAttribute("contactEmail") String contactEmail,
			@ModelAttribute("CONTROLLING_USER") User controllingUser,
			@ModelAttribute("ticketNote") TicketNote note,
			BindingResult result,
			@PathVariable int ticketId) {

		ResultModel model = new ResultModel("redirect:/support/ticket/view/ticket/" + ticketId, "support/ticket/reply/reply");

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
