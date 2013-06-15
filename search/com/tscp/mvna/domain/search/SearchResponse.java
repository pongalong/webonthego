package com.tscp.mvna.domain.search;

public class SearchResponse {
	private Object[] results;
	private boolean success;

	public SearchResponse(boolean success, Object[] results) {
		this.success = success;
		this.results = results;
	}

	public Object[] getResults() {
		return results;
	}

	public boolean isSuccess() {
		return success;
	}

}