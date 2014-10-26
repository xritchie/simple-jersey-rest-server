package com.property.db.entities.sessions;

import java.io.Serializable;

import com.property.db.entities.account.Account;
import com.property.security.SecurityUtility;

public class SessionPK implements Serializable {

	private static final long serialVersionUID = -4324138177969595368L;
	
	private String sessionKey = null;	
	private Integer accountId = null;
	
	
	public SessionPK() {
		super();
		this.setSessionKey(SecurityUtility.generateGUIDWithoutDashes());
	}

	public SessionPK(Account account) {
		this();
		this.setAccountId(account.getId());
	}
	

	public SessionPK(Integer accountId, String sessionKey) {
		super();
		this.setSessionKey(sessionKey);
		this.setAccountId(accountId);
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	} 
}
