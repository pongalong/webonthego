package com.tscp.mvna.domain.support.ticket;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "ADMIN")
public class AdminTicket extends AgentTicket {
	private static final long serialVersionUID = 3545858776250871180L;

	public AdminTicket() {
		this.type = TicketType.ADMIN;
	}

}
