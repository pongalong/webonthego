package com.trc.web.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
import com.trc.exception.management.TicketManagementException;
import com.trc.manager.TicketManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.trc.web.validation.TicketValidator;

@Controller
@RequestMapping("/support/ticket")
public class TicketController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private TicketValidator ticketValidator;
	@Autowired
	private UserManager userManager;

	static final Logger logger = LoggerFactory.getLogger("devLogger");

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showTicketOverviewAndSearch() {
		ResultModel resultModel = new ResultModel("support/ticket/overview");

		AdminTicket ticket = new AdminTicket();
		ticket.setStatus(null);
		ticket.setPriority(null);
		ticket.setCategory(null);

		resultModel.addObject("ticket", ticket);
		resultModel.addObject("categoryList", Arrays.asList(TicketCategory.values()));
		resultModel.addObject("priorityList", Arrays.asList(TicketPriority.values()));
		resultModel.addObject("statusList", Arrays.asList(TicketStatus.values()));
		return resultModel.getSuccess();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView postTicketOverviewAndSearch(@ModelAttribute AdminTicket ticket) {
		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");
		User ticketOwner = userManager.getCurrentUser();
		try {
			List<Ticket> tickets = ticketManager.searchTickets(ticketOwner.getUserId(), ticket.getCreatorId(), ticket.getAssigneeId(), ticket.getStatus(),
					ticket.getCategory(), ticket.getPriority(), null, ticket.getTitle(), ticket.getDescription());
			resultModel.addObject("ticketList", tickets);

			String ticketSearchContextString = "";
			if (ticket.getCreatorId() != 0) {
				ticketSearchContextString += " Created by " + ticket.getCreatorId();
			}
			if (ticket.getAssigneeId() != 0) {
				ticketSearchContextString += " Assigned to " + ticket.getAssigneeId();
			}
			if (ticket.getStatus() != TicketStatus.NONE) {
				ticketSearchContextString += " with Status " + ticket.getStatus().getDescription();
			}
			if (ticket.getCategory() != TicketCategory.NONE) {
				ticketSearchContextString += " in Category " + ticket.getCategory().getDescription();
			}
			if (ticket.getPriority() != TicketPriority.NONE) {
				ticketSearchContextString += " with Priority " + ticket.getPriority().getDescription();
			}
			if (ticket.getTitle() != null && !ticket.getTitle().trim().isEmpty()) {
				ticketSearchContextString += " with Title like " + ticket.getTitle();
			}
			if (ticket.getDescription() != null && !ticket.getDescription().trim().isEmpty()) {
				ticketSearchContextString += " with Description like " + ticket.getDescription();
			}

			resultModel.addObject("ticketSearchContextString", ticketSearchContextString);
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showTicketForm() {
		ResultModel resultModel = new ResultModel("support/ticket/create", "support/ticket/exception/error_select_user");
		User ticketOwner = userManager.getCurrentUser();
		if (ticketOwner.getUserId() == 0)
			return resultModel.getError();
		Ticket ticket = ticketManager.buildMyTicketType(userManager.getLoggedInUser());
		SessionManager.set(SessionKey.TICKET, ticket);
		resultModel.addObject("ticketOwner", ticketOwner);
		resultModel.addObject("categoryList", Arrays.asList(TicketCategory.values()));
		resultModel.addObject("priorityList", Arrays.asList(TicketPriority.values()));
		resultModel.addObject("ticket", ticket);
		return resultModel.getSuccess();

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView postTicketForm(@ModelAttribute Ticket ticket, BindingResult result) {
		Ticket sessionTicket = (Ticket) SessionManager.get(SessionKey.TICKET);
		ticket = Ticket.sync(ticket, sessionTicket);
		ResultModel resultModel = new ResultModel("support/ticket/ticket", "support/ticket/create");
		ticketValidator.validate(ticket, result);
		if (result.hasErrors()) {
			return resultModel.getError();
		} else {
			try {
				int ticketId = ticketManager.openTicket(ticket);
				ticket.setId(ticketId);
				resultModel.addObject("ticket", ticket);
				return resultModel.getSuccess();
			} catch (TicketManagementException e) {
				return resultModel.getException();
			}
		}
	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showUpdateTicket(@PathVariable int ticketId) {
		ResultModel resultModel = new ResultModel("support/ticket/update");
		try {
			Ticket ticket = ticketManager.getTicketById(ticketId);

			logger.debug("ticketController show instanceof AdminTicket {}", ticket instanceof AdminTicket);

			SessionManager.set(SessionKey.TICKET, ticket);
			resultModel.addObject("categoryList", Arrays.asList(TicketCategory.values()));
			resultModel.addObject("priorityList", Arrays.asList(TicketPriority.values()));
			resultModel.addObject("statusList", Arrays.asList(TicketStatus.values()));
			resultModel.addObject("ticket", ticket);
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postUpdateTicket(@ModelAttribute Ticket ticket, BindingResult result) {
		Ticket sessionTicket = (Ticket) SessionManager.get(SessionKey.TICKET);
		ticket = Ticket.sync(ticket, sessionTicket);

		logger.debug("ticketController post instanceof AdminTicket {}", ticket instanceof AdminTicket);

		ResultModel resultModel = new ResultModel("redirect:/support/ticket/view/ticket/" + ticket.getId(), "support/ticket/update");
		ticketValidator.validate(ticket, result);
		if (result.hasErrors()) {
			logger.debug("returning errors");
			resultModel.addObject("ticket", ticket);
			resultModel.addObject("categoryList", Arrays.asList(TicketCategory.values()));
			resultModel.addObject("priorityList", Arrays.asList(TicketPriority.values()));
			resultModel.addObject("statusList", Arrays.asList(TicketStatus.values()));
			return resultModel.getError();
		} else {
			try {
				ticketManager.updateTicket(ticket);
				resultModel.addObject("ticket", ticket);
				return resultModel.getSuccess();
			} catch (TicketManagementException e) {
				logger.debug("exception {}", e);
				return resultModel.getException();
			}
		}
	}

	@RequestMapping(value = "/view/ticket/{ticketId}", method = RequestMethod.GET)
	public ModelAndView viewTicketById(@PathVariable int ticketId) {
		ResultModel resultModel = new ResultModel("support/ticket/ticket");
		try {
			Ticket ticket = ticketManager.getTicketById(ticketId);
			resultModel.addObject("ticket", ticket);
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/view/inquiry", method = RequestMethod.GET)
	public ModelAndView viewInquiries() {
		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");
		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, 0, 0, null, null, null, TicketType.INQUIRY, null, null);
			resultModel.addObject("ticketList", tickets);
			resultModel.addObject("ticketSearchContextString", "Inquiries");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/view/creator/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsCreated() {
		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");
		User ticketCreator = userManager.getLoggedInUser();
		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, ticketCreator.getUserId(), 0, TicketStatus.OPEN, null, null, null, null, null);
			resultModel.addObject("ticketList", tickets);
			resultModel.addObject("ticketSearchContextString", "Created By Me");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/view/assignee/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsAssigned() {
		ResultModel resultModel = new ResultModel("support/ticket/tickets", "support/ticket/overview");
		User ticketCreator = userManager.getLoggedInUser();
		try {
			List<Ticket> tickets = ticketManager.searchTickets(0, 0, ticketCreator.getUserId(), TicketStatus.OPEN, null, null, null, null, null);
			resultModel.addObject("ticketList", tickets);
			resultModel.addObject("ticketSearchContextString", "Assigned to Me");
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

}
