package com.property.db.entities.account;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.security.auth.Subject;

import org.hibernate.annotations.Type;

import com.property.db.entities.contacts.Contact;
import com.property.db.entities.sessions.Session;
import com.property.security.SecurityUtility;
import com.property.utility.genson.annotations.Censor;

@Entity
@Table(name="accounts")
public class Account extends com.property.db.entities.Entity implements java.security.Principal {	
	
	@Id 
	@Column(name = "account_id", nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountState state;
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Column(name = "email", nullable = true, length = 128, unique = true)
	@Type(type = "string")
	private String email;
	
	@Column(name = "password_salt", nullable = true, length = 128)
	@Type(type = "string")
	private String passwordSalt;
	
	@Column(name = "encrypted_password", nullable = true, length = 128)
	@Type(type = "string")
	@Censor
	private String password;
	
	@Column(name = "encrypt_count", nullable = true)
	private Integer encryptCount;
	
	@Column(name = "persistanc_token", nullable = true, length = 128)
	@Type(type = "string")
	private String persistanceToken;
	
	@Column(name = "single_access_token", nullable = true, length = 128)
	@Type(type = "string")
	private String singleAccessToken;
	
	@Column(name = "perishable_token", nullable = true, length = 128)
	@Type(type = "string")
	private String perishableToken;
	
	@Column(name = "registration_ip", nullable = true, length = 30)
	@Type(type = "string")
	private String registrationIp;
	
	@Column(name = "alias", nullable = true, length = 30, unique = true)
	@Type(type = "string")
	private String alias;
	
	@Column(name = "first_name", nullable = true, length = 45)
	@Type(type = "string")
	private String firstName;
	
	@Column(name = "last_name", nullable = true, length = 45)
	@Type(type = "string")
	private String lastName;
	
	@Column(name = "dob", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	@Column(name = "recieve_promo_info", nullable = true)
	@Type(type = "numeric_boolean")
	private Boolean recievePromtionalInfo;
	
	@OneToMany(mappedBy="account", cascade=CascadeType.ALL)
	private List<Contact> contactInfo;
	
	@OneToMany(mappedBy="account")
	private List<Session> sessions;

	public Account() {
		super();
	}
	
	public Account(Integer id) {
		super();
		this.setId(id);
	}

	public Account(Integer id, AccountState state, AccountType type,
			String email, String passwordSalt, String password, Integer encryptCount,
			String persistanceToken, String singleAccessToken,
			String perishableToken, String registrationIp, String alias,
			String firstName, String lastName, Date dob,
			Boolean recievePromtionalInfo, List<Contact> contactInfo) {
		super();
		this.setId(id);
		this.setState(state);
		this.setType(type);
		this.setEmail(email);
		this.setPasswordSalt(passwordSalt);
		this.setPassword(password);
		this.setEncryptCount(encryptCount);
		this.setPersistanceToken(persistanceToken);
		this.setSingleAccessToken(singleAccessToken);
		this.setPerishableToken(perishableToken);
		this.setRegistrationIp(registrationIp);
		this.setAlias(alias);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setDob(dob);
		this.setRecievePromtionalInfo(recievePromtionalInfo);
		this.setContactInfo(contactInfo);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public AccountState getState() {
		return state;
	}
	public void setState(AccountState state) {
		this.state = state;
	}

	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@Censor
	public String getPassword() {
		return password;
		
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEncryptCount() {
		return encryptCount;
	}

	public void setEncryptCount(Integer encryptCount) {
		this.encryptCount = encryptCount;
	}

	public String getPersistanceToken() {
		return persistanceToken;
	}
	public void setPersistanceToken(String persistanceToken) {
		this.persistanceToken = persistanceToken;
	}

	public String getSingleAccessToken() {
		return singleAccessToken;
	}
	public void setSingleAccessToken(String singleAccessToken) {
		this.singleAccessToken = singleAccessToken;
	}

	public String getPerishableToken() {
		return perishableToken;
	}
	public void setPerishableToken(String perishableToken) {
		this.perishableToken = perishableToken;
	}

	public String getRegistrationIp() {
		return registrationIp;
	}
	public void setRegistrationIp(String registrationIp) {
		this.registrationIp = registrationIp;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Boolean getRecievePromtionalInfo() {
		return recievePromtionalInfo;
	}
	public void setRecievePromtionalInfo(Boolean recievePromtionalInfo) {
		this.recievePromtionalInfo = recievePromtionalInfo;
	}

	public List<Contact> getContactInfo() {
		if (contactInfo == null)
			contactInfo = new ArrayList<Contact>();
		return contactInfo;
	}
	public void setContactInfo(List<Contact> contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean implies(Subject subject) {
		return false;
	}
	
	public Account toSummary()
	{
		this.setPasswordSalt(null);
		this.setPassword(null);
		this.setEncryptCount(null);
		
		this.setPersistanceToken(null);
		this.setPerishableToken(null);
		this.setSingleAccessToken(null);
		this.setRegistrationIp(null);
		
		this.setContactInfo(null);
		this.setSessions(null);
		
		return this;
	}
	
	public boolean isValidPassword(String password) throws NoSuchAlgorithmException
	{
		return 
		(
			(this != null) &&
			(this.getPassword() != null) &&
			(this.getPassword().equals(SecurityUtility.hashPassword(password, this.getPasswordSalt(), this.getEncryptCount()))) &&
			(this.getState() != AccountState.BLOCKED)
		);
	}

}
