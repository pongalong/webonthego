package com.trc.service;

import java.util.Collection;

import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketStatus;
import com.trc.exception.service.TicketServiceException;

public interface TicketServiceModel {

	public int saveTicket(Ticket ticket) throws TicketServiceException;

	public void updateTicket(Ticket ticket) throws TicketServiceException;

	public void deleteTicket(Ticket ticket) throws TicketServiceException;

	public Ticket getTicketById(int id) throws TicketServiceException;

	public Collection<Ticket> getTicketByStatus(TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCustomer(int custId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCreator(int creatorId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByAssignee(int assigneeId, TicketStatus status) throws TicketServiceException;

	public Collection<Ticket> getTicketByCategory(TicketCategory category, TicketStatus status) throws TicketServiceException;

}