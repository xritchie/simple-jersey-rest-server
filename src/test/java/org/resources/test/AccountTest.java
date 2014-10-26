package org.resources.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.property.db.entities.account.Account;
import com.property.db.entities.contacts.Contact;
import com.property.db.entities.contacts.ContactType;
import com.property.db.entities.sessions.Session;
import com.property.db.entities.sessions.SessionBody;


public class AccountTest extends GrizzlyWebTest {

	private final String password = "password";
	private final String newPassword = "new-password";
	private com.property.db.entities.account.Account reqAccount = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		reqAccount = new com.property.db.entities.account.Account();
		reqAccount.setType(com.property.db.entities.account.AccountType.OWNER);
		reqAccount.setEmail("xritchie@gmail.com");
		reqAccount.setAlias("Kayngee");
		reqAccount.setFirstName("Shawn");
		reqAccount.setLastName("Ritchie");
		reqAccount.setDob(sdf.parse("22/08/1987"));
		reqAccount.setRecievePromtionalInfo(true);
		reqAccount.setPassword(password);
		assertNotNull(reqAccount);
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
    public com.property.db.entities.account.Account registerAccount() throws IOException {	
		WebTarget t = target("account");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post(Entity.entity(reqAccount, MediaType.APPLICATION_JSON));
		
		
		com.property.db.entities.account.Account resAccount
			= convertResponse(response, com.property.db.entities.account.Account.class);
		
		assertNotNull(resAccount);
		assertTrue(resAccount.getEmail().equals(this.reqAccount.getEmail()));
		
		return resAccount;
    }
	
    public Session activateAccount(com.property.db.entities.account.Account regAccount) throws IOException {	
			
		String activationCode = sessionFactory.getAccountDAO().getActivationCode(regAccount.getId());
		
		WebTarget t = target("account").path(regAccount.getId() + "/activate/" + activationCode);
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post(null);
			
		Session session 
			= convertResponse(response, com.property.db.entities.sessions.Session.class);

		assertNotNull(session);
		assertNotNull(session.getAccount());
		assertTrue(session.isActive());
		assertTrue(session.isValidSession());
		
		return session;
    }
    
    public Session login(com.property.db.entities.account.Account regAccount, String usePassword) throws IOException
    {
		WebTarget t = target("session").path("login");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post
			(
				Entity.entity
				(
					new SessionBody(regAccount.getEmail(), usePassword), 
					MediaType.APPLICATION_JSON
				)
			);
		
		Session session 
			= convertResponse(response, com.property.db.entities.sessions.Session.class);
		
		assertNotNull(session);
		assertNotNull(session.getAccount());
		assertTrue(session.isActive());
		assertTrue(session.isValidSession());
		
		return session;
    }
    
    public Session refreshSession(Session loginSession) throws IOException
    {
		WebTarget t = target("session");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", loginSession.getAccountId())
			.header("Authorization", loginSession.getSessionKey())
			.get();
		
		Session session 
			= convertResponse(response, com.property.db.entities.sessions.Session.class);
		
		assertNotNull(session);
		assertNotNull(session.getAccount());
		assertTrue(session.isActive());
		assertTrue(session.isValidSession());
		
		return session;
    }
    
    public Boolean logout(Session loginSession)
    {
		WebTarget t = target("session").path("logout");
		Boolean loggedout =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", loginSession.getAccountId())
			.header("Authorization", loginSession.getSessionKey())
			.post(null, Boolean.class);
		assertNotNull(loggedout);
		assertTrue(loggedout);
		
		return loggedout;
    }
    
    public void refreshExpiredSession(Session loginSession)
    {
		WebTarget t = target("session");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", loginSession.getAccountId())
			.header("Authorization", loginSession.getSessionKey())
			.get();
		
		assertNotNull(response);
		assertTrue(response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode());
    }
    
    public void loginIncorrectEmail(String usePassword)
    {

		WebTarget t = target("session").path("login");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post
			(
				Entity.entity
				(
					new SessionBody("not.valid.email@false.com", usePassword), 
					MediaType.APPLICATION_JSON
				)
			);
		
		assertNotNull(response);
		assertTrue(response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode());
    }
    
    public void loginIncorrectPassword(com.property.db.entities.account.Account regAccount, String usePassword)
    {

		WebTarget t = target("session").path("login");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post
			(
				Entity.entity
				(
					new SessionBody(regAccount.getEmail(), usePassword), 
					MediaType.APPLICATION_JSON
				)
			);
		
		assertNotNull(response);
		assertTrue(response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode());
    }
    
    public void requestNewPassword(com.property.db.entities.account.Account regAccount)
    {
		WebTarget t = target("account").path("forgot-password");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post
			(
				Entity.entity
				(
					new SessionBody(regAccount.getEmail()), 
					MediaType.APPLICATION_JSON
				)
			);
		
		assertNotNull(response);
		assertNotNull(response.getStatus() == Response.Status.OK.getStatusCode());
    }
    
    public Session resetPassword(com.property.db.entities.account.Account regAccount) throws IOException {	
		
		String activationCode = sessionFactory.getAccountDAO().getActivationCode(regAccount.getId());
		
		WebTarget t = target("account").path(regAccount.getId() + "/reset/" + activationCode);
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.post
			(
				Entity.entity
				(
					new SessionBody(regAccount.getEmail(), this.newPassword), 
					MediaType.APPLICATION_JSON
				)
			);
		
		Session session 
			= convertResponse(response, com.property.db.entities.sessions.Session.class);
		
		assertNotNull(session);
		assertNotNull(session.getAccount());
		assertTrue(session.isActive());
		assertTrue(session.isValidSession());
		
		return session;
    }
    
    public Account getAccount(Session validSession) throws IOException
    {
		WebTarget t = target("account");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.get();
		
		com.property.db.entities.account.Account account
			= convertResponse(response, com.property.db.entities.account.Account.class);
		
		assertNotNull(account);
		assertTrue(account.getEmail().equals(reqAccount.getEmail()));
		
		return account;
    }
    
    public Account updateAccount(Session validSession) throws IOException
    {
    	Account putAccount = new Account(validSession.getAccountId());
    	putAccount.setAlias("newAlias");
    	
		WebTarget t = target("account");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.put
			(
				Entity.entity
				(
					putAccount, 
					MediaType.APPLICATION_JSON
				)
			);
		
		com.property.db.entities.account.Account account
			= convertResponse(response, com.property.db.entities.account.Account.class);
		
		assertNotNull(account);
		assertTrue(account.getEmail().equals(reqAccount.getEmail()));
		assertTrue(putAccount.getAlias().equals(account.getAlias()));
		
		return account;
    }
    
    public void deleteAccount(Session validSession) throws IOException
    {
    	Account putAccount = new Account();
    	putAccount.setAlias("newAlias");
    	
		WebTarget t = target("account");
		Boolean deleted =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.delete(Boolean.class);

		assertNotNull(deleted);
		assertTrue(deleted);
    }
    
    public void getAccountInvalidSessionKey(Session expiredSession)
    {
		WebTarget t = target("account");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", expiredSession.getAccountId())
			.header("Authorization", expiredSession.getSessionKey())
			.get();
		
		assertNotNull(response);
		assertTrue(response.getStatus() == Response.Status.FORBIDDEN.getStatusCode());
    }
    
    public Contact createContact(Session validSession) throws IOException {	
		WebTarget t = target("account").path("contact");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.post(Entity.entity(new Contact(validSession.getAccountId(), ContactType.EMAIL, "Mr Shawn Ritchie", validSession.getAccount().getEmail()), MediaType.APPLICATION_JSON));
		
		
		Contact contact
			= convertResponse(response, Contact.class);
		
		assertNotNull(contact);
		assertTrue(validSession.getAccount().getEmail().equals(contact.getContact()));
		
		return contact;
    }
    
    public Contact[] getContacts(Session validSession) throws IOException {	
		WebTarget t = target("account").path("contacts");
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.get();
		
		
		Contact[] contacts
			= convertResponse(response, Contact[].class);
		
		assertNotNull(contacts);

		return contacts;
    }
    
    public Contact getContact(Session validSession, Contact contact) throws IOException {	
		WebTarget t = target("account").path("contact/" + contact.getId());
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.get();
		
		
		Contact getContact
			= convertResponse(response, Contact.class);
		
		assertNotNull(getContact);
		assertTrue(getContact.getId() == contact.getId());
		
		return getContact;
    }
    
