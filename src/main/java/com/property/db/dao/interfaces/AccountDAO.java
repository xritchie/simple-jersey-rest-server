package com.property.db.dao.interfaces;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.googlecode.genericdao.dao.hibernate.original.GenericDAO;
import com.property.db.entities.account.Account;
import com.property.db.entities.account.AccountType;

public interface AccountDAO extends GenericDAO<Account, Integer> {

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
	) throws NoSuchAlgorithmException;
	
	public Account registerAccount
	(
		Account account
	) throws NoSuchAlgorithmException;
	
	public Account getAccount
	(
		Integer accountId
	);
	
	public Account getAccount
	(
		String email
	);

	public Account deleteAccount
	(
		Integer accountId
	);
	
	public Account updateAccount
	(
		Account account
	);
	
	public Account resetPassword
	(
		Integer accountId,
		String confirmationCode,
		String password
	) throws NoSuchAlgorithmException;
	
	public String getActivationCode
	(
		Integer accountId
	);
	
	public Account activateAccount
	(
		Integer accountId,
		String activationCode
	);
	
	public Account resetConfirmationCode
	(
		String email
	);
	
	public Account resetConfirmationCode
	(
		Integer accountId
	);
}
