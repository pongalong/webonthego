package com.trc.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trc.dao.UserDao;
import com.trc.exception.management.AccountManagementException;
import com.trc.user.EmptyUser;
import com.trc.user.User;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.web.context.SecurityContextFacade;
import com.trc.web.session.SessionKey;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvna.service.gateway.WebserviceGateway;
import com.tscp.mvne.Account;
import com.tscp.mvne.CustInfo;
import com.tscp.mvne.TSCPMVNA;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Service
public class UserManager implements UserManagerModel {
	public static SecurityContextFacade securityContext;
	private UserDao userDao;
	private AccountManager accountManager;
	private CacheManager cacheManager;
	private TSCPMVNA port;

	@Autowired
	public void init(
			UserDao userDao, AccountManager accountManager, CacheManager cacheManager, SecurityContextFacade securityContextFacade, WebserviceGateway gateway) {
		this.userDao = userDao;
		this.accountManager = accountManager;
		this.cacheManager = cacheManager;
		this.port = gateway.getPort();
		securityContext = securityContextFacade;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsersWithRole(
			ROLE role) {
		return userDao.getAllUsersWithRole(role);
	}

	@Transactional(readOnly = true)
	public List<User> getAllInternalUsers() {
		return userDao.getAllUsersWithRole(ROLE.getInternalRoles());
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserByEmail(
			String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserByUsername(
			String username) {
		return userDao.getUserByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserById(
			int id) {
		return userDao.getUserById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> searchByEmail(
			String email) {
		return userDao.searchByEmail(email);
	}

	@Transactional(readOnly = true)
	public List<User> searchCustomersByEmail(
			String email) {
		return userDao.searchCustomersByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> searchById(
			int id) {
		return userDao.searchById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User searchByAccountNo(
			int accountNo) {
		Account account = port.getAccountInfo(accountNo);
		List<User> users = searchByEmail(account.getContactEmail());
		return users != null && users.size() > 0 ? users.get(0) : new User();
	}

	@Transactional(readOnly = true)
	public List<User> searchByEmailAndDate(
			String email, DateTime startDate, DateTime endDate) {
		return userDao.searchByEmailAndDate(email, startDate, endDate);
	}

	@Deprecated
	@Transactional(readOnly = true)
	public List<User> searchByNotEmailAndDate(
			String email, DateTime startDate, DateTime endDate) {
		return userDao.searchByNotEmailAndDate(email, startDate, endDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> searchByUsername(
			String username) {
		return userDao.searchByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> search(
			String param) {
		return userDao.search(param);
	}

	@Override
	public User getLoggedInUser() {
		Authentication authentication = securityContext.getContext().getAuthentication();
		boolean isAnon1 = authentication == null || !(authentication.getPrincipal() instanceof UserDetails);
		return isAnon1 ? new EmptyUser() : (User) authentication.getPrincipal();
	}

	@Override
	@Transactional(readOnly = false)
	public Serializable saveUser(
			User user) {
		if (user.getAuthorities().isEmpty()) {
			user.getRoles().add(new Authority(user, ROLE.ROLE_USER));
		}
		return userDao.saveUser(user);
	}

	@Override
	@Transactional
	public void saveOrUpdateUser(
			User user) {
		userDao.saveOrUpdateUser(user);
	}

	@Override
	@Transactional
	public void persistUser(
			User user) {
		userDao.persistUser(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(
			User user) {
		userDao.deleteUser(user);
	}

	@Override
	@Transactional(readOnly = false)
	// commented out to allow for password resets
	// @PreAuthorize("isAuthenticated() and hasPermission(#user, 'canUpdate')")
	public void updateUser(
			User user) {
		userDao.updateUser(user);
	}

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_MANAGER','isAtleast')")
	public void enableUser(
			User user) {
		user.setEnabled(true);
		updateUser(user);
	}

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("isAuthenticated() and hasPermission('ROLE_MANAGER','isAtleast')")
	public void disableUser(
			User user) {
		user.setEnabled(false);
		user.setDateDisabled(new Date());
		updateUser(user);
	}

	@Override
	public boolean isUsernameAvailable(
			String username) {
		return getUserByUsername(username) == null;
	}

	@Override
	public boolean isEmailAvailable(
			String email) {
		return getUserByEmail(email) == null;
	}

	@Loggable(value = LogLevel.TRACE)
	public void getUserRealName(
			User user) {

		if (user.isInternalUser()) {
			user.getContactInfo().setFirstName(user.getUsername());
			user.getContactInfo().setLastName(user.getGreatestAuthority().getRole().toString());
		} else if (user.isUser()) {
			try {
				CustInfo custInfo = accountManager.getCustInfo(user);
				if (custInfo != null) {
					user.getContactInfo().setFirstName(custInfo.getFirstName());
					user.getContactInfo().setLastName(custInfo.getLastName());
				} else {
					user.getContactInfo().setFirstName(user.getUsername());
				}
			} catch (AccountManagementException e) {
				user.getContactInfo().setFirstName(user.getUsername());
			}
		} else {
			user.getContactInfo().setFirstName(user.getUsername());
		}
	}

	/* ************************************************************************************************
	 * Session Helper Methods
	 * ************************************************************************************************
	 */

	@Override
	public User getCurrentUser() {
		User user = (User) CacheManager.get(SessionKey.USER);
		return user == null ? new EmptyUser() : user;
	}

	public void setCurrentUser(
			User user) {
		CacheManager.set(SessionKey.USER, user);
	}

	public User getControllingUser() {
		return (User) CacheManager.get(SessionKey.CONTROLLING_USER);
	}

	public void setControllingUser(
			User user) {
		CacheManager.set(SessionKey.CONTROLLING_USER, user);
	}

	/* ************************************************************************************************
	 * Login Helper Methods
	 * ************************************************************************************************
	 */

	@PreAuthorize("hasPermission('ROLE_ADMIN', 'isAtleast')")
	public void forceLogout(
			SessionRegistry sessionRegistry, int userId) {

		List<Object> activePrincipals = sessionRegistry.getAllPrincipals();

		for (Object principal : activePrincipals)
			if (((User) principal).getUserId() == userId) {
				List<SessionInformation> sessionInfo = sessionRegistry.getAllSessions(((User) principal), false);
				for (SessionInformation si : sessionInfo)
					si.expireNow();
				break;
			}
	}

	public void autoLogin(
			User user, AuthenticationManager authenticationManager) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		autoLogin(user, request, authenticationManager);
	}

	public void autoLogin(
			User user, HttpServletRequest request, AuthenticationManager authenticationManager) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

		// generate session if one doesn't exist
		request.getSession();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authenticatedUser = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

		// setting role to the session
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

		cacheManager.beginSession(user);
	}

}