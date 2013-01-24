package com.trc.web.security.authentication;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.trc.config.Config;
import com.trc.exception.management.DeviceManagementException;
import com.trc.manager.DeviceManager;
import com.trc.manager.UserManager;
import com.trc.security.encryption.StringEncrypter;
import com.trc.user.AnonymousUser;
import com.trc.user.User;
import com.trc.util.logger.DevLogger;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.tscp.mvne.Device;

public class MySavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private UserManager userManager;
	@Autowired
	private DeviceManager deviceManager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		User user = userManager.getLoggedInUser();
		userManager.getUserRealName(user);
		MDC.put("sessionId", SessionManager.getCurrentSessionId());
		// if (Config.ADMIN && user.isSuperUser()) {
		// MDC.put("internalUser", user.getUsername());
		// userManager.setSessionAdmin(user);
		// userManager.setSessionUser(new AnonymousUser());
		// setAlwaysUseDefaultTargetUrl(true);
		// setDefaultTargetUrl("/admin/home");
		// } else if (Config.ADMIN && user.isAdmin()) {
		// MDC.put("internalUser", user.getUsername());
		// userManager.setSessionAdmin(user);
		// userManager.setSessionUser(new AnonymousUser());
		// setAlwaysUseDefaultTargetUrl(true);
		// setDefaultTargetUrl("/admin/home");
		// } else if (Config.ADMIN && user.isManager()) {
		// MDC.put("internalUser", user.getUsername());
		// userManager.setSessionManager(user);
		// userManager.setSessionUser(new AnonymousUser());
		// setAlwaysUseDefaultTargetUrl(true);
		// setDefaultTargetUrl("/manager/home");
		// } else if (Config.ADMIN && user.isServiceRep()) {
		// MDC.put("internalUser", user.getUsername());
		// userManager.setSessionManager(user);
		// userManager.setSessionUser(new AnonymousUser());
		// setAlwaysUseDefaultTargetUrl(true);
		// setDefaultTargetUrl("/servicerep/home");
		if (Config.ADMIN && user.isInternalUser()) {
			MDC.put("internalUser", user.getUsername());
			userManager.setSessionControllingUser(user);
			userManager.setSessionUser(new AnonymousUser());
			if (user.isAdmin()) {
				setDefaultTargetUrl("/admin/home");
			} else if (user.isManager()) {
				setDefaultTargetUrl("/manager/home");
			} else if (user.isServiceRep()) {
				setDefaultTargetUrl("/servicerep/home");
			} else if (user.isSuperUser()) {
				setDefaultTargetUrl("/it/home");
			}
		} else {
			MDC.put("username", user.getUsername());
			userManager.setSessionUser(user);

			String targetUrl = "/";
			boolean alwaysUseDefaultTarget = false;
			boolean hasDevice = false;

			if (!user.isAdmin()) {
				try {
					List<Device> devices = deviceManager.getDeviceInfoList(user);
					hasDevice = devices != null && !devices.isEmpty();
					targetUrl = hasDevice ? "/account" : "/start";
					alwaysUseDefaultTarget = !hasDevice;
				} catch (DeviceManagementException e) {
					targetUrl = "/start";
					alwaysUseDefaultTarget = false;
				}
			}
			
			DevLogger.debug("target URL is " + targetUrl);
			DevLogger.debug("always use default target? " + alwaysUseDefaultTarget);
			
			setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTarget);
			setDefaultTargetUrl(targetUrl);
		}

		SessionManager.set(SessionKey.ENCRYPTER, new StringEncrypter(SessionManager.getCurrentSessionId()));
		super.onAuthenticationSuccess(request, response, authentication);
	}

}