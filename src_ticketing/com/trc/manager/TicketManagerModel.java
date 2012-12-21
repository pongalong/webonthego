package com.trc.manager;

import java.util.Collection;

import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketStatus;
import com.trc.exception.management.TicketManagementException;
import com.trc.user.User;

public interface TicketManagerModel {

	public Ticket buildMyTicketType(User user);

	public int openTicket(Ticket ticket) throws TicketManagementException;

	public void updateTicket(Ticket ticket) throws TicketManagementException;

	public void deleteTicket(Ticket ticket) throws TicketManagementException;

	public Ticket getTicketById(int id) throws TicketManagementException;

	public Collection<Ticket> getTicketByStatus(TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCustomer(int custId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCreator(int creatorId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByAssignee(int assigneeId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCategory(TicketCategory category, TicketStatus status) throws TicketManagementException;

	public TicketNote getTicketNote(int ticketId, int noteId) throws TicketManagementException;

	public int openTicketNote(TicketNote note) throws TicketManagementException;

	public void updateTicketNote(TicketNote note) throws TicketManagementException;

}
