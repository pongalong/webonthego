package com.trc.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.dao.TicketDao;
import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
import com.trc.domain.ticket.category.TicketCategory;
import com.trc.domain.ticket.category.TicketCategory_old;
import com.trc.exception.service.TicketServiceException;

@Service
public class TicketService {
	@Autowired
	private TicketDao ticketDao;

	public Ticket getTicketById(
			int ticketId) throws TicketServiceException {
		return ticketDao.getTicketById(ticketId);
	}

	public List<Ticket> searchTickets(
			int custId, int creatorId, int assigneeId, TicketStatus status, TicketCategory category, TicketPriority priority, TicketType type, String title, String description) throws TicketServiceException {
		return ticketDao.searchTickets(custId, creatorId, assigneeId, status, category, priority, type, title, description);
	}

	public int saveTicket(
			Ticket ticket) throws TicketServiceException {
		return ticketDao.save(ticket);
	}

	public int saveTicket(
			InquiryTicket ticket) throws TicketServiceException {
		return ticketDao.save(ticket);
	}

	public int saveTicket(
			AgentTicket ticket) throws TicketServiceException {
		return ticketDao.save(ticket);
	}

	public int saveTicket(
			AdminTicket ticket) throws TicketServiceException {
		return ticketDao.save(ticket);
	}

	public int saveTicket(
			CustomerTicket ticket) throws TicketServiceException {
		return ticketDao.save(ticket);
	}

	public void updateTicket(
			Ticket ticket) throws TicketServiceException {
		ticket.setLastModifiedDate(new Date());
		ticketDao.update(ticket);
	}

	public int saveNote(
			TicketNote note) throws TicketServiceException {
		return ticketDao.save(note);
	}

	public void updateNote(
			TicketNote note) throws TicketServiceException {
		ticketDao.update(note);
	}

	public TicketNote getNoteById(
			int noteId) throws TicketServiceException {
		return ticketDao.getNoteById(noteId);
	}

	public List<TicketCategory> getTicketCategories() {
		return ticketDao.getTicketCategories(false, true);
	}

	public List<TicketCategory> getRootTicketCategories() {
		return ticketDao.getTicketCategories(true, false);
	}

	public List<TicketCategory> getAllTicketCategories() {
		return ticketDao.getTicketCategories(true, true);
	}

}
