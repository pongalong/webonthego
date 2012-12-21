package com.trc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.user.User;
import com.trc.user.authority.Authority;

/**
 * Used for Activation logging testing
 * 
 */

@Deprecated
public class HibernateUtil_Ticketv2 {

	private static SessionFactory sessionFactory;

	public static Configuration getInitializedConfiguration() {
		Configuration config = new Configuration();
		config.addAnnotatedClass(CustomerTicket.class);
		config.addAnnotatedClass(AgentTicket.class);
		config.addAnnotatedClass(AdminTicket.class);
		config.addAnnotatedClass(InquiryTicket.class);
		config.addAnnotatedClass(Ticket.class);
		config.addAnnotatedClass(User.class);
		config.addAnnotatedClass(TicketNote.class);
		config.addAnnotatedClass(Authority.class);
		config.configure();
		return config;
	}

	public static Session getSession() {
		if (sessionFactory == null) {
			Configuration config = HibernateUtil_Ticketv2.getInitializedConfiguration();
			sessionFactory = config.buildSessionFactory();
		}
		Session hibernateSession = sessionFactory.getCurrentSession();
		return hibernateSession;
	}

}