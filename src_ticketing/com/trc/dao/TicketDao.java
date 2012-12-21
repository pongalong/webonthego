package com.trc.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketCategory;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;

@Repository
public class TicketDao extends HibernateDaoSupport {
	static final Logger logger = LoggerFactory.getLogger("devLogger");

	@Autowired
	public void init(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public Ticket getTicketById(int ticketId) {
		return (Ticket) getHibernateTemplate().get(Ticket.class, ticketId);
	}

	public List<Ticket> searchTickets(int custId, int creatorId, int assigneeId, TicketStatus status, TicketCategory category, TicketPriority priority,
			TicketType type, String title, String description) {
		List<Object> values = new ArrayList<Object>();
		boolean firstClause = true;
		String query = "from Ticket t";

		boolean hasUserParam = custId + creatorId + assigneeId > 0;
		boolean hasSelectors = (status != null && status != TicketStatus.NONE) || (category != null && category != TicketCategory.NONE)
				|| (priority != null && priority != TicketPriority.NONE) || (type != null && type != TicketType.NONE);
		boolean hasSearchWords = (title != null && !title.trim().isEmpty()) || (description != null && !description.trim().isEmpty());

		if (hasUserParam || hasSelectors || hasSearchWords) {
			query += " where ";
		}

		if (custId != 0) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.customerId = ?";
			values.add(custId);
		}

		if (creatorId != 0) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.creatorId = ?";
			values.add(creatorId);
		}

		if (assigneeId != 0) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.assigneeId = ?";
			values.add(assigneeId);
		}

		if (status != null && status != TicketStatus.NONE) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.status = ?";
			values.add(status);
		}

		if (category != null && category != TicketCategory.NONE) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.category = ?";
			values.add(category);
		}

		if (priority != null && priority != TicketPriority.NONE) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.priority = ?";
			values.add(priority);
		}

		if (type != null && type != TicketType.NONE) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.type = ?";
			values.add(type);
		}

		if (title != null && !title.trim().isEmpty()) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.title like ?";
			values.add("%" + title + "%");
		}

		if (description != null && !description.trim().isEmpty()) {
			if (!firstClause)
				query += " and ";
			firstClause = false;
			query += "t.description like ?";
			values.add("%" + description + "%");
		}

		return getHibernateTemplate().find(query, values.toArray());
	}

	public int save(Ticket ticket) {
		return (Integer) getHibernateTemplate().save(ticket);
	}

	public void update(Ticket ticket) {
		getHibernateTemplate().update(ticket);
	}

	public int save(TicketNote note) {
		return (Integer) getHibernateTemplate().save(note);
	}

	public void update(TicketNote note) {
		getHibernateTemplate().update(note);
	}

	public TicketNote getNoteById(int noteId) {
		return getHibernateTemplate().get(TicketNote.class, noteId);
	}

}
