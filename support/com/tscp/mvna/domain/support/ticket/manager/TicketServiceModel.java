package com.tscp.mvna.domain.support.ticket.manager;

import java.util.Collection;

import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketStatus;
import com.tscp.mvna.domain.support.ticket.category.TicketCategory;
import com.tscp.mvna.domain.support.ticket.exception.TicketServiceException;

public interface TicketServiceModel {

	public int saveTicket(
			Ticket ticket) throws TicketServiceException;

	public void updateTicket(
			Ticket ticket) throws TicketServiceException;

	public void deleteTicket(
			Ticket ticket) throws TicketServiceException;

	public Ticket getTicketById(
			int id) throws TicketServiceException;

	public Collection<Ticket> getTicketByStatus(
			TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCustomer(
			int custId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCreator(
			int creatorId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByAssignee(
			int assigneeId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCategory(
			TicketCategory category, TicketStatus status) throws TicketServiceException;

}