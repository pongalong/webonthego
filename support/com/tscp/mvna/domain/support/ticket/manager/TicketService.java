package com.tscp.mvna.domain.support.ticket.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tscp.mvna.domain.support.ticket.AdminTicket;
import com.tscp.mvna.domain.support.ticket.AgentTicket;
import com.tscp.mvna.domain.support.ticket.CustomerTicket;
import com.tscp.mvna.domain.support.ticket.InquiryTicket;
import com.tscp.mvna.domain.support.ticket.SearchTicket;
import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketNote;
import com.tscp.mvna.domain.support.ticket.category.TicketCategory;
import com.tscp.mvna.domain.support.ticket.exception.TicketServiceException;

@Service
public class TicketService {
	@Autowired
	private TicketDao ticketDao;

	public Ticket getTicketById(
			int ticketId) throws TicketServiceException {
		return ticketDao.getTicketById(ticketId);
	}

	public List<Ticket> searchTickets(
			SearchTicket ticket) {
		return ticketDao.searchTickets(ticket);
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
