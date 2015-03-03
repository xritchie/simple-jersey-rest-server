package com.property.db.dao.impls;


import java.security.NoSuchAlgorithmException;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.property.db.dao.interfaces.SessionDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.sessions.Session;
import com.property.db.entities.sessions.SessionLogoutType;
import com.property.db.entities.sessions.SessionPK;
import com.property.db.hibernate.FreeholdGenericDAOImpl;
import com.property.db.validators.ValidSession;
import com.property.exceptions.UnauthorizedException;

public class SessionDAOImpl 
	extends FreeholdGenericDAOImpl<Session, SessionPK> 
		implements SessionDAO  
{
	private Session getSession(org.hibernate.Session session, Integer accountId, String sessionKey)
	{
		String hql = "FROM Session S WHERE S.accountId=:accountId AND S.sessionKey=:sessionKey";
		Query query = session.createQuery(hql);
		query.setInteger("accountId", accountId);
		query.setString("sessionKey", sessionKey);
		return (Session)query.uniqueResult();
	}
	
	@ValidSession
	@Override
	public Session getSession(Integer accountId, String sessionKey)
	{		
		org.hibernate.Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		//Session userSession = this.fetch(new SessionPK(accountId, sessionKey));
		Session userSession = getSession(session, accountId, sessionKey);
		tx.commit();
				
		if (userSession != null)
			userSession.setAccount(userSession.getAccount().toSummary());
		
		return userSession;
	}
		
	@Override
	public Session createSession(Account account, String ipAddress)
	{
		Session userSession = new Session(account, ipAddress);

		org.hibernate.Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		session.persist(userSession);
		session.flush();
		tx.commit();
		
		if (userSession != null)
			userSession.setAccount(userSession.getAccount().toSummary());
		
		return userSession;
	}
	
	@Override
	public Session login(Account account, String ipAddress, String password) 
			throws NoSuchAlgorithmException,UnauthorizedException
	{
		if (account.isValidPassword(password) == false)
			throw new UnauthorizedException();

		return createSession(account, ipAddress);
		
	}
		
	@Override
	public Session verifySession(Integer accountId, String sessionKey)
	{
		Session userSession = getSession(accountId, sessionKey);
		
		if ((userSession != null) && (userSession.isValidSession()))
			return userSession;
		else
		{
			if ((userSession != null) && (userSession.isExpired()))
			{
				org.hibernate.Session session = this.getSession();
				userSession.logout(SessionLogoutType.EXPIRED);
				
				Transaction tx = session.beginTransaction();
				session.persist(userSession);
				tx.commit();
			}
			
			return null;
		}
	}
	
	@Override
	public void logout(Session session)
	{
		session.logout(SessionLogoutType.LOGOUT);
		
		org.hibernate.Session txSession = this.getSession();
		Transaction tx = txSession.beginTransaction();
		this.update(session);
		tx.commit();
	}
	
	@Override
	public void logoutAllSession(Integer accountId)
	{
		org.hibernate.Session txSession = this.getSession();
		Transaction tx = txSession.beginTransaction();
		Query queryupdate = 
			txSession
		        .createQuery
		        (
	        		"UPDATE Session AS S " + 
					"SET S.active = 0, S.closedOn = UNIX_TIMESTAMP(NOW()), S.logoutType = :logoutType " +
					"WHERE S.accountId = :accountId AND (S.active = 1 OR S.closedOn IS NULL)"
				);
		queryupdate.setString("logoutType", SessionLogoutType.LOGIN.toString().toUpperCase());
		queryupdate.setInteger("accountId", accountId);
		queryupdate.executeUpdate();
		tx.commit();
	}

	
}
