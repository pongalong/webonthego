package com.trc.web.security.authentication;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.trc.config.Config;
import com.trc.exception.management.AccountManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.UserManager;
import com.trc.security.encryption.SessionEncrypter;
import com.trc.security.encryption.StringEncrypter;
import com.trc.user.NoControllingUser;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.PaymentHistory;
import com.trc.web.session.SessionKey;
import com.trc.web.session.SessionManager;
import com.tscp.util.logger.DevLogger;

public class MySavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		User user = userManager.getLoggedInUser();

		if (!user.isInternalUser())
			userManager.getUserRealName(user);

		setSessionAttributes(user);

		if (Config.ADMIN && user.isInternalUser()) {
			setAdminDefaultTargetUrl(user);
		} else {
			setUserDefaultTargetUrl(user);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void setSessionAttributes(
			User user) {

		StringEncrypter encrypter = new StringEncrypter(SessionManager.getCurrentSessionId());

		SessionManager.set(SessionKey.ENCRYPTER, encrypter);

		if (Config.ADMIN && user.isInternalUser()) {
			SessionManager.set("controlling_user", user);
			SessionManager.set("user", null);
		} else {
			SessionManager.set("controlling_user", new NoControllingUser());
			SessionManager.set("user", user);
		}

		try {
			List<AccountDetail> accountDetails = accountManager.getAllAccountDetails(user);
			for (AccountDetail ad : accountDetails) {
				ad.setEncodedAccountNum(SessionEncrypter.encryptId(ad.getAccount().getAccountNo()));
				ad.setEncodedDeviceId(SessionEncrypter.encryptId(ad.getDeviceInfo().getId()));
			}
			SessionManager.set("accountDetails", accountDetails);
		} catch (AccountManagementException e) {
			DevLogger.debug("Exception fetching account details at login", e);
		}

		try {
			PaymentHistory paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
			SessionManager.set("paymentHistory", paymentHistory);
		} catch (AccountManagementException e) {
			DevLogger.debug("Exception fetching paymentHistory at login", e);
		}
	}

	private void setAdminDefaultTargetUrl(
			User user) {

		setAlwaysUseDefaultTargetUrl(true);

		if (user.isAdmin()) {
			setDefaultTargetUrl("/admin/home");
		} else if (user.isManager()) {
			setDefaultTargetUrl("/manager/home");
		} else if (user.isServiceRep()) {
			setDefaultTargetUrl("/servicerep/home");
		} else if (user.isSuperUser()) {
			setDefaultTargetUrl("/it/home");
		}
	}

	private void setUserDefaultTargetUrl(
			User user) {

		String targetUrl = "/";
		boolean hasDevice = false;

		List<AccountDetail> accountDetails = (List<AccountDetail>) SessionManager.get("accountDetails");
		hasDevice = accountDetails != null && !accountDetails.isEmpty();
		targetUrl = hasDevice ? "/account" : "/start";

		setAlwaysUseDefaultTargetUrl(!hasDevice);
		setDefaultTargetUrl(targetUrl);
	}

}