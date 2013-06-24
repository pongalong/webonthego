package com.tscp.mvna.domain.affiliate.manager.service.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.tscp.mvna.domain.affiliate.SourceCode;

@Repository
public class SourceCodeDao extends HibernateDaoSupport {

	@Autowired
	public void init(
			HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public int save(
			SourceCode sourceCode) {
		return (Integer) getHibernateTemplate().save(sourceCode);
	}

	public void update(
			SourceCode sourceCode) {
		getHibernateTemplate().update(sourceCode);
	}

	public void delete(
			SourceCode sourceCode) {
		getHibernateTemplate().delete(sourceCode);
	}

	public SourceCode get(
			int id) {
		return getHibernateTemplate().get(SourceCode.class, id);
	}

	public SourceCode getByCode(
			String code) {
		return (SourceCode) getHibernateTemplate().find("from SourceCode where code = ?", code).get(0);
	}

	public List<SourceCode> searchByCode(
			String code) {
		return getHibernateTemplate().find("from SourceCode where code like ?", wildcard(code));
	}

	public List<SourceCode> getAll() {
		return getHibernateTemplate().find("from SourceCode");
	}

	protected String wildcard(
			String value) {
		return "%" + value + "%";
	}

}
