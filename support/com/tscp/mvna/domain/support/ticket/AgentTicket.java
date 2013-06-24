package com.tscp.mvna.domain.support.ticket;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "AGENT")
public class AgentTicket extends CustomerTicket {
	private static final long serialVersionUID = 6566960793806943444L;
	private int creatorId;

	public AgentTicket() {
		this.type = TicketType.AGENT;
	}

	@Column(name = "creator")
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

}