package com.trc.domain.ticket;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.trc.domain.ticket.category.TicketCategory;

@Entity
@Table(name = "TICKET")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class Ticket implements Serializable {
	private static final long serialVersionUID = -1651076728127823433L;

	protected int id;
	protected TicketType type = TicketType.NONE;
	protected TicketPriority priority = TicketPriority.NORMAL;
	protected TicketStatus status = TicketStatus.OPEN;
	protected String title;
	protected TicketCategory category;
	protected String description;
	protected List<TicketNote> notes;
	protected Date createdDate = new Date();
	protected Date lastModifiedDate;

	
	@Id
	@Column(name = "ticket_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	@Transient
	// @Column(name = "type") @Enumerated(EnumType.STRING)
	public TicketType getType() {
		return type;
	}

	public void setType(
			TicketType type) {
		this.type = type;
	}

	@Column(name = "priority")
	@Enumerated(EnumType.STRING)
	public TicketPriority getPriority() {
		return priority;
	}

	public void setPriority(
			TicketPriority priority) {
		this.priority = priority;
	}

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(
			TicketStatus status) {
		this.status = status;
	}

	@Column(name = "created_date")
	// @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(
			Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "last_modified_date")
	// @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(
			Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(
			String title) {
		this.title = title;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(
			String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ticket_category")
	public TicketCategory getCategory() {
		return category;
	}

	public void setCategory(
			TicketCategory category) {
		this.category = category;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket")
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<TicketNote> getNotes() {
		return notes;
	}

	public void setNotes(
			List<TicketNote> notes) {
		this.notes = notes;
	}

	/**
	 * Syncs the data between two Ticket objects. Usually the one stored in session and the one created for the model.
	 * 
	 * @param modelTicket
	 * @param sessionTicket
	 * @return
	 */
	@Transient
	public static Ticket sync(
			Ticket modelTicket, Ticket sessionTicket) {
		sessionTicket.setCategory(modelTicket.getCategory());
		sessionTicket.setDescription(modelTicket.getDescription());
		sessionTicket.setNotes(modelTicket.getNotes());
		sessionTicket.setPriority(modelTicket.getPriority());
		sessionTicket.setStatus(modelTicket.getStatus());
		sessionTicket.setTitle(modelTicket.getTitle());
		return sessionTicket;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", type=" + type + ", priority=" + priority + ", category=" + category.getDescription() + ", status=" + status + ", title=" + title + ", description=" + description + "]";
	}

}
//