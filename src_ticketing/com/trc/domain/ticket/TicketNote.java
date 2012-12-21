package com.trc.domain.ticket;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trc.user.User;

@Entity
@Table(name = "ticket_note")
public class TicketNote implements Serializable {
	private static final long serialVersionUID = 6952675718201666555L;
	private int id;
	private String note;
	private User creator;
	private Ticket ticket;
	private Date date = new Date();

	@Id
	@Column(name = "ticket_note_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "note", nullable = true, insertable = true, updatable = true)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ticket_id")
	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Column(name = "created_date")
	// @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Transient
	public static void sync(TicketNote note, TicketNote sessionNote) {
		note.setId(sessionNote.getId());
		note.setCreator(sessionNote.getCreator());
		note.setDate(sessionNote.getDate());
		note.setTicket(sessionNote.getTicket());
		sessionNote.setNote(note.getNote());
	}

	@Override
	public String toString() {
		return "TicketNote [id=" + id + ", note=" + note + ", creator=" + creator + ", ticket=" + ticket + ", date=" + date + "]";
	}

}
