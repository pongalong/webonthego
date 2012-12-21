package com.trc.web.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.Ticket;

@Component
public class TicketValidator implements Validator {

	static final Logger logger = LoggerFactory.getLogger("devLogger");

	@Override
	public boolean supports(Class<?> myClass) {
		boolean result = InquiryTicket.class.isAssignableFrom(myClass) || CustomerTicket.class.isAssignableFrom(myClass)
				|| AgentTicket.class.isAssignableFrom(myClass) || AdminTicket.class.isAssignableFrom(myClass);
		logger.debug("validator supports {}", result);
		return result;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Ticket ticket = (Ticket) target;
		if (ticket instanceof InquiryTicket) {
			checkInquiryTicket(ticket, errors);
		} else if (ticket instanceof CustomerTicket) {
			checkCustomerTicket(ticket, errors);
		} else if (ticket instanceof AgentTicket) {
			checkAgentTicket(ticket, errors);
		} else if (ticket instanceof AdminTicket) {
			checkAdminTicket(ticket, errors);
		}
		logger.debug("validator errors {}", errors.getAllErrors());
	}

	private void checkAdminTicket(Ticket ticket, Errors errors) {
		// TODO Auto-generated method stub

	}

	private void checkAgentTicket(Ticket ticket, Errors errors) {
		// TODO Auto-generated method stub

	}

	private void checkCustomerTicket(Ticket ticket, Errors errors) {
		// TODO Auto-generated method stub

	}

	private void checkInquiryTicket(Ticket ticket, Errors errors) {
		// TODO Auto-generated method stub

	}

}
