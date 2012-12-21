package com.trc.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.dao.TicketDao;
import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
import com.trc.exception.service.TicketServiceException;

@Service
public class TicketService {
	@Autowired
	private TicketDao ticketDao;

	static final Logger logger = LoggerFactory.getLogger("devLogger");

	public Ticket getTicketById(int ticketId) throws TicketServiceException {
		return ticketDao.getTicketById(ticketId);
	}

	public List<Ticket> searchTickets(int custId, int creatorId, int assigneeId, TicketStatus status, TicketCategory category, TicketPriority priority,
			TicketType type, String title, String description) throws TicketServiceException {
		return ticketDao.searchTickets(custId, creatorId, assigneeId, status, category, priority, type, title, description);
	}

	public int saveTicket(Ticket ticket) throws TicketServiceException {
		logger.debug("ticketService instanceof AdminTicket {}", ticket instanceof AdminTicket);
		return ticketDao.save(ticket);
	}

	public void updateTicket(Ticket ticket) throws TicketServiceException {
		logger.debug("ticketService instanceof AdminTicket {}", ticket instanceof AdminTicket);
		ticketDao.update(ticket);
	}

	public int saveNote(TicketNote note) throws TicketServiceException {
		return ticketDao.save(note);
	}

	public void updateNote(TicketNote note) throws TicketServiceException {
		ticketDao.update(note);
	}

	public TicketNote getNoteById(int noteId) throws TicketServiceException {
		return ticketDao.getNoteById(noteId);
	}

}
