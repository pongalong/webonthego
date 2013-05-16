package com.trc.domain.ticket.category;

public enum TicketCategory_old {
	NONE("Select one"),
	REGISTRATION("Account registration"),
	DEVICE_ACTIVATION("Device activation"),
	DEVICE_INACTIVE("Device not working"),
	ACCOUNT_SUSPENSION("Account suspension"),
	ACCOUNT_SETTINGS("Account settings"),
	BALANCE_REFUND("Refund / Dispute Usage"),
	USAGE("Usage"),
	COUPON("Coupons"),
	OTHER("Other");

	private String description;

	private TicketCategory_old(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public TicketCategory toTicketCategory() {
		TicketCategory ticketCategory = new TicketCategory();

		switch (this) {
			case NONE:
				ticketCategory.setId(0);
				break;
			case REGISTRATION:
				ticketCategory.setId(34);
				break;
			case DEVICE_ACTIVATION:
				ticketCategory.setId(35);
				break;
			case DEVICE_INACTIVE:
				ticketCategory.setId(3);
				break;
			case ACCOUNT_SUSPENSION:
				ticketCategory.setId(44);
				break;
			case ACCOUNT_SETTINGS:
				ticketCategory.setId(4);
				break;
			case BALANCE_REFUND:
				ticketCategory.setId(45);
				break;
			case USAGE:
				ticketCategory.setId(45);
				break;
			case COUPON:
				ticketCategory.setId(24);
				break;
			case OTHER:
				ticketCategory.setId(1);
				break;
		}

		return ticketCategory;

	}

}
