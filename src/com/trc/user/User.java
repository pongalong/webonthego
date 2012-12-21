package com.trc.user;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.trc.manager.UserManager;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.user.contact.ContactInfo;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements UserModel, UserDetails {
	private static final long serialVersionUID = 1L;
	private int userId;
	private String username;
	private String password;
	private String email;
	private Date dateEnabled;
	private Date dateDisabled;
	private boolean enabled;
	private SecurityQuestionAnswer userHint = new SecurityQuestionAnswer();
	private Collection<Authority> authorities = new HashSet<Authority>();
	private ContactInfo contactInfo = new ContactInfo();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	@Column(name = "username")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	@Column(name = "enabled", columnDefinition = "BOOLEAN")
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "date_enabled")
	public Date getDateEnabled() {
		return this.dateEnabled;
	}

	public void setDateEnabled(Date dateEnabled) {
		this.dateEnabled = dateEnabled;
	}

	@Column(name = "date_disabled")
	public Date getDateDisabled() {
		return this.dateDisabled;
	}

	public void setDateDisabled(Date dateDisabled) {
		this.dateDisabled = dateDisabled;
	}

	@Embedded
	public SecurityQuestionAnswer getSecurityQuestionAnswer() {
		return this.userHint;
	}

	public void setSecurityQuestionAnswer(SecurityQuestionAnswer securityQuestionAnswer) {
		this.userHint = securityQuestionAnswer;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	@Cascade({ CascadeType.ALL })
	public Collection<Authority> getRoles() {
		return this.authorities;
	}

	public void setRoles(Collection<Authority> roles) {
		this.authorities = roles;
	}

	/*****************************************************************************
	 * Begins Spring UserDetails implementation methods // TODO User class should
	 * not implement UserDetails, instead we should create another class that uses
	 * Assembler to keep our design separate from Spring.
	 *****************************************************************************/

	private Collection<GrantedAuthority> grantedAuthorities;

	@Transient
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		if (grantedAuthorities == null) {
			grantedAuthorities = new HashSet<GrantedAuthority>();
			for (Authority authority : getRoles()) {
				grantedAuthorities.add(new GrantedAuthorityImpl(authority.getRole().toString()));
			}
		}
		return grantedAuthorities;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Transient
	public boolean isInternalUser() {
		return isSuperUser() || isAdmin() || isManager() || isServiceRep();
	}

	@Transient
	public boolean isSuperUser() {
		return getRoles().contains(new Authority(ROLE.ROLE_SUPERUSER));
	}

	@Transient
	public boolean isAdmin() {
		// GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_ADMIN");
		// return getAuthorities().contains(ga);
		return getRoles().contains(new Authority(ROLE.ROLE_ADMIN));
	}

	@Transient
	public boolean isManager() {
		// GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_MANAGER");
		// return getAuthorities().contains(ga);
		return getRoles().contains(new Authority(ROLE.ROLE_MANAGER));
	}

	@Transient
	public boolean isServiceRep() {
		// GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_SERVICEREP");
		// return getAuthorities().contains(ga);
		return getRoles().contains(new Authority(ROLE.ROLE_SERVICEREP));
	}

	@Transient
	public boolean isUser() {
		// GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_USER");
		// return getAuthorities().contains(ga);
		return getRoles().contains(new Authority(ROLE.ROLE_USER));
	}

	private static final Logger logger = LoggerFactory.getLogger("devLogger");

	@Transient
	public boolean isAuthenticated() {
		GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_ANONYMOUS");
		boolean origCheck = !UserManager.securityContext.getContext().getAuthentication().getAuthorities().contains(ga) && !getAuthorities().contains(ga);
		Authentication authentication = UserManager.securityContext.getContext().getAuthentication();
		boolean newCheck = authentication != null && authentication.getPrincipal() instanceof UserDetails;
		logger.debug("origCheck: {} newCheck: {}", origCheck, newCheck);
		return origCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	@Transient
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email + ", dateEnabled=" + dateEnabled
				+ ", dateDisabled=" + dateDisabled + ", enabled=" + enabled + ", userHint=" + userHint + ", authorities=" + authorities + ", contactInfo="
				+ contactInfo + "]";
	}

}