package com.trc.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
import com.trc.exception.management.TicketManagementException;
import com.trc.exception.service.TicketServiceException;
import com.trc.manager.UserManager;
import com.trc.service.TicketService;
import com.trc.user.User;

@Component
public class TicketManager {
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserManager userManager;

	static final Logger logger = LoggerFactory.getLogger("devLogger");

	public Ticket buildMyTicketType(User user) {
		Ticket ticket;
		if (user.isAdmin()) {
			ticket = new AdminTicket();
		} else if (user.isManager()) {
			ticket = new AgentTicket();
		} else if (user.isServiceRep()) {
			ticket = new AgentTicket();
		} else if (user.isSuperUser()) {
			ticket = new AdminTicket();
		} else {
			ticket = new CustomerTicket();
		}
		return ticket;
	}

	public Ticket getTicketById(int ticketId) throws TicketManagementException {
		try {
			return ticketService.getTicketById(ticketId);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public List<Ticket> searchTickets(int custId, int creatorId, int assigneeId, TicketStatus status, TicketCategory category, TicketPriority priority,
			TicketType type, String title, String description) throws TicketManagementException {
		try {
			return ticketService.searchTickets(custId, creatorId, assigneeId, status, category, priority, type, title, description);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public TicketNote getTicketNote(int ticketId, int noteId) throws TicketManagementException {
		Ticket ticket = getTicketById(ticketId);
		for (TicketNote note : ticket.getNotes()) {
			if (note.getId() == noteId)
				return note;
		}
		throw new TicketManagementException("Note " + noteId + " not found on ticket " + ticketId);
	}

	public int openTicket(Ticket ticket) throws TicketManagementException {
		try {
			TicketType type = ticket.getType();
			User customer;
			User creator;
			logger.debug("ticketManager instanceof AdminTicket {}", ticket instanceof AdminTicket);
			switch (type) {
				case INQUIRY:
					break;
				case CUSTOMER:
					customer = userManager.getCurrentUser();
					((CustomerTicket) ticket).setCustomerId(customer.getUserId());
					break;
				case AGENT:
					customer = userManager.getCurrentUser();
					creator = userManager.getLoggedInUser();
					((AgentTicket) ticket).setCustomerId(customer.getUserId());
					((AgentTicket) ticket).setCreatorId(creator.getUserId());
					break;
				case ADMIN:
					customer = userManager.getCurrentUser();
					creator = userManager.getLoggedInUser();
					((AdminTicket) ticket).setCustomerId(customer.getUserId());
					((AdminTicket) ticket).setCreatorId(creator.getUserId());
					break;
				default:
					logger.debug("unknown ticket type in switch block");
					throw new TicketManagementException("Cannot open ticket for unknown type");
			}
			return ticketService.saveTicket(ticket);
		} catch (TicketServiceException e) {
			logger.debug("{}", e);
			throw new TicketManagementException(e);
		}
	}

	public void updateTicket(Ticket ticket) throws TicketManagementException {
		logger.debug("ticketManager instanceof AdminTicket {}", ticket instanceof AdminTicket);
		try {
			ticketService.updateTicket(ticket);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public int saveNote(TicketNote note) throws TicketManagementException {
		try {
			return ticketService.saveNote(note);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public void updateNote(TicketNote note) throws TicketManagementException {
		try {
			ticketService.updateNote(note);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public TicketNote getNoteById(int noteId) throws TicketManagementException {
		try {
			return ticketService.getNoteById(noteId);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

}
