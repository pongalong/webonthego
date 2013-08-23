package com.tscp.mvna.domain.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tscp.util.profiler.Profiler;

/**
 * Hibernate 4 implementation for future use
 * 
 * @author Jonathan
 * 
 */
public class Dao {
	private static final Logger logger = LoggerFactory.getLogger(Dao.class);
	protected static int maxOpenSessionCount;
	protected static int openSessionCount;

	public static int getOpenSessionCount() {
		return openSessionCount;
	}

	public static int getMaxOpenSessionCount() {
		return maxOpenSessionCount;
	}

	protected static void closeSession(
			Session session) {

		if (session != null && session.isOpen())
			session.close();
		openSessionCount--;
	}

	protected static Session getSession() {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			openSessionCount++;

			if (openSessionCount > maxOpenSessionCount)
				maxOpenSessionCount = openSessionCount;

			return session;
		} catch (HibernateException e) {
			logger.error("Error opening Hibernate Session", e);
			throw e;
		}
	}

	public static Object get(
			@SuppressWarnings("rawtypes") Class clazz, Serializable id) {

		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Loading object {} with ID {}", clazz.getSimpleName(), id);

			Object result = session.get(clazz, id);
			tx.commit();
			return result;
		} catch (HibernateException e) {
			logger.error("Error loading object {} with ID {}.", clazz.getSimpleName(), id, e);
			tx.rollback();
			return null;
		} finally {
			closeSession(session);
		}
	}

	public static Serializable save(
			Object obj) throws HibernateException {

		Session session = null;
		Transaction tx = null;
		Serializable result;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Saving object {}.", obj);

			result = session.save(obj);
			tx.commit();

		} catch (HibernateException e) {
			logger.error("Error saving object {}.", obj, e);
			tx.rollback();
			throw e;
		} finally {
			closeSession(session);
		}

		return result;
	}

	public static void delete(
			Object obj) throws HibernateException {

		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Saving object {}.", obj);

			session.delete(obj);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving object {}.", obj, e);
			tx.rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public static void saveOrUpdate(
			Object obj) throws HibernateException {

		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Saving or updating object {}.", obj);

			session.saveOrUpdate(obj);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving object {}.", obj, e);
			tx.rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public static void update(
			Object obj) throws HibernateException {

		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Updating object {}.", obj);

			session.update(obj);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error updating object {}.", obj, e);
			tx.rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public static void persist(
			Object obj) {

		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			logger.trace("Persisting object {}.", obj);

			session.persist(obj);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error persisting object {}.", obj, e);
			tx.rollback();
		} finally {
			closeSession(session);
		}
	}

	public static List findByCriteria(
			DetachedCriteria criteria) {

		Session session = null;
		Transaction tx = null;
		List result = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			result = criteria.getExecutableCriteria(session).list();

			tx.commit();

		} catch (HibernateException e) {
			tx.rollback();
		} finally {
			closeSession(session);
		}

		return result;
	}

	public static Object uniqueResult(
			String hqlString, Object... args) {

		Session session = null;
		Transaction tx = null;
		Object result = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			profiler.start(hqlString);
			Query query = buildQuery(session, hqlString, args);
			logger.debug("Executing HQL {}", query.getQueryString());
			result = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing {}", hqlString, e);
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;

	}

	@SuppressWarnings("rawtypes")
	public static List list(
			String hqlString) {
		return list(hqlString, (Object[]) null);
	}

	@SuppressWarnings("rawtypes")
	public static List list(
			String hqlString, Object... args) {

		Session session = null;
		Transaction tx = null;
		List result = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			profiler.start(hqlString);
			Query query = buildQuery(session, hqlString, args);
			logger.debug("Executing HQL {}", query.getQueryString());
			result = query.list();
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error executing {}", hqlString, e);
			tx.rollback();
		} finally {
			closeSession(session);
			profiler.stop();
		}

		return result;
	}

	private static Query buildQuery(
			Session session, String hqlString, Object... args) {
		Query query = session.createQuery(hqlString);
		if (args != null)
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		return query;
	}

	/* **************************************************
	 * Profiler Methods
	 */

	protected static Profiler profiler = new Profiler();

	public static Profiler getProfiler() {
		return profiler;
	}

}