package com.tscp.mvna.web.context.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

@Component("securityContextFacade")
public interface SecurityContextFacade {

	SecurityContext getContext();

	void setContext(
			SecurityContext securityContext);
}