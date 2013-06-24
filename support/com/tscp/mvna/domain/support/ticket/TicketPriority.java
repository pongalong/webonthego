package com.tscp.mvna.domain.support.ticket;

public enum TicketPriority {
	NONE("Select one"), VERY_HIGH("Very high"), HIGH("High"), NORMAL("Normal"), LOW("Low");

	private String description;

	private TicketPriority(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}