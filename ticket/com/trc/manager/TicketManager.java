package com.trc.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.trc.exception.management.TicketManagementException;
import com.trc.exception.service.TicketServiceException;
import com.trc.service.TicketService;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;

@Component
public class TicketManager {
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserManager userManager;

	public Ticket buildMyTicketType(
			User user) {
		Ticket ticket;

		Authority authority = user.getGreatestAuthority();

		if (authority.getRole() == ROLE.ROLE_ADMIN)
			ticket = new AdminTicket();
		else if (authority.getRole() == ROLE.ROLE_MANAGER)
			ticket = new AgentTicket();
		else if (authority.getRole() == ROLE.ROLE_AGENT)
			ticket = new AgentTicket();
		else if (authority.getRole() == ROLE.ROLE_SU)
			ticket = new AdminTicket();
		else
			ticket = new CustomerTicket();

		return ticket;
	}

	public Ticket getTicketById(
			int ticketId) throws TicketManagementException {
		try {
			return ticketService.getTicketById(ticketId);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public List<Ticket> searchTickets(
			int custId,
			int creatorId,
			int assigneeId,
			TicketStatus status,
			TicketCategory category,
			TicketPriority priority,
			TicketType type,
			String title,
			String description) throws TicketManagementException {
		try {
			return ticketService.searchTickets(custId, creatorId, assigneeId, status, category, priority, type, title, description);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public TicketNote getTicketNote(
			int ticketId,
			int noteId) throws TicketManagementException {
		Ticket ticket = getTicketById(ticketId);
		for (TicketNote note : ticket.getNotes()) {
			if (note.getId() == noteId)
				return note;
		}
		throw new TicketManagementException("Note " + noteId + " not found on ticket " + ticketId);
	}

	public int openTicket(
			Ticket ticket) throws TicketManagementException {
		try {
			TicketType type = ticket.getType();
			User customer;
			User creator;
			switch (type) {
				case INQUIRY:
					return ticketService.saveTicket((InquiryTicket) ticket);
				case CUSTOMER:
					customer = userManager.getCurrentUser();
					((CustomerTicket) ticket).setCustomerId(customer.getUserId());
					return ticketService.saveTicket((CustomerTicket) ticket);
				case AGENT:
					customer = userManager.getCurrentUser();
					creator = userManager.getLoggedInUser();
					((AgentTicket) ticket).setCustomerId(customer.getUserId());
					((AgentTicket) ticket).setCreatorId(creator.getUserId());
					return ticketService.saveTicket((AgentTicket) ticket);
				case ADMIN:
					customer = userManager.getCurrentUser();
					creator = userManager.getLoggedInUser();
					((AdminTicket) ticket).setCustomerId(customer.getUserId());
					((AdminTicket) ticket).setCreatorId(creator.getUserId());
					return ticketService.saveTicket((AdminTicket) ticket);
				default:
					throw new TicketManagementException("Cannot open ticket for unknown type");
			}
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public void updateTicket(
			Ticket ticket) throws TicketManagementException {
		try {
			ticketService.updateTicket(ticket);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public int saveNote(
			TicketNote note) throws TicketManagementException {
		try {
			return ticketService.saveNote(note);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public void updateNote(
			TicketNote note) throws TicketManagementException {
		try {
			ticketService.updateNote(note);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	public TicketNote getNoteById(
			int noteId) throws TicketManagementException {
		try {
			return ticketService.getNoteById(noteId);
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

}
