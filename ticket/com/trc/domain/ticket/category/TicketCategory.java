package com.trc.domain.ticket.category;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ticket_category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TicketCategory implements Serializable {
	private static final long serialVersionUID = 8925478412956138233L;
	protected int id;
	protected TicketCategory parentCategory;
	protected String description;
	protected List<TicketCategory> subcategories;

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	public TicketCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(
			TicketCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(
			String dsecription) {
		this.description = dsecription;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory")
	public List<TicketCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(
			List<TicketCategory> subcategories) {
		this.subcategories = subcategories;
	}

	@Override
	public String toString() {
		String parent = parentCategory != null ? ", parent=" + parentCategory.getDescription() : "";
		String subcat = subcategories != null ? ", subcategories=" + subcategories.size() : "";
		return "TicketCategory [id=" + id + ", desc=" + description + parent + subcat + "]";
	}

}