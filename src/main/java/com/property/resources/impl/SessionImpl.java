package com.property.resources.impl;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.property.connections.HibernateSessionFacotry;
import com.property.db.entities.account.Account;
import com.property.db.entities.sessions.SessionBody;
import com.property.exceptions.UnauthorizedException;
import com.property.resources.Session;
import com.property.utility.ConfigUtility;
import com.property.utility.SpringPropertiesUtil;


@Path("/session")
public class SessionImpl implements Session{

	@Autowired
	@Qualifier("default")
	private HibernateSessionFacotry sessionFactory;
	
	@Override
	public com.property.db.entities.sessions.Session login(SessionBody sessionBody, HttpServletRequest request) 
		throws UnauthorizedException, NoSuchAlgorithmException 
	{
		Account account = sessionFactory.getAccountDAO().getAccount(sessionBody.getEmail());
		if (account == null)
			throw  new UnauthorizedException();
		
		if (ConfigUtility.isSingleSessionEnabled())
			sessionFactory.getSessionDAO().logoutAllSession(account.getId());
		
		return sessionFactory.getSessionDAO().login(account, request.getRemoteAddr(), sessionBody.getPassword());
	}

	@Override
	public Boolean logout(HttpHeaders httpHeaders) {
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		String sessionKey = httpHeaders.getHeaderString("Authorization");

		com.property.db.entities.sessions.Session session = sessionFactory.getSessionDAO().getSession(uid, sessionKey);
		if (session == null)
			return false;
		
		if (!session.isValidSession())
			return true;
		
		sessionFactory.getSessionDAO().logout(session);
		return true;
	}

	@Override
	public com.property.db.entities.sessions.Session refreshSession(HttpHeaders httpHeaders) {
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		String sessionKey = httpHeaders.getHeaderString("Authorization");
		
		com.property.db.entities.sessions.Session session = sessionFactory.getSessionDAO().verifySession(uid, sessionKey);
		if (session == null)
			throw  new UnauthorizedException();
			
		return session;
	}
	

}
