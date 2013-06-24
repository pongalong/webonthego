package com.tscp.mvna.domain.support.ticket.manager;

import java.util.Collection;

import com.trc.user.User;
import com.tscp.mvna.domain.support.ticket.Ticket;
import com.tscp.mvna.domain.support.ticket.TicketNote;
import com.tscp.mvna.domain.support.ticket.TicketStatus;
import com.tscp.mvna.domain.support.ticket.category.TicketCategory;
import com.tscp.mvna.domain.support.ticket.exception.TicketManagementException;

public interface TicketManagerModel {

	public Ticket buildMyTicketType(
			User user);

	public int openTicket(
			Ticket ticket) throws TicketManagementException;

	public void updateTicket(
			Ticket ticket) throws TicketManagementException;

	public void deleteTicket(
			Ticket ticket) throws TicketManagementException;

	public Ticket getTicketById(
			int id) throws TicketManagementException;

	public Collection<Ticket> getTicketByStatus(
			TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCustomer(
			int custId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCreator(
			int creatorId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByAssignee(
			int assigneeId, TicketStatus status) throws TicketManagementException;

	public Collection<Ticket> getTicketByCategory(
			TicketCategory category, TicketStatus status) throws TicketManagementException;

	public TicketNote getTicketNote(
			int ticketId, int noteId) throws TicketManagementException;

	public int openTicketNote(
			TicketNote note) throws TicketManagementException;

	public void updateTicketNote(
			TicketNote note) throws TicketManagementException;

}
