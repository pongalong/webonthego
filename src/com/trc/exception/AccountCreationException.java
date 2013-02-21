package com.trc.exception;

import com.trc.exception.management.AccountManagementException;

public class AccountCreationException extends AccountManagementException {
	private static final long serialVersionUID = -8252187976048089088L;

	public AccountCreationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountCreationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AccountCreationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AccountCreationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
