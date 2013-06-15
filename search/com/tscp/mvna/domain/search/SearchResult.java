package com.tscp.mvna.domain.search;

public class SearchResult {
	private int id;
	private String label;
	private String description;

	public SearchResult(int id, String label, String description) {
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(
			int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(
			String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(
			String description) {
		this.description = description;
	}

}