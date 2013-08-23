package com.tscp.mvna.domain.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 4 implementation for future use
 * 
 * @author Jonathan
 * 
 */
public class PreparedQuery extends Dao {
	protected static final Logger logger = LoggerFactory.getLogger(PreparedQuery.class);

	private static Query buildQuery(
			Session session, String queryName, Object... args) {

		Query query = session.getNamedQuery(queryName);
		String[] parameters = query.getNamedParameters();

		if (args.length != parameters.length)
			throw new HibernateException("Number of arguments does not match number of parameters");

		List<String> sortedParams = new ArrayList<String>();
		for (String p : parameters)
			sortedParams.add(p);
		Collections.sort(sortedParams);

		for (int i = 0; i < args.length; i++)
			query.setParameter(sortedParams.get(i), args[i]);

		return query;
	}

	@SuppressWarnings("rawtypes")
	public static List list(
			String queryName, Object... args) {

		Session session = getSession();
		Transaction tx = session.beginTransaction();

		List result = null;

		try {
			profiler.start(queryName);
			result = buildQuery(session, queryName, args).list();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing named query {}: {} : {}", queryName, e.getMessage(), e.getCause());
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;
	}

	public static Object[] listScalar(
			String queryName, Object... args) {

		Session session = getSession();
		Transaction tx = session.beginTransaction();

		Object[] result = null;

		try {
			profiler.start(queryName);
			result = (Object[]) buildQuery(session, queryName, args).uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing named query {}: {} : {}", queryName, e.getMessage(), e.getCause());
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;
	}

	public static Object uniqueResult(
			String queryName, Object... args) {

		Session session = getSession();
		Transaction tx = session.beginTransaction();

		Object result = null;

		try {
			profiler.start(queryName);
			result = buildQuery(session, queryName, args).uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing named query {}: {} : {}", queryName, e.getMessage(), e.getCause());
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;
	}

	public static int executeUpdate(
			String queryName, Object... args) {

		Session session = getSession();
		Transaction tx = session.beginTransaction();

		int result = -1;

		try {
			profiler.start(queryName);
			result = buildQuery(session, queryName, args).executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing named query {}: {} : {}", queryName, e.getMessage(), e.getCause());
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;
	}

}