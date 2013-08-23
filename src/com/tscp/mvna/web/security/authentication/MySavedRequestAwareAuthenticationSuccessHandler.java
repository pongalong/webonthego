package com.tscp.mvna.web.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.trc.manager.AccountManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetailCollection;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.tscp.mvna.config.Config;
import com.tscp.mvna.web.session.SessionManager;
import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

public class MySavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(MySavedRequestAwareAuthenticationSuccessHandler.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private SessionManager sessionManager;

	@Loggable(value = LogLevel.INFO)
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		User user = userManager.getLoggedInUser();

		logger.trace("Authentication successful, " + user.getUsername() + " is now logged in.");

		if (!user.isInternalUser())
			userManager.getUserRealName(user);

		sessionManager.createSession(user);
		cacheManager.createCache(user);

		if (Config.ADMIN && user.isInternalUser())
			setAdminDefaultTargetUrl(user);
		else
			setUserDefaultTargetUrl(user);

		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void setAdminDefaultTargetUrl(
			User user) {

		setAlwaysUseDefaultTargetUrl(true);

		Authority authority = user.getGreatestAuthority();

		if (authority.compare(ROLE.ROLE_MANAGER) >= 0)
			setDefaultTargetUrl("/admin/home");
		else if (authority.compare(ROLE.ROLE_AGENT) == 0) {
			setDefaultTargetUrl("/support/ticket");
		} else if (authority.compare(ROLE.ROLE_SALES) == 0)
			setDefaultTargetUrl("/sales/home");
		else
			setDefaultTargetUrl("/home");
	}

	private void setUserDefaultTargetUrl(
			User user) {

		String targetUrl = "/";
		boolean hasDevice = false;

		AccountDetailCollection accountDetailSet = (AccountDetailCollection) cacheManager.fetch(new AccountDetailCollection());
		hasDevice = accountDetailSet != null && !accountDetailSet.isEmpty();
		targetUrl = hasDevice ? "/account" : "/start";

		setAlwaysUseDefaultTargetUrl(!hasDevice);
		setDefaultTargetUrl(targetUrl);
	}
}