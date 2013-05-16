package com.trc.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketPriority;

@Component
public class TicketValidator implements Validator {

	@Override
	public boolean supports(
			Class<?> myClass) {
		boolean result = InquiryTicket.class.isAssignableFrom(myClass) || CustomerTicket.class.isAssignableFrom(myClass) || AgentTicket.class.isAssignableFrom(myClass) || AdminTicket.class.isAssignableFrom(myClass);
		return result;
	}

	@Override
	public void validate(
			Object target, Errors errors) {
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
	}

	private void checkAdminTicket(
			Ticket ticket, Errors errors) {

		AdminTicket adminTicket = (AdminTicket) ticket;

		checkDescriptionAndCategory(adminTicket, errors);
		checkPriority(adminTicket, errors);
	}

	private void checkAgentTicket(
			Ticket ticket, Errors errors) {

		AgentTicket agentTicket = (AgentTicket) ticket;

		checkDescriptionAndCategory(agentTicket, errors);
		checkPriority(agentTicket, errors);
	}

	private void checkCustomerTicket(
			Ticket ticket, Errors errors) {

		CustomerTicket customerTicket = (CustomerTicket) ticket;

		checkDescriptionAndCategory(customerTicket, errors);
		checkPriority(customerTicket, errors);
	}

	private void checkInquiryTicket(
			Ticket ticket, Errors errors) {

		InquiryTicket inquiryTicket = (InquiryTicket) ticket;

		if (ValidationUtil.isEmpty(inquiryTicket.getContactEmail())) {
			errors.rejectValue("contactEmail", "email.required", "Please enter an e-mail address");
		} else if (!EmailValidator.checkEmail(inquiryTicket.getContactEmail())) {
			errors.rejectValue("contactEmail", "email.invalid", "E-mail address is invalid");
		}

		checkDescriptionAndCategory(inquiryTicket, errors);
	}

	private void checkDescriptionAndCategory(
			Ticket ticket, Errors errors) {

		if (ValidationUtil.isEmpty(ticket.getDescription()))
			errors.rejectValue("description", "ticket.description.required", "You must enter a description");
		else if (!ValidationUtil.isBetween(ticket.getDescription(), 10, 10000))
			errors.rejectValue("description", "ticket.description.size", "Your description is too short");
		if (ticket.getCategory().getId() <= 0)
			errors.rejectValue("category.id", "ticket.category.required", "You must choose a category");
	}

	private void checkPriority(
			Ticket ticket, Errors errors) {

		if (ticket.getPriority() == TicketPriority.NONE)
			errors.rejectValue("priority", "ticket.priority.required", "You must set a priority");

	}
}
