package com.property.db.dao.impls;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.property.db.dao.interfaces.ContactDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.contacts.Contact;
import com.property.db.hibernate.FreeholdGenericDAOImpl;
import com.property.exceptions.InternalServerException;

public class ContactDAOImpl 
	extends FreeholdGenericDAOImpl<Contact, Long> 
		implements ContactDAO 
{

	@Override
	@SuppressWarnings("unchecked")
	public List<Contact> getContacts(Integer accountId)
	{
		List<Contact> results = null;
		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "FROM Contact C WHERE C.account=:id AND C.deleted = 0";
		Query query = session.createQuery(hql);
		query.setInteger("id", accountId);
		results = (List<Contact>)(List<?>)query.list();
		tx.commit();

		return results;
	}

	private Contact getContactWorker(Session session, Integer accountId, Long contactId)
	{
		String hql = "FROM Contact C WHERE C.account=:account_id AND C.id=:contact_id AND C.deleted = 0";
		Query query = session.createQuery(hql);
		query.setInteger("account_id", accountId);
		query.setLong("contact_id", contactId);
		return (Contact)query.uniqueResult();
	}
	
	@Override
	public Contact getContact(Integer accountId, Long contactId)
	{
		Contact contact = null;
		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		contact = getContactWorker(session, accountId, contactId);
		tx.commit();

		return contact;
	}
	
	@Override
	public Contact addContact(Contact contact)
	{
		Session session = this.getSession();	
		Transaction tx = session.beginTransaction();
		session.persist(contact);
		session.flush();
		tx.commit();
		
		return contact;
	}
	
	@Override
	public Contact addContact(Integer accountId, Contact contact)
	{
		contact.setAccountId(accountId);
		return addContact(contact);
	}
	
	@Override
	public Contact updateContact(Integer accountId, Contact contact)
	{
		Contact updatedContact = null;
		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		updatedContact = getContactWorker(session, accountId, contact.getId());
		if (updatedContact != null)
		{
			if (contact.getContactType() != null) updatedContact.setContactType(contact.getContactType());
			if (contact.getContactName() != null) updatedContact.setContactName(contact.getContactName());
			if (contact.getContact() != null) updatedContact.setContact(contact.getContact());
			
			this.update(updatedContact);
		}
		tx.commit();

		if (updatedContact == null)
			throw new InternalServerException();
		
		return updatedContact;
	}
	
	@Override
	public Boolean deleteContact(Integer accountId, Long contactId)
	{
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "update Contact C set C.deleted = 1, C.deletedOn = NOW() WHERE C.account=:account_id AND C.id=:contact_id AND C.deleted = 0";
		Query query = session.createQuery(hql);
		query.setInteger("account_id", accountId);
		query.setLong("contact_id", contactId);
		int updated = query.executeUpdate();
		tx.commit();

		return updated > 0 ? true : false;
	}
}
