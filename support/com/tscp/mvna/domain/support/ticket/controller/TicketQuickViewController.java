package com.tscp.mvna.domain.support.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.UserManager;
import com.trc.user.User;
import com.tscp.mvna.domain.support.ticket.AgentTicket;
import com.tscp.mvna.domain.support.ticket.CustomerTicket;
import com.tscp.mvna.domain.support.ticket.InquiryTicket;
import com.tscp.mvna.domain.support.ticket.SearchTicket;
import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketPaginator;
import com.tscp.mvna.domain.support.ticket.TicketType;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;
import com.tscp.mvna.domain.support.ticket.manager.TicketManager;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/support/ticket/view")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"ticket",
		"ticketList" })
public class TicketQuickViewController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private UserManager userManager;

	@RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
	public ModelAndView viewTicketById(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @PathVariable int ticketId) {

		ClientPageView view = new ClientPageView("support/ticket/ticket");

		try {
			Ticket ticket = ticketManager.getTicketById(ticketId);

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

			return view;
		} catch (TicketManagementException e) {
			return view.exception();
		}

	}

	protected ModelAndView getView(
			SearchTicket searchTicket, String context) {

		ClientFormView view = new ClientFormView("support/ticket/search/results", "support/ticket/home");

		try {
			view.addObject("searchTicket", searchTicket);
			view.addObject("ticketList", new TicketPaginator(ticketManager.searchTickets(searchTicket)));
			view.addObject("ticketSearchContextString", context);
			return view.redirect();
		} catch (TicketManagementException e) {
			return view.exception();
		}
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ModelAndView viewAll() {
		return getView(new SearchTicket(), "All Tickets");
	}

	@RequestMapping(value = "/inquiry", method = RequestMethod.GET)
	public ModelAndView viewInquiries() {
		SearchTicket searchTicket = new SearchTicket();
		searchTicket.setType(TicketType.INQUIRY);
		return getView(searchTicket, "All Inquiries");
	}

	@RequestMapping(value = "/creator/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsCreated(
			@ModelAttribute("CONTROLLING_USER") User controllingUser) {
		SearchTicket searchTicket = new SearchTicket();
		searchTicket.setCreatorId(controllingUser.getUserId());
		return getView(searchTicket, "Created by Me");
	}

	@RequestMapping(value = "/assignee/me", method = RequestMethod.GET)
	public ModelAndView viewTicketsAssigned(
			@ModelAttribute("CONTROLLING_USER") User controllingUser) {
		SearchTicket searchTicket = new SearchTicket();
		searchTicket.setAssigneeId(controllingUser.getUserId());
		return getView(searchTicket, "Assigned to Me");
	}

	@RequestMapping(value = "/customer/{userId}", method = RequestMethod.GET)
	public ModelAndView viewTicketsCustomer(
			@ModelAttribute("USER") User user) {
		SearchTicket searchTicket = new SearchTicket();
		searchTicket.setCustomerId(user.getUserId());
		return getView(searchTicket, "For Customer " + user.getEmail());
	}
}
