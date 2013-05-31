package com.trc.domain.support.knowledgebase;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "swkbarticledata")
public class ArticleData implements Serializable {
	private static final long serialVersionUID = 6932933288755845769L;
	private int id;
	private Article article;
	private String contents;
	private String contentsText;

	@Id
	@Column(name = "kbarticledataid", updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(
			Integer id) {
		this.id = id;
	}

	@Column(name = "contents")
	public String getContents() {
		return contents;
	}

	public void setContents(
			String contents) {
		this.contents = contents;
	}

	@Column(name = "contentsText")
	public String getContentsText() {
		return contentsText;
	}

	public void setContentsText(
			String contentsText) {
		this.contentsText = contentsText;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "kbarticleid")
	public Article getArticle() {
		return article;
	}

	public void setArticle(
			Article article) {
		this.article = article;
	}

}
