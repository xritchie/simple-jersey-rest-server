package com.property.db.dao.interfaces;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.original.GenericDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.contacts.Contact;

public interface ContactDAO 
	extends GenericDAO<Contact, Long> 
{
	public List<Contact> getContacts(Integer accountId);
	
	public Contact addContact(Contact contact);
	
	public Contact addContact(Integer accountId, Contact contact);
	
	public Contact getContact(Integer accountId, Long contactId);
	
	public Contact updateContact(Integer accountId, Contact contact);
	
	public Boolean deleteContact(Integer accountId, Long contactId);
}
