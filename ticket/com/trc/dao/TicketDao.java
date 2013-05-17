package com.trc.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.trc.domain.ticket.AdminTicket;
import com.trc.domain.ticket.AgentTicket;
import com.trc.domain.ticket.CustomerTicket;
import com.trc.domain.ticket.InquiryTicket;
import com.trc.domain.ticket.SearchTicket;
import com.trc.domain.ticket.Ticket;
import com.trc.domain.ticket.TicketNote;
import com.trc.domain.ticket.TicketPriority;
import com.trc.domain.ticket.TicketStatus;
import com.trc.domain.ticket.TicketType;
import com.trc.domain.ticket.category.TicketCategory;

@Repository
public class TicketDao extends HibernateDaoSupport {

	@Autowired
	public void init(
			HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public Ticket getTicketById(
			int ticketId) {
		return (Ticket) getHibernateTemplate().get(Ticket.class, ticketId);
	}

	public List<Ticket> searchTickets(
			SearchTicket ticket) {
		return searchTickets(ticket.getCustomerId(), ticket.getCreatorId(), ticket.getAssigneeId(), ticket.getStatus(), ticket.getCategory(), ticket.getPriority(), ticket.getType(), ticket.getTitle(), ticket.getDescription());
	}

	public List<Ticket> searchTickets(
			int custId, int creatorId, int assigneeId, TicketStatus status, TicketCategory category, TicketPriority priority, TicketType type, String title, String description) {

		DetachedCriteria criteria;

		switch (type) {
			case INQUIRY:
				criteria = DetachedCriteria.forClass(InquiryTicket.class);
				break;
			case CUSTOMER:
				criteria = DetachedCriteria.forClass(CustomerTicket.class);
				break;
			case AGENT:
				criteria = DetachedCriteria.forClass(AgentTicket.class);
				break;
			case ADMIN:
				criteria = DetachedCriteria.forClass(AdminTicket.class);
				break;
			default:
				criteria = DetachedCriteria.forClass(Ticket.class);
				break;
		}

		if (custId > 0)
			criteria.add(Restrictions.eq("customerId", custId));
		if (creatorId != 0)
			criteria.add(Restrictions.eq("creatorId", creatorId));
		if (assigneeId != 0)
			criteria.add(Restrictions.eq("assigneeId", assigneeId));
		if (status != null && status != TicketStatus.NONE)
			criteria.add(Restrictions.eq("status", status));
		if (category != null && category.getId() > 0)
			criteria.add(Restrictions.eq("category.id", category.getId()));
		if (priority != null && priority != TicketPriority.NONE)
			criteria.add(Restrictions.eq("priority", priority));
		if (title != null && !title.trim().isEmpty())
			criteria.add(Restrictions.like("title", "%" + title + "%"));
		if (description != null && !description.trim().isEmpty())
			criteria.add(Restrictions.like("description", "%" + description + "%"));

		criteria.addOrder(Order.desc("id"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public int save(
			Ticket ticket) {
		return (Integer) getHibernateTemplate().save(ticket);
	}

	public int save(
			InquiryTicket ticket) {
		return (Integer) getHibernateTemplate().save(ticket);
	}

	public void update(
			Ticket ticket) {
		getHibernateTemplate().update(ticket);
	}

	public int save(
			TicketNote note) {
		return (Integer) getHibernateTemplate().save(note);
	}

	public void update(
			TicketNote note) {
		getHibernateTemplate().update(note);
	}

	public TicketNote getNoteById(
			int noteId) {
		return getHibernateTemplate().get(TicketNote.class, noteId);
	}

	public List<TicketCategory> getTicketCategories(
			boolean root, boolean category) {

		String query = "from TicketCategory";
		if (root && category) {
			query += " order by ID asc";
		} else if (root) {
			query += " where parentCategory is null order by ID asc";
		} else if (category) {
			query += " where parentCategory is not null order by parentCategory, ID asc";
		}

		return getHibernateTemplate().find(query);
	}
}