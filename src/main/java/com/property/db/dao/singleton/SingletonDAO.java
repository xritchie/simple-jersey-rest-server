package com.property.db.dao.singleton;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;

import com.property.db.dao.impls.AccountDAOImpl;
import com.property.db.dao.impls.ContactDAOImpl;
import com.property.db.dao.impls.SessionDAOImpl;
import com.property.db.dao.interfaces.AccountDAO;
import com.property.db.dao.interfaces.ContactDAO;
import com.property.db.dao.interfaces.SessionDAO;

//@Component("dao")
//@Qualifier("default")
//@Scope("singleton")
public class SingletonDAO {
		
	private SessionDAO sessionDAOImpl = null;
	private AccountDAO accountDAOImpl = null;
	private ContactDAO contactDAO = null;
	
	public SingletonDAO(SessionFactory sessionFactory) {
		createSingletonAccountDAO(sessionFactory);
		createSingletonSessionDAO(sessionFactory);
		createSingletonContactDAO(sessionFactory);
	}
	
	@PostConstruct
	public void init()
	{
	}
	
	@PreDestroy
	public void destroy()
	{
	}
   
	
	public AccountDAO getAccountDAO() {
		return accountDAOImpl;
	}
	
	public SessionDAO getSessionDAO() {
		return sessionDAOImpl;
	}
	
	public ContactDAO getContactDAO() {
		return contactDAO;
	}

	public void createSingletonAccountDAO(SessionFactory sessionFactory)
	{
		if (accountDAOImpl == null)
		{
			accountDAOImpl = new AccountDAOImpl();
			if (sessionFactory != null)
				((AccountDAOImpl)accountDAOImpl).setSessionFactory(sessionFactory);
		}
	}
	
	public void createSingletonSessionDAO(SessionFactory sessionFactory)
	{
		if (sessionDAOImpl == null)
		{
			sessionDAOImpl = new SessionDAOImpl();
			if (sessionFactory != null)
				((SessionDAOImpl)sessionDAOImpl).setSessionFactory(sessionFactory);
		}
	}
	
	public void createSingletonContactDAO(SessionFactory sessionFactory)
	{
		if (contactDAO == null)
		{
			contactDAO = new ContactDAOImpl();
			if (sessionFactory != null)
				((ContactDAOImpl)contactDAO).setSessionFactory(sessionFactory);
		}
	}
}
