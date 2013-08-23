package com.tscp.mvna.domain.support.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.UserManager;
import com.trc.user.User;
import com.tscp.mvna.domain.support.ticket.TicketNote;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;
import com.tscp.mvna.domain.support.ticket.manager.TicketManager;
import com.tscp.mvna.domain.support.ticket.validation.TicketValidator;
import com.tscp.mvna.web.controller.model.ClientFormView;
import com.tscp.mvna.web.controller.model.ClientPageView;

//TODO CREATE ticketNoteValidator
@Controller
@RequestMapping("/support/ticket/note")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"ticket",
		"ticketNote" })
public class TicketNoteController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private TicketValidator ticketValidator;
	@Autowired
	private UserManager userManager;

	@RequestMapping(value = "/add/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showAddNote(
			@ModelAttribute("CONTROLLING_USER") User controllingUser, @PathVariable int ticketId) {

		ClientPageView view = new ClientPageView("support/ticket/note/add");

		try {
			TicketNote note = new TicketNote();
			note.setCreator(controllingUser);
			note.setTicket(ticketManager.getTicketById(ticketId));

			view.addObject("ticketNote", note);

			return view;
		} catch (TicketManagementException e) {
			return view.exception();
		}
	}

	@RequestMapping(value = "/add/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postAddNote(
			@PathVariable int ticketId, @ModelAttribute("ticketNote") TicketNote note, BindingResult result) {

		ClientFormView view = new ClientFormView("support/ticket/view/" + ticketId, "support/ticket/note/add");

		// TODO ADD VALIDATION

		if (result.hasErrors())
			return view.validationFailed();

		try {
			ticketManager.saveNote(note);
			return view.redirect();
		} catch (TicketManagementException e) {
			return view.exception();
		}

	}

	@RequestMapping(value = "/update/{noteId}", method = RequestMethod.GET)
	public ModelAndView showUpdateNote(
			@PathVariable int noteId) {

		ClientPageView view = new ClientPageView("support/ticket/note/update");

		try {
			view.addObject("ticketNote", ticketManager.getNoteById(noteId));
			return view;
		} catch (TicketManagementException e) {
			return view.exception();
		}
	}

	@RequestMapping(value = "/update/{noteId}", method = RequestMethod.POST)
	public ModelAndView postUpdateNote(
			@ModelAttribute("ticketNote") TicketNote note, BindingResult result) {

		ClientFormView view = new ClientFormView("support/ticket/view/" + note.getTicket().getId(), "support/ticket/note/edit");

		// TODO ADD VALIDATION

		if (result.hasErrors())
			return view.validationFailed();

		try {
			ticketManager.updateNote(note);
			return view.redirect();
		} catch (TicketManagementException e) {
			return view.exception();
		}

	}

}