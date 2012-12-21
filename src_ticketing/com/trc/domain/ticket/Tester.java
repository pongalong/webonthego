package com.trc.domain.ticket;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.trc.hibernate.HibernateUtil_Ticketv2;
import com.trc.user.User;

public class Tester {
	static final Logger logger = LoggerFactory.getLogger("devLogger");
	static final User user = new User();

	public static void main(String[] args) {
		logger.trace("Hello world");
		Session session = HibernateUtil_Ticketv2.getSession();
		logger.debug("session opened");
		Transaction tx = session.beginTransaction();
		logger.debug("transaction started");

		user.setUsername("blah blah");
		user.setUserId(5489);
		logger.debug("user is {}", user.getUsername());

		Ticket ticket = null;
		ticket = buildAdminTicket();

		int id = (Integer) session.save(ticket);
		logger.debug("ticket created with ID {}", id);
		tx.commit();
		logger.debug("closing...");
	}

	public static Ticket buildAdminTicket() {
		AdminTicket adminTicket = new AdminTicket();
		adminTicket.setCategory(TicketCategory.ACCOUNT_SETTINGS);
		adminTicket.setDescription("my description string");
		adminTicket.setTitle("my ticket title");
		adminTicket.setAssigneeId(user.getUserId());
		adminTicket.setCustomerId(user.getUserId());
		adminTicket.setCreatorId(user.getUserId());
		return adminTicket;
	}

	public static Ticket buildAgentTicket() {
		AgentTicket agentTicket = new AgentTicket();
		agentTicket.setCategory(TicketCategory.ACCOUNT_SETTINGS);
		agentTicket.setDescription("my description string");
		agentTicket.setTitle("my ticket title");
		agentTicket.setAssigneeId(user.getUserId());
		agentTicket.setCustomerId(user.getUserId());
		agentTicket.setCreatorId(user.getUserId());
		return agentTicket;
	}

	public static Ticket buildCustomerTicket() {
		CustomerTicket customerTicket = new CustomerTicket();
		customerTicket.setCategory(TicketCategory.ACCOUNT_SETTINGS);
		customerTicket.setDescription("my description string");
		customerTicket.setTitle("my ticket title");
		customerTicket.setAssigneeId(user.getUserId());
		customerTicket.setCustomerId(user.getUserId());
		return customerTicket;
	}

	public static Ticket buildInquiryTicket() {
		InquiryTicket inquiryTicket = new InquiryTicket();
		inquiryTicket.setCategory(TicketCategory.ACCOUNT_SETTINGS);
		inquiryTicket.setContactPhone("6262007989");
		inquiryTicket.setDescription("my description string");
		inquiryTicket.setContactEmail("myEmail@email.com");
		inquiryTicket.setTitle("my ticket title");
		return inquiryTicket;
	}

	public static Logger loadLogger() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			configurator.doConfigure("C:\\Users\\Jonathan\\workspace\\TruConnect\\WebContent\\WEB-INF\\resources\\logback.xml");
		} catch (JoranException je) {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		return context.getLogger("devLogger");
	}

}
