package com.tscp.mvna.domain.dao;

import org.hibernate.SessionFactory;

/**
 * Hibernate 4 implementation for future use
 * 
 * @author Jonathan
 * 
 */
public abstract class HibernateUtil {
	// protected static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	// protected static final Configuration configuration = buildConfiguration();
	// protected static final ServiceRegistry serviceRegistry = buildServiceRegistry();
	// protected static final SessionFactory sessionFactory = buildSessionFactory();
	protected static SessionFactory sessionFactory;

	// private static final Configuration buildConfiguration() {
	// try {
	// return new Configuration().configure("hibernate.cfg.xml");
	// } catch (HibernateException e) {
	// logger.error("Initial Hibernate Configuration creation failed", e);
	// throw new ExceptionInInitializerError(e);
	// }
	// }
	//
	// private static final ServiceRegistry buildServiceRegistry() {
	// return new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
	// }
	//
	// private static final SessionFactory buildSessionFactory() {
	// try {
	// return configuration.buildSessionFactory(serviceRegistry);
	// } catch (Throwable e) {
	// logger.error("Initial SessionFactory creation failed", e);
	// e.printStackTrace();
	// throw new ExceptionInInitializerError(e);
	// }
	// }

	public static final SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}