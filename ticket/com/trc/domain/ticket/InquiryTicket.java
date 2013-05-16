package com.trc.domain.ticket;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "INQUIRY")
public class InquiryTicket extends Ticket {
	private static final long serialVersionUID = -3788997727517144764L;
	private String contactEmail;
	private String contactPhone;

	public InquiryTicket() {
		this.type = TicketType.INQUIRY;
		this.title = "INQUIRY " + this.createdDate;
	}

	@Column(name = "requester_email")
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(
			String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "requester_contact_phone")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(
			String contactPhone) {
		this.contactPhone = contactPhone;
	}

}