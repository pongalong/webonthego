package com.tscp.mvna.domain.registration.affiliate;

import com.trc.user.User;
import com.tscp.mvna.domain.affiliate.SourceCode;
import com.tscp.mvna.domain.registration.Registration;

public class AffiliateRegistration extends Registration {
	private static final long serialVersionUID = 2217084928095696862L;
	private SourceCode sourceCode = new SourceCode();
	private User createdBy;

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(
			SourceCode sourceCode) {
		this.sourceCode = sourceCode;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(
			User createdBy) {
		this.createdBy = createdBy;
	}

}