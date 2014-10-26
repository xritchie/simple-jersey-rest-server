package com.property.resources.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.property.db.entities.account.*;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.property.app.mandrill.MandrillController;
import com.property.connections.HibernateSessionFacotry;
import com.property.db.entities.contacts.Contact;
import com.property.db.entities.sessions.Session;
import com.property.db.entities.sessions.SessionBody;
import com.property.exceptions.InternalServerException;
import com.property.exceptions.UnauthorizedException;
import com.property.resources.Account;

@Path("/account")
public class AccountImpl implements Account{

	@Autowired
	@Qualifier("default")
	private HibernateSessionFacotry sessionFactory;
	
	@Autowired
	@Qualifier("default")
	private MandrillController mandrillController;

	@Override
	public com.property.db.entities.account.Account createAccount
	(
		final com.property.db.entities.account.Account account,
		HttpServletRequest request
	) 
		throws NoSuchAlgorithmException, MandrillApiError, IOException
	{
		account.setRegistrationIp(request.getRemoteAddr());
		com.property.db.entities.account.Account postAccount = sessionFactory.getAccountDAO().registerAccount(account);
		mandrillController.sendActivationEmail(postAccount);
		return postAccount.toSummary();
	}

	@Override
	public Session activateAccount
	(
		Integer accountId,
		String activationCode,
		HttpServletRequest request
	) 
		throws MandrillApiError, IOException 
	{
		com.property.db.entities.account.Account activatedAccount = 
				sessionFactory.getAccountDAO().activateAccount(accountId, activationCode);
		if (activatedAccount == null)
			throw  new UnauthorizedException();
		
		mandrillController.sendWelcomeEmail(activatedAccount);
		
		if (activatedAccount.getState() == AccountState.PENDING)
			throw new InternalServerException();
		
		return sessionFactory.getSessionDAO().createSession(activatedAccount, request.getRemoteAddr());
	}

	@Override
	public Boolean requestPasswordChange
	(
		SessionBody resetPassword,
		HttpServletRequest request
	) 
		throws MandrillApiError, IOException 
	{
		com.property.db.entities.account.Account account = 
				sessionFactory.getAccountDAO().resetConfirmationCode(resetPassword.getEmail());
		
		mandrillController.sendForgotPasswordEmail(account);
		
		return true;
	}

	@Override
	public Session resetPassword
	(
		Integer accountId,
		String activationCode,
		SessionBody resetPassword,
		HttpServletRequest request
	) throws NoSuchAlgorithmException, MandrillApiError, IOException 
	{
		com.property.db.entities.account.Account resetAccount = 
				sessionFactory.getAccountDAO().resetPassword(accountId, activationCode, resetPassword.getPassword());
		
		if (resetAccount == null)
			throw  new UnauthorizedException();
		
		mandrillController.sendResetPasswordEmail(resetAccount);
		
		return sessionFactory.getSessionDAO().createSession(resetAccount, request.getRemoteAddr());
	}
	
	@Override
	public com.property.db.entities.account.Account getAccount(HttpHeaders httpHeaders) 
	{
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		com.property.db.entities.account.Account account = sessionFactory.getAccountDAO().getAccount(uid);
		return account == null ? null : account.toSummary();
	}

	@Override
	public com.property.db.entities.account.Account updateAccount
	(
		final com.property.db.entities.account.Account account
	) 
	{
		com.property.db.entities.account.Account updateAccount = sessionFactory.getAccountDAO().updateAccount(account);
		return updateAccount == null ? null : updateAccount.toSummary();
	}

	@Override
	public Boolean deleteAccount(HttpHeaders httpHeaders) 
			throws MandrillApiError, IOException {
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		com.property.db.entities.account.Account deletedAccount = sessionFactory.getAccountDAO().deleteAccount(uid);
		if (deletedAccount == null)
			throw  new InternalServerException();
		
		sessionFactory.getSessionDAO().logoutAllSession(uid);
		
		mandrillController.sendDeleteAccount(deletedAccount);
		
		return true;
	}

	
	
	@Override
	public com.property.db.entities.contacts.Contact[] getContacts(HttpHeaders httpHeaders) {
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		List<Contact> contacts = sessionFactory.getContactDAO().getContacts(uid);
		Contact[] contactsArr = contacts.stream().map(v -> v.asContactSummary()).toArray(s -> new Contact[s]);
		return contactsArr;
	}

	@Override
	public com.property.db.entities.contacts.Contact addContact
	(
			final com.property.db.entities.contacts.Contact contact,
			HttpHeaders httpHeaders
	) 
	{
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		contact.setAccountId(uid);
		Contact postContact = sessionFactory.getContactDAO().addContact(uid, contact);
		return postContact;
	}

	@Override
	public com.property.db.entities.contacts.Contact getContact
	(
		Long contactId,
		HttpHeaders httpHeaders
	) 
	{
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		Contact contact =  sessionFactory.getContactDAO().getContact(uid, contactId);
		return contact != null ? contact.asContactSummary() : null;
	}

	@Override
	public com.property.db.entities.contacts.Contact updateContact
	(
		final com.property.db.entities.contacts.Contact contact,
		Long contactId,
		HttpHeaders httpHeaders
	) 
	{
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		contact.setAccountId(uid);
		Contact updatedContact =  sessionFactory.getContactDAO().updateContact(uid, contact);
		return updatedContact != null ? updatedContact.asContactSummary() : null;
	}
	
	@Override
	public Boolean deleteContact
	(
		Long contactId,
		HttpHeaders httpHeaders
	) 
	{
		Integer uid = httpHeaders.getHeaderString("From") == null  ? null : Integer.parseInt(httpHeaders.getHeaderString("From"));
		return sessionFactory.getContactDAO().deleteContact(uid, contactId);
	}
}
