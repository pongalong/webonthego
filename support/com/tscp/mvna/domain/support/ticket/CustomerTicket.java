package com.tscp.mvna.domain.support.ticket;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class CustomerTicket extends Ticket {
	private static final long serialVersionUID = -8249415459905630871L;
	private int customerId;
	private int assigneeId;

	public CustomerTicket() {
		this.type = TicketType.CUSTOMER;
	}

	@Column(name = "customer")
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Column(name = "assignee")
	public int getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(int assigneeId) {
		this.assigneeId = assigneeId;
	}

}
