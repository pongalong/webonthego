package com.trc.web.controller;

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

import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.exception.management.TicketManagementException;
import com.trc.manager.TicketManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.model.ResultModel;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.trc.web.validation.TicketValidator;

//TODO CREATE ticketNoteValidator
@Controller
@RequestMapping("/support/ticket/note")
public class TicketNoteController {
	@Autowired
	private TicketManager ticketManager;
	@Autowired
	private TicketValidator ticketValidator;
	@Autowired
	private UserManager userManager;

	static final Logger logger = LoggerFactory.getLogger("devLogger");

	@RequestMapping(value = "/add/{ticketId}", method = RequestMethod.GET)
	public ModelAndView showAddNote(@PathVariable int ticketId) {
		ResultModel resultModel = new ResultModel("support/ticket/note/add");
		try {
			Ticket ticket = ticketManager.getTicketById(ticketId);

			SessionManager.set(SessionKey.TICKET, ticket);

			resultModel.addObject("ticket", ticket);
			resultModel.addObject("note", new TicketNote());
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/add/{ticketId}", method = RequestMethod.POST)
	public ModelAndView postAddNote(@PathVariable int ticketId, @ModelAttribute Ticket ticket, @ModelAttribute TicketNote note, BindingResult result) {
		ResultModel resultModel = new ResultModel("redirect:/support/ticket/view/ticket/" + ticketId, "support/ticket/note/add");

		User ticketCreator = userManager.getLoggedInUser();
		Ticket sessionTicket = (Ticket) SessionManager.get(SessionKey.TICKET);

		note.setTicket(sessionTicket);
		note.setCreator(ticketCreator);
		ticketValidator.validate(sessionTicket, result);
		if (result.hasErrors()) {
			return resultModel.getError();
		} else {
			try {
				ticketManager.saveNote(note);
				return resultModel.getSuccess();
			} catch (TicketManagementException e) {
				return resultModel.getException();
			}
		}
	}

	@RequestMapping(value = "/update/{noteId}", method = RequestMethod.GET)
	public ModelAndView showUpdateNote(@PathVariable int noteId) {
		ResultModel resultModel = new ResultModel("support/ticket/note/update");
		try {
			TicketNote ticketNote = ticketManager.getNoteById(noteId);
			SessionManager.set(SessionKey.TICKET_NOTE, ticketNote);
			resultModel.addObject("note", ticketNote);
			return resultModel.getSuccess();
		} catch (TicketManagementException e) {
			return resultModel.getAccessException();
		}
	}

	@RequestMapping(value = "/update/{noteId}", method = RequestMethod.POST)
	public ModelAndView postUpdateNote(@ModelAttribute TicketNote note, BindingResult result) {
		TicketNote sessionTicketNote = (TicketNote) SessionManager.get(SessionKey.TICKET_NOTE);
		TicketNote.sync(note, sessionTicketNote);

		ResultModel resultModel = new ResultModel("redirect:/support/ticket/view/ticket/" + note.getTicket().getId(), "support/ticket/note/edit");

		logger.debug("{}", note.getId());
		logger.debug("note has ticket {}", note.getTicket().getType());

		ticketValidator.validate(sessionTicketNote, result);
		if (result.hasErrors()) {
			return resultModel.getError();
		} else {
			try {
				ticketManager.updateNote(note);
				return resultModel.getSuccess();
			} catch (TicketManagementException e) {
				return resultModel.getAccessException();
			}
		}
	}

}