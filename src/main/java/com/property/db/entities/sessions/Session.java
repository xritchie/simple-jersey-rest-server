package com.property.db.entities.sessions;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.property.db.entities.account.Account;
import com.property.db.entities.account.AccountState;
import com.property.db.entities.account.AccountType;
import com.property.security.SecurityUtility;
import com.property.utility.ConfigUtility;

@Entity
@Table(name="sessions")
@IdClass(SessionPK.class)
public class Session extends com.property.db.entities.Entity implements Serializable{

	private static final long serialVersionUID = 7840619975522514195L;

	@Id 
	@Column(name = "session_key", nullable=false)
	private String sessionKey;	
	
	@Id 
	@Column(name="account_id", nullable=false)
	private Integer accountId; 
	
	@ManyToOne(fetch=FetchType.EAGER) 
    @JoinColumn(insertable=false, updatable=false, name="account_id")
	@Where(clause = "active IS TRUE")
	private Account account; 
	
	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountState state; 
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Column(name = "login_ip", nullable = false, length = 128)
	private String loginIp;
	
	@Column(name = "closed_on", nullable = true)
	private Long closedOn;
	
	@Column(name = "requests_made", nullable = true)
	private Integer requestsMade; 
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@Column(name = "expires", nullable = false)
	private Long expires; 
	
	@Column(name = "logout_type", nullable = true)
	@Enumerated(EnumType.STRING)
	private SessionLogoutType logoutType;

	public Session() {
		super();
		
		this.setSessionKey(SecurityUtility.generateGUIDWithoutDashes());
		this.setClosedOn(null);
		this.setRequestsMade(0); 
		this.setLogoutType(null);
		this.setActive(true);
		this.setExpires(ConfigUtility.getSessionExpiration());
	}
	
	public Session(Account account, String loginIp) 
	{
		this();
		this.setAccount(account);
		this.setAccountId(account.getId());
		this.setState(account.getState());
		this.setType(account.getType());
		this.setLoginIp(loginIp);		
	}

	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public AccountState getState() {
		return state;
	}
	public void setState(AccountState state) {
		this.state = state;
	}

	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}

	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Long getClosedOn() {
		return closedOn;
	}
	public void setClosedOn(Long closedOn) {
		this.closedOn = closedOn;
	}

	public Integer getRequestsMade() {
		return requestsMade;
	}
	public void setRequestsMade(Integer requestsMade) {
		this.requestsMade = requestsMade;
	}

	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getExpires() {
		return expires;
	}
	public void setExpires(Long expires) {
		this.expires = expires;
	}
	
	public SessionLogoutType getLogoutType() {
		return logoutType;
	}
	public void setLogoutType(SessionLogoutType logoutType) {
		this.logoutType = logoutType;
	}

	public Boolean isExpired()
	{
		return !((this.getExpires() > new Date().getTime()) || (this.getExpires() == 0));
	}
	
	public Boolean isValidSession()
	{
		return 
		(
			(this.getType() != null) &&
			(this.getState() != null) &&
			(this.getClosedOn() == null) &&
			(this.isActive()) && 
			(this.getState() != AccountState.BLOCKED) &&
			(this.getState() != AccountState.DELETED) &&
			(!this.isExpired()) &&
			(this.isDeleted() == false)
		);
	}
	
	public Boolean isLoggedOut()
	{
		return
			(
				(!this.getActive()) &&
				(this.getLogoutType() != null) &&
				(this.getClosedOn() != null)
			);
	}
	
	public void logout(SessionLogoutType logoutType)
	{
		this.setActive(false);
		this.setLogoutType(logoutType);
		this.setClosedOn(new Date().getTime());
	}	
		
	public boolean isActive()
	{
		return active;
	}
}
