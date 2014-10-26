package com.property.db.entities.sessions;

import com.property.db.validators.ValidEmail;
import com.property.utility.genson.annotations.Censor;


public class SessionBody {
	
	private Integer accountId = null;
	
	private String email = null;
	
	@Censor
	private String password = null;

	public SessionBody() {
		super();
	}

	public SessionBody(Integer accountId, String email, String password) {
		this();
		
		this.setAccountId(accountId);
		this.setEmail(email);
		this.setPassword(password);
	}

	public SessionBody(String email, String password)
	{
		this(null, email, password);
	}
	
	public SessionBody(String email)
	{
		this(null, email, null);
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
    @ValidEmail
	public String getEmail() {
		return email;
	}
	public void setEmail(@ValidEmail String email) {
		this.email = email.toLowerCase();
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
