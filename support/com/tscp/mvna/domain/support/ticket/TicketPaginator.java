package com.tscp.mvna.domain.support.ticket;

import java.util.List;

import com.tscp.util.Paginator;

public class TicketPaginator extends Paginator<Ticket> {
	private static final long serialVersionUID = -7785100183853120339L;

	public TicketPaginator(List<Ticket> tickets) {
		super(tickets);
	}

}