package com.trc.domain.support.knowledgebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "swkbarticles")
public class Article implements Serializable {
	private static final long serialVersionUID = 1495544695293668738L;
	private int id;
	private String subject;
	private int creatorid;
	private int views;
	private int hasattachments;
	private int dateline;
	private String articlestatus;
	private List<Category> categories = new ArrayList<Category>();
	private ArticleData articleData;

	public Article() {
		articleData = new ArticleData();
		articleData.setArticle(this);
	}

	@Id
	@Column(name = "kbarticleid", updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	@Column(name = "subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(
			String subject) {
		this.subject = subject;
	}

	@Column(name = "creatorid")
	public int getCreatorid() {
		return creatorid;
	}

	public void setCreatorid(
			int creatorid) {
		this.creatorid = creatorid;
	}

	@Column(name = "views")
	public int getViews() {
		return views;
	}

	public void setViews(
			int views) {
		this.views = views;
	}

	@Column(name = "hasattachments")
	public int getHasattachments() {
		return hasattachments;
	}

	public void setHasattachments(
			int hasattachments) {
		this.hasattachments = hasattachments;
	}

	@Column(name = "dateline")
	public int getDateline() {
		return dateline;
	}

	public void setDateline(
			int dateline) {
		this.dateline = dateline;
	}

	@Column(name = "articlestatus")
	public String getArticlestatus() {
		return articlestatus;
	}

	public void setArticlestatus(
			String articlestatus) {
		this.articlestatus = articlestatus;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "article_category", joinColumns = { @JoinColumn(name = "articleid") }, inverseJoinColumns = { @JoinColumn(name = "categoryid") })
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(
			List<Category> categories) {
		this.categories = categories;
	}

	@OneToOne(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(
			ArticleData articleData) {
		this.articleData = articleData;
	}

	/* ************************************************************************
	 * Helper Methods
	 */

	@Transient
	public void addCategory(
			Category category) {
		categories.add(category);
	}

}
