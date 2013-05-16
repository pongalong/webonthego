package com.trc.dao;

import java.util.Collection;

import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.category.TicketCategory_old;

public interface TicketDaoModel {

	public int saveTicket(Ticket ticket);

	public void updateTicket(Ticket ticket);

	public void deleteTicket(Ticket ticket);

	public Ticket getTicketById(int id);

	public Collection<Ticket> getTicketByStatus(TicketStatus status);

	public Collection<Ticket> getTicketByCustomer(int custId, TicketStatus status);

	public Collection<Ticket> getTicketByCreator(int creatorId, TicketStatus status);

	public Collection<Ticket> getTicketByAssignee(int assigneeId, TicketStatus status);

	public Collection<Ticket> getTicketByCategory(TicketCategory_old category, TicketStatus status);

}
