package com.trc.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.trc.manager.UserManager;
import com.trc.user.authority.Authority;
import com.trc.user.authority.ROLE;
import com.trc.user.contact.ContactInfo;
import com.tscp.mvna.domain.affiliate.SourceCode;
import com.tscp.mvna.web.session.SessionObject;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements UserModel, UserDetails, SessionObject {
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

	public void setUserId(
			int userId) {
		this.userId = userId;
	}

	@Override
	@Column(name = "username")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(
			String username) {
		this.username = username;
	}

	@Override
	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(
			String password) {
		this.password = password;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(
			String email) {
		this.email = email;
	}

	@Transient
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(
			ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	@Column(name = "enabled", columnDefinition = "BOOLEAN")
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(
			boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "date_enabled")
	public Date getDateEnabled() {
		return this.dateEnabled;
	}

	public void setDateEnabled(
			Date dateEnabled) {
		this.dateEnabled = dateEnabled;
	}

	@Column(name = "date_disabled")
	public Date getDateDisabled() {
		return this.dateDisabled;
	}

	public void setDateDisabled(
			Date dateDisabled) {
		this.dateDisabled = dateDisabled;
	}

	@Embedded
	public SecurityQuestionAnswer getSecurityQuestionAnswer() {
		return this.userHint;
	}

	public void setSecurityQuestionAnswer(
			SecurityQuestionAnswer securityQuestionAnswer) {
		this.userHint = securityQuestionAnswer;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	public Collection<Authority> getRoles() {
		return this.authorities;
	}

	public void setRoles(
			Collection<Authority> roles) {
		this.authorities = roles;
	}

	/********************************************************************************************************************************
	 * Begins Spring UserDetails implementation methods. User class should not implement UserDetails, instead we should
	 * create another class that uses Assembler to keep our design separate from Spring.
	 ********************************************************************************************************************************/

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
	public Authority getGreatestAuthority() {
		Authority greatestRole = new Authority(ROLE.ROLE_ANONYMOUS);
		for (Authority auth : getRoles())
			if (greatestRole.compare(auth) < 0)
				greatestRole = auth;
		return greatestRole;
	}

	@Transient
	public boolean isInternalUser() {
		Set<ROLE> internalRoles = new HashSet<ROLE>(Arrays.asList(ROLE.getInternalRoles()));

		for (Authority auth : getRoles())
			if (internalRoles.contains(auth.getRole()))
				return true;

		return false;
	}

	@Transient
	public boolean isUser() {
		return getRoles().contains(new Authority(ROLE.ROLE_USER));
	}

	@Transient
	public boolean isAuthenticated() {
		Authentication authentication = UserManager.securityContext.getContext().getAuthentication();
		return authentication != null && authentication.getPrincipal() instanceof UserDetails;
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
	public boolean equals(
			Object obj) {
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
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", email=" + email + "]";
	}

	/* *******************************************************************************
	 * 2013/06/04 properties to map the source code and the user that registered this user
	 */

	private SourceCode sourceCode;
	private User createdBy;

	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_source_code", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "source_code_id"))
	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(
			SourceCode sourceCode) {
		this.sourceCode = sourceCode;
	}

	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_created_by", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "creator_id"))
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(
			User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String getSessionKey() {
		return this.getClass().getSimpleName();
	}

}