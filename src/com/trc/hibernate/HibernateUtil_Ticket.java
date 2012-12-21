package com.trc.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import sun.security.krb5.internal.Ticket;

import com.trc.domain.ticket.TicketNote;
import com.trc.user.User;
import com.trc.user.authority.Authority;

/**
 * Used for Activation logging testing
 * 
 */

@Deprecated
public class HibernateUtil_Ticket {

	private static SessionFactory sessionFactory;

	public static Configuration getInitializedConfiguration() {
		Configuration config = new Configuration();
		config.addAnnotatedClass(Ticket.class);
		config.addAnnotatedClass(User.class);
		config.addAnnotatedClass(TicketNote.class);
		config.addAnnotatedClass(Authority.class);
		config.configure();
		return config;
	}

	public static Session getSession() {
		if (sessionFactory == null) {
			Configuration config = HibernateUtil_Ticket.getInitializedConfiguration();
			sessionFactory = config.buildSessionFactory();
		}
		Session hibernateSession = sessionFactory.getCurrentSession();
		return hibernateSession;
	}

}