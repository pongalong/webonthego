package com.trc.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.SearchTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketType;
import com.trc.domain.ticket.category.TicketCategory;
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
			SearchTicket ticket) throws TicketManagementException {
		return ticketService.searchTickets(ticket);
	}

	public TicketNote getTicketNote(
			int ticketId, int noteId) throws TicketManagementException {
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
			switch (type) {
				case INQUIRY:
					return ticketService.saveTicket((InquiryTicket) ticket);
				case CUSTOMER:
					return ticketService.saveTicket((CustomerTicket) ticket);
				case AGENT:
					return ticketService.saveTicket((AgentTicket) ticket);
				case ADMIN:
					return ticketService.saveTicket((AdminTicket) ticket);
				default:
					throw new TicketManagementException("Cannot open ticket for unknown type");
			}
		} catch (TicketServiceException e) {
			throw new TicketManagementException(e);
		}
	}

	@PreAuthorize("isAuthenticated() and hasPermission('', 'ticketAssign')")
	public void assignTicket(
			Ticket ticket, User user) throws TicketManagementException {
		AdminTicket adminTicket = (AdminTicket) ticket;
		adminTicket.setAssigneeId(user.getUserId());
		updateTicket(adminTicket);
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

	public List<TicketCategory> getAllTicketCategories() {
		return ticketService.getAllTicketCategories();
	}

	public List<TicketCategory> getTicketCategories() {
		return ticketService.getTicketCategories();
	}

	public List<TicketCategory> getRootTicketCategories() {
		return ticketService.getRootTicketCategories();
	}

}
