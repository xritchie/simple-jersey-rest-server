package com.property.filters.security;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.property.db.entities.account.Account;
import com.property.db.entities.sessions.Session;
import com.property.utility.ConfigUtility;
import com.property.utility.SpringPropertiesUtil;

 
// register as jersey's provider
//TODO:: Grizzly NOT MAKING USE OF SECURITY CONTEXT 
public class Authorizer implements javax.ws.rs.core.SecurityContext {
	
	@Inject
    javax.inject.Provider<UriInfo> uriInfo;
	
	private final Account account;
	private final Session session;
	private final Principal principal;

	public Authorizer() {
		super();
		this.account = null;
		this.session = null;
		this.principal = null;
	}

	public Authorizer(Account account, Session session) {
		this.account = account;
		this.session = session;
		
		this.principal = new Principal() {
			@Override
			public String getName() {
				return account.getAlias();
			}
       };
	}
	
	@Override
    public String getAuthenticationScheme() {
        return Authorizer.BASIC_AUTH;
    }
 
    @Override
    public Principal getUserPrincipal() {
        return principal;
    }
 
    @Override
    public boolean isSecure() {
    	if (ConfigUtility.isDebugMode())
    		return "https".equals(uriInfo.get().getRequestUri().getScheme());
    	else
    		return true;
    }
 
    @Override
    public boolean isUserInRole(String role) {
 
        if ((role == null) || (session == null) || (!session.isValidSession()) || (this.session.getType() == null) || (account == null)) 
            return false;

		return this.session.getType().toString().equals(role);
    }

}
