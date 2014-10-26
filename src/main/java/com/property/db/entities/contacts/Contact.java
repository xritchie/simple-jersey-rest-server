package com.property.db.entities.contacts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.property.db.entities.account.Account;

@Entity
@Table(name="contacts")
public class Contact extends com.property.db.entities.Entity{

	@Id 
	@Column(name = "contact_id", nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="account_id", nullable=false)
	private Integer accountId; 
		
	@ManyToOne(fetch=FetchType.LAZY)  
	@JoinColumn(insertable=false, updatable=false, name="account_id")
	private Account account;
	
	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContactType contactType;
	
	@Column(name = "contact_name", nullable = true, length = 45)
	@Type(type = "string")
	private String contactName;
	
	@Column(name = "contact", nullable = true, length = 128)
	@Type(type = "string")
	private String contact;

	public Contact() {
		super();
	}

	public Contact(Long id, Integer accountId, Account account, ContactType contactType,
			String contactName, String contact) {
		this();
		this.setId(id);
		this.setAccountId(accountId);
		this.setAccount(account);
		this.setContactType(contactType);
		this.setContactName(contactName);
		this.setContact(contact);
	}
	
	public Contact(Integer accountId, ContactType contactType, String contactName, String contact) {
		this(null, accountId, null, contactType, contactName, contact);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	public ContactType getContactType() {
		return contactType;
	}
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public Contact asContactSummary()
	{
		this.setAccount(null);
		return this;
	}	
}
