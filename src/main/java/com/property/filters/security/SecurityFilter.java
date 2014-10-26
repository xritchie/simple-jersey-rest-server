package com.property.filters.security;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;

import org.springframework.beans.factory.annotation.Autowired;

import com.property.connections.HibernateSessionFacotry;
import com.property.db.entities.account.Account;
import com.property.db.entities.sessions.Session;
import com.property.utility.ConfigUtility;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

//@Provider    					//Is used to register the filter with jersey since were using dynamic features it is not required
//@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements  ContainerRequestFilter {
	     	
	@Context
    private ResourceInfo resourceInfo;
	
	@Autowired
	private HibernateSessionFacotry sessionFactory;
		
    @Override
    public void filter(ContainerRequestContext request) {
    	try
    	{
    		String sessionKey = request.getHeaderString("Authorization");
    		Integer uid = request.getHeaderString("From") == null  ? null : Integer.parseInt(request.getHeaderString("From"));
    		
    		if ((sessionKey == null) || (uid == null))
    			return;
    					
    		Session session = null;
    		Account account = null;

			session = sessionFactory.getSessionDAO().verifySession(uid, sessionKey);
			if (session != null)
				account = session.getAccount();
    		
    		if 	(
					(session != null) 
					&& (account != null)
					&& (session.getSessionKey().equals(sessionKey)) 
					&& (session.getAccountId() == uid)
					&& (session.isValidSession())
				)
    				request.setSecurityContext(new Authorizer(account, session));
    		else
    			request.setSecurityContext(new Authorizer());
    	}
    	catch (Exception ex)
    	{
    		if (ConfigUtility.isDebugMode())
    			ex.printStackTrace();
    		return;
    	}
    }
}