    public Contact updateContact(Session validSession, Contact contact) throws IOException {	
    	
    	String updateName = "updatedName";
    	contact.setContactName(updateName);
    	
		WebTarget t = target("account").path("contact/" + contact.getId());
		Response response =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.put(Entity.entity(contact, MediaType.APPLICATION_JSON));
		
		
		Contact updatedContact
			= convertResponse(response, Contact.class);
		
		assertNotNull(updatedContact);
		assertTrue(updatedContact.getContactName().equals(contact.getContactName()));
		
		return updatedContact;
    }
    
    public Boolean deleteContact(Session validSession, Contact contact) throws IOException {	
    	
		WebTarget t = target("account").path("contact/" + contact.getId());
		Boolean deleted =
			t.request()
			.header("Accept", MediaType.APPLICATION_JSON)
			.header("from", validSession.getAccountId())
			.header("Authorization", validSession.getSessionKey())
			.delete(Boolean.class);
		
		assertNotNull(deleted);
		assertTrue(deleted);
		
		return deleted;
    }
    
    
    public Session loginSession(com.property.db.entities.account.Account registeredAccount, String usePassword) throws IOException
    {
    	Session logedinSession = login(registeredAccount, usePassword);
    	assertNotNull(logedinSession);
    	
    	Session refreshedSession = refreshSession(logedinSession);
    	assertNotNull(refreshedSession);
    	
    	return refreshedSession;
    }
    
    public Session loginCycle(com.property.db.entities.account.Account registeredAccount, String usePassword) throws IOException
    {
    	Session session = loginSession(registeredAccount, usePassword);
		assertNotNull(session);
    			
    	Boolean loggedout = logout(session);
    	assertTrue(loggedout);
    	
    	return session;
    }
    
    public void tryToLogin(Session logedinSession, com.property.db.entities.account.Account registeredAccount, String usePassword)
    {
    	refreshExpiredSession(logedinSession);
    	loginIncorrectEmail(usePassword);
    	loginIncorrectPassword(registeredAccount, usePassword);
    }
    
    @Test
    public void TestAccounts() throws IOException
    {
    	com.property.db.entities.account.Account registeredAccount = registerAccount();
    	assertNotNull(registeredAccount);
    	
    	Session activatedSession = activateAccount(registeredAccount);
    	assertNotNull(activatedSession);
    	
    	Session expiredSession = loginCycle(registeredAccount, password);
		assertNotNull(expiredSession);

		tryToLogin(expiredSession, registeredAccount, "incorrect-password");
		
		requestNewPassword(registeredAccount);
		
    	expiredSession = loginCycle(registeredAccount, password);
		assertNotNull(expiredSession);
		
		Session resetSession = resetPassword(registeredAccount);
		assertNotNull(resetSession);
		
    	Session refreshedSession = refreshSession(resetSession);
    	assertNotNull(refreshedSession);
    	
    	Boolean loggedout = logout(refreshedSession);
    	assertTrue(loggedout);
    	
    	tryToLogin(expiredSession, registeredAccount, password);
    	
    	expiredSession = loginCycle(registeredAccount, newPassword);
		assertNotNull(expiredSession);
		
		Session validSession = loginSession(registeredAccount, newPassword);
		assertNotNull(validSession);
		
		Account myAccount = getAccount(validSession);
		assertNotNull(myAccount);
		
//		expiredSession.setSessionKey("fuckin-expired-session-key");
//		getAccountInvalidSessionKey(expiredSession);
		
		Account updatedAccount = updateAccount(validSession);
		assertNotNull(updatedAccount);
		
		deleteAccount(validSession);
		
    	Session reactivatedSession = activateAccount(registeredAccount);
    	assertNotNull(reactivatedSession);
    	
    	Contact addContact = createContact(validSession);
    	assertNotNull(addContact);
    	
    	Contact getContact = getContact(validSession, addContact);
    	assertNotNull(getContact);
    	
    	Contact updateContact = updateContact(validSession, getContact);
    	assertNotNull(updateContact);
    	
    	Contact[] contacts = getContacts(validSession);
    	assertNotNull(contacts);
    	assertTrue(contacts.length == 1);
    	
    	Boolean deleted = deleteContact(validSession, getContact);
    	assertTrue(deleted);
    	
    	contacts = getContacts(validSession);
    	assertNotNull(contacts);
    	assertTrue(contacts.length == 0);
    }
	
    
	
}
