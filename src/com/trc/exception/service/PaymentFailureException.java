package com.trc.exception.service;

public class PaymentFailureException extends PaymentServiceException {
	private static final long serialVersionUID = 5197203267844444323L;

	private String response;
	private String authCode;
	private String message;

	public PaymentFailureException(Exception e) {
		parseExceptionMessage(e);
	}

	protected void parseExceptionMessage(
			Exception e) {

		String[] parsed = e.getMessage().split("\\*");

		for (int i = 1; i < parsed.length; i++) {
			String s = parsed[i];
			int marker = s.indexOf(":");

			if (s.isEmpty())
				continue;

			String label = s.substring(0, marker + 1).trim();
			s = s.replace(label, "");

			switch (label.toUpperCase()) {
				case "RESPONSE:":
					response = s.trim();
				case "AUTHCODE:":
					authCode = s.trim();
				case "ADDITIONAL:":
					message = s.trim();
			}
		}

	}

	public String getResponse() {
		return response;
	}

	public void setResponse(
			String response) {
		this.response = response;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(
			String authCode) {
		this.authCode = authCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(
			String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PaymentFailureException [response=" + response + ", authCode=" + authCode + ", message=" + message + "]";
	}

}