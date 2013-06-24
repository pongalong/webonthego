package com.tscp.mvna.form;

public class ConfirmableFormField extends FormField {
	private static final long serialVersionUID = -6816532005616801187L;
	public String confirmValue;

	public String getConfirmValue() {
		return confirmValue;
	}

	public void setConfirmValue(String confirmValue) {
		this.confirmValue = confirmValue;
	}

	public boolean confirm() {
		return value != null && value.equals(confirmValue);
	}
}