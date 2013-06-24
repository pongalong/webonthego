package com.tscp.mvna.form;

import java.io.Serializable;

public class FormField implements Serializable {
	private static final long serialVersionUID = -3015882738139766492L;
	public String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}