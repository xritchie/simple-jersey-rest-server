package com.property.connections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.property.db.dao.interfaces.AccountDAO;
import com.property.db.dao.interfaces.ContactDAO;
import com.property.db.dao.interfaces.SessionDAO;
import com.property.db.dao.singleton.SingletonDAO;

@Component("sessionFactory")
@Qualifier("default")
@Scope("singleton")
public class HibernateSessionFacotry {

	private SessionFactory sessionFactory = null;
	private SingletonDAO dao = null;
	
	public HibernateSessionFacotry() {
		createSessionFactoryInstance();
		createDAOs();
	}
	
	@PostConstruct
	public void init()
	{
//		EventListenerRegistry registry = ((SessionFactoryImpl)sessionFactory).getServiceRegistry().getService(
//        EventListenerRegistry.class);
//        registry.getEventListenerGroup(EventType.DELETE).appendListener(new SoftDeleteEventListener());
	}
	
	@PreDestroy
	public void destroy()
	{
		if (!sessionFactory.isClosed())
			sessionFactory.close();
	}
	
	
	public SingletonDAO getDao() {
		return dao;
	}
	
	public AccountDAO getAccountDAO() {
		return  getDao().getAccountDAO();
	}
	
	public SessionDAO getSessionDAO() {
		return getDao().getSessionDAO();
	}
	
	public ContactDAO getContactDAO() {
		return getDao().getContactDAO();
	}

	public SessionFactory getSessionFactory() {
		createSessionFactoryInstance();
		return sessionFactory;
	}
	
	public Session getSession()
	{
		SessionFactory sessionFactory = this.getSessionFactory();
		Session currentSession = sessionFactory.getCurrentSession();
		if ((currentSession != null) && (currentSession.isOpen()))
			return sessionFactory.getCurrentSession();
		
		return sessionFactory.openSession();
	}

	
	public void createSessionFactoryInstance()
	{
		if (sessionFactory == null)
		{
			Configuration configuration = new Configuration();
		    configuration.configure();
		    StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		    sessionFactory = configuration.buildSessionFactory(ssrb.build());
		}
	}
	
	public void createDAOs()
	{
		if (dao == null)
			dao = new SingletonDAO(getSessionFactory());
	}
}
