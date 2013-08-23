package com.tscp.mvna.domain.support.ticket.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.user.User;
import com.tscp.mvna.domain.support.ticket.AdminTicket;
import com.tscp.mvna.domain.support.ticket.SearchTicket;
import com.tscp.mvna.domain.support.ticket.TicketPaginator;
import com.tscp.mvna.domain.support.ticket.TicketPriority;
import com.tscp.mvna.domain.support.ticket.TicketStatus;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;
import com.tscp.mvna.domain.support.ticket.manager.TicketManager;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/support/ticket/search")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"searchTicket",
		"ticketList" })
public class TicketSearchController {
	@Autowired
	private TicketManager ticketManager;

	@ModelAttribute
	public void ticketReferenceData(
			ModelMap map) {
		map.addAttribute("ticketCategories", ticketManager.getRootTicketCategories());
		map.addAttribute("priorityList", Arrays.asList(TicketPriority.values()));
		map.addAttribute("statusList", Arrays.asList(TicketStatus.values()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView ticketSearchShow(
			@ModelAttribute("USER") User user) {

		ClientPageView view = new ClientPageView("support/ticket/search");

		SearchTicket searchTicket = new SearchTicket();
		searchTicket.setCustomerId(user.getUserId());
		view.addObject("searchTicket", searchTicket);

		return view;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView ticketSearchPost(
			@ModelAttribute("searchTicket") SearchTicket searchTicket) {

		ClientFormView view = new ClientFormView("support/ticket/search/results", "support/ticket/search");

		try {
			view.addObject("ticketList", new TicketPaginator(ticketManager.searchTickets(searchTicket)));
			view.addObject("ticketSearchContextString", getContextString(searchTicket));
			return view.redirect();
		} catch (TicketManagementException e) {
			return view.exception();
		}
	}

	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public String viewResults() {
		return "redirect:/support/ticket/search/results/1";
	}

	@RequestMapping(value = "/results/{page}", method = RequestMethod.GET)
	public ModelAndView viewResults(
			@PathVariable("page") int page, @ModelAttribute("ticketList") TicketPaginator ticketList) {

		ClientFormView view = new ClientFormView("support/ticket/tickets", "support/ticket/home");

		ticketList.setPageNum(page);
		view.addObject("ticketList", ticketList);
		return view;
	}

	protected static String getContextString(
			AdminTicket ticket) {

		String searchContext = "";

		if (ticket.getCreatorId() != 0)
			searchContext += " Created by " + ticket.getCreatorId();
		if (ticket.getAssigneeId() != 0)
			searchContext += " Assigned to " + ticket.getAssigneeId();
		if (ticket.getStatus() != TicketStatus.NONE)
			searchContext += " with Status " + ticket.getStatus().getDescription();
		if (ticket.getCategory() != null && ticket.getCategory().getId() > 0) {
			searchContext += " in Category ";
			if (ticket.getCategory().getParentCategory() != null)
				searchContext += ticket.getCategory().getParentCategory().getDescription() + " > ";
			searchContext += ticket.getCategory().getDescription();
		}
		if (ticket.getPriority() != TicketPriority.NONE)
			searchContext += " with Priority " + ticket.getPriority().getDescription();
		if (ticket.getTitle() != null && !ticket.getTitle().trim().isEmpty())
			searchContext += " with Title like " + ticket.getTitle();
		if (ticket.getDescription() != null && !ticket.getDescription().trim().isEmpty())
			searchContext += " with Description like " + ticket.getDescription();

		return searchContext;
	}
}
