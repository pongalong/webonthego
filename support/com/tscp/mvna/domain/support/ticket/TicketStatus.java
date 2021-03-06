package com.tscp.mvna.domain.support.ticket;

public enum TicketStatus {
	NONE("Select one"),
	OPEN("Open"), 
	IN_PROCESS("In Process"), 
	IN_PROCESS_LOCKED("In Process (locked)"), 
	CLOSED("Closed"), 
	RESOLVED("Resolved"), 
	ON_HOLD("On Hold"), 
	UNRESOLVED("Unresolved"), 
	REJECTED("Rejected"), 
	REOPEN("Reopened");

	private String description;

	private TicketStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}