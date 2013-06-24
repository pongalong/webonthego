package com.tscp.mvna.domain.support.ticket;

public class SearchTicket extends AdminTicket {
	private static final long serialVersionUID = 2340637346206259751L;

	public SearchTicket() {
		this.priority = TicketPriority.NONE;
		this.status = TicketStatus.NONE;
		this.type = TicketType.NONE;
	}

}