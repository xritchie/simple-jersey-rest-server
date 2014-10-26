package com.property.db.dao.interfaces;

import java.security.NoSuchAlgorithmException;

import com.googlecode.genericdao.dao.hibernate.original.GenericDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.sessions.Session;
import com.property.db.entities.sessions.SessionPK;
import com.property.exceptions.UnauthorizedException;

public interface SessionDAO extends GenericDAO<Session, SessionPK> {
	
	public Session getSession
	(
		Integer accountId,
		String sessionKey
	);
	
	public Session createSession
	(
		Account account, 
		String ipAddress
	);
	
	public Session login
	(
		Account account, 
		String ipAddress, 
		String password
	)
		throws NoSuchAlgorithmException,UnauthorizedException;
	
	public Session verifySession
	(
		Integer accountId, 
		String sessionKey
	);

	public void logout
	(
		Session session
	);
	
	public void logoutAllSession
	(
			Integer accountId
	);
}
