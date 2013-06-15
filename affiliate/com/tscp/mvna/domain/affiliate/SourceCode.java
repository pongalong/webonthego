package com.tscp.mvna.domain.affiliate;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "affiliate_source_code")
public class SourceCode implements Serializable {
	private static final long serialVersionUID = 818243607810108303L;
	private int id;
	private String name;
	private String description;
	private String code;
	private SourceCode parent;
	private Collection<SourceCode> children;

	@Id
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(
			String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(
			String description) {
		this.description = description;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(
			String code) {
		this.code = code;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	public SourceCode getParent() {
		return parent;
	}

	public void setParent(
			SourceCode parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	public Collection<SourceCode> getChildren() {
		return children;
	}

	public void setChildren(
			Collection<SourceCode> children) {
		this.children = children;
	}

}