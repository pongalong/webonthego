package com.trc.domain.ticket;

import java.util.List;

import com.trc.util.Paginator;

public class TicketPaginator extends Paginator<Ticket> {

	public TicketPaginator(List<Ticket> tickets) {
		super.setRecords(tickets);
		super.setSummarySize(3);
	}

}