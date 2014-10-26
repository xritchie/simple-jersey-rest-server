package com.property.db.dao.impls;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.property.db.dao.interfaces.AccountDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.account.AccountState;
import com.property.db.entities.account.AccountType;
import com.property.db.entities.contacts.Contact;
import com.property.db.hibernate.FreeholdGenericDAOImpl;
import com.property.exceptions.InternalServerException;
import com.property.security.SecurityUtility;

public class AccountDAOImpl 
	extends FreeholdGenericDAOImpl<Account, Integer> 
		implements AccountDAO 
{
	private static Integer noIterationsFrom = 1000;
	private static Integer noIterationsTo = 1500;
		
	@Override
	public Account registerAccount(Account account) 
		throws NoSuchAlgorithmException
	{
		String salt = SecurityUtility.generateSalt();
		String password = account.getPassword() != null ? account.getPassword() : SecurityUtility.generateSalt();
		Integer noIterations = SecurityUtility.generateRandomNumber(noIterationsFrom, noIterationsTo);
		
		account.setSingleAccessToken(SecurityUtility.generateGUIDWithoutDashes());
		
		account.setState(AccountState.ACTIVE);
		account.setPasswordSalt(salt);
		account.setEncryptCount(noIterations);
		account.setPassword(SecurityUtility.hashPassword(password, salt, noIterations));
			
		Session session = this.getSession();	
		Transaction tx = session.beginTransaction();
		session.persist(account);
		session.flush();
		tx.commit();

		return account;
	}
	
	@Override
	public Account registerAccount
	(
		AccountType accountType, 
		String email,
		String password, 
		String registrationIP, 
		String alias,
		String firstName, 
		String lastName, 
		Date dob, 
		Boolean promotionalInfo
	) 
		throws NoSuchAlgorithmException
	{
		Account account = 
				new Account
				(
					null, 
					AccountState.PENDING,
					accountType,
					email,
					null,
					password,
					null,
					null,
					null,
					null,
					registrationIP,
					alias,
					firstName,
					lastName,
					dob,
					promotionalInfo,
					new ArrayList<Contact>()
				);
		
		return registerAccount(account);
	}

	@Override
	public Account getAccount(Integer accountId)
	{	
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		Account account = this.fetch(accountId);
		tx.commit();
		
		return account;
	}
	
	public Account getAccount(String email)
	{
		Account account = null;
		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		account = getAccount(session, email);
		tx.commit();
		
		return account;
	}
	
	private Account getAccount(Session session, Integer accountId, String confirmationCode)
	{
		String hql = "FROM Account A WHERE A.id=:accountId AND A.singleAccessToken=:confirmationCode AND A.deleted = 0";
		Query query = session.createQuery(hql);
		query.setInteger("accountId", accountId);
		query.setString("confirmationCode", confirmationCode);
		return (Account)query.uniqueResult();
	}

	public Account getAccount(Session session, String email)
	{
		String hql = "FROM Account A WHERE A.email=:email AND A.deleted = 0";

		Query query = session.createQuery(hql);
		query.setString("email", email);
		return (Account)query.uniqueResult();
	}
	
	@Override
	public Account deleteAccount(Integer accountId)
	{
		Session session = this.getSession();
		
		Transaction tx = session.beginTransaction();
		
		Account originalAccount = this.fetch(accountId);
		originalAccount.setState(AccountState.DELETED);
		originalAccount.setSingleAccessToken(SecurityUtility.generateGUIDWithoutDashes());
		this.update(originalAccount);
		
		tx.commit();
				
		return originalAccount;
	}

	@Override
	public Account updateAccount
	(
		Account account
	)
	{
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		Account originalAccount = this.fetch(account.getId());
		tx.commit();
		
		if (account.getFirstName() != null)
			originalAccount.setFirstName(account.getFirstName());
		
		if (account.getLastName() != null)
			originalAccount.setLastName(account.getLastName());
		
		if (account.getDob() != null)
			originalAccount.setDob(account.getDob());
		
		if (account.getAlias() != null)
			originalAccount.setAlias(account.getAlias());
		
		session = this.getSession();
		tx = session.beginTransaction();
		this.update(originalAccount);
		Account updatedAccount = this.fetch(originalAccount.getId());
		tx.commit();

		return updatedAccount;
	}
	
	@Override
	public Account resetPassword
	(
		Integer accountId,
		String confirmationCode,
		String password
	) throws NoSuchAlgorithmException
	{
		String salt = SecurityUtility.generateSalt();
		Integer noIterations = SecurityUtility.generateRandomNumber(noIterationsFrom, noIterationsTo);
		String hashedPassword = SecurityUtility.hashPassword(password, salt, noIterations);
		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		Account updateAccount = getAccount(session, accountId, confirmationCode);
		if (updateAccount != null)
		{
			updateAccount.setPasswordSalt(salt);
			updateAccount.setEncryptCount(noIterations);
			updateAccount.setPassword(hashedPassword);
			updateAccount.setSingleAccessToken(SecurityUtility.generateGUIDWithoutDashes());
			this.update(updateAccount);
		}
		tx.commit();

		if (updateAccount == null)
			throw new InternalServerException();
		
		return updateAccount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getActivationCode
	(
		Integer accountId
	)
	{
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		List<String> activationCode = 
				session
				.createCriteria(Account.class)
				.setProjection(Projections.property("singleAccessToken"))
				.list();
		tx.commit();
		
		return activationCode != null ? activationCode.get(0) : null;
	}
	
	@Override
	public Account activateAccount
	(
		Integer accountId,
		String activationCode
	)
	{		
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		Account updateAccount = getAccount(session, accountId, activationCode);
		if (updateAccount != null)
		{
			updateAccount.setState(AccountState.ACTIVE);
			updateAccount.setSingleAccessToken(SecurityUtility.generateGUIDWithoutDashes());
			this.update(updateAccount);
		}
		tx.commit();

		if (updateAccount == null)
			throw new InternalServerException();
		
		return updateAccount;
	}

	
	@FunctionalInterface
	public interface GetAccountFunction
	{
		public Account getAccount(Session session);
	}
	
	private Account genericResetConfirmationCode(GetAccountFunction getAccountFx)
	{
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		
		Account account = getAccountFx.getAccount(session);
		
		account.setSingleAccessToken(SecurityUtility.generateGUIDWithoutDashes());
		this.update(account);
		Account updatedAccount = this.fetch(account.getId());
		
		tx.commit();

		return updatedAccount;
	}
	
	@Override
	public Account resetConfirmationCode(String email) {
		GetAccountFunction getAccountFx = (Session session) -> { return this.getAccount(session, email); };
		return genericResetConfirmationCode(getAccountFx);
	}
	
	@Override
	public Account resetConfirmationCode(Integer accountId) {
		GetAccountFunction getAccountFx = (Session session) -> { return this.fetch(accountId); };
		return genericResetConfirmationCode(getAccountFx);
	}
}
