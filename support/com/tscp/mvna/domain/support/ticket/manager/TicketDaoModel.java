package com.tscp.mvna.domain.support.ticket.manager;

import java.util.Collection;

import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketStatus;
import com.tscp.mvna.domain.support.ticket.category.TicketCategory;

public interface TicketDaoModel {

	public int saveTicket(
			Ticket ticket);

	public void updateTicket(
			Ticket ticket);

	public void deleteTicket(
			Ticket ticket);

	public Ticket getTicketById(
			int id);

	public Collection<Ticket> getTicketByStatus(
			TicketStatus status);

	public Collection<Ticket> getTicketByCustomer(
			int custId, TicketStatus status);

	public Collection<Ticket> getTicketByCreator(
			int creatorId, TicketStatus status);

	public Collection<Ticket> getTicketByAssignee(
			int assigneeId, TicketStatus status);

	public Collection<Ticket> getTicketByCategory(
			TicketCategory category, TicketStatus status);

}
