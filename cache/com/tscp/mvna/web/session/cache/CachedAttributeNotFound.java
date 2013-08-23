package com.tscp.mvna.web.session.cache;

public class CachedAttributeNotFound extends RuntimeException {
	private static final long serialVersionUID = 8221295786293344272L;

	public CachedAttributeNotFound() {
		super();
	}

	public CachedAttributeNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CachedAttributeNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public CachedAttributeNotFound(String message) {
		super(message);
	}

	public CachedAttributeNotFound(Throwable cause) {
		super(cause);
	}

}
