package com.property.resources;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.property.db.entities.sessions.Session;
import com.property.db.entities.sessions.SessionBody;
import com.property.db.validators.ValidContact;
import com.property.db.validators.ValidPostAccount;
import com.property.db.validators.ValidAccount;
import com.property.db.validators.ValidResetEmail;
import com.property.db.validators.ValidResetPassword;
import com.property.db.validators.ValidSession;
import com.property.filters.auditing.Audit;

@Service
@Consumes( MediaType.APPLICATION_JSON )
@Produces( { MediaType.APPLICATION_JSON })
public interface Account {

	@POST
	@Audit
	@NotNull @ValidAccount
	public com.property.db.entities.account.Account createAccount
	(
		@NotNull @ValidPostAccount final com.property.db.entities.account.Account account,
		@Context HttpServletRequest request
	) throws NoSuchAlgorithmException, MandrillApiError, IOException;
	
	@POST
	@Audit
	@NotNull @ValidSession
	@Path("{account-id}/activate/{activation-code}")
	public Session activateAccount
	(
		@NotNull @Min(1) @PathParam("account-id") Integer accountId,
		@NotNull @Length(min = 32, max = 32) @PathParam("activation-code") String activationCode,
		@Context HttpServletRequest request
	) throws MandrillApiError, IOException;
	
	
	@POST
	@Audit
	@Path("forgot-password")
	public Boolean requestPasswordChange
	(
		@NotNull @ValidResetEmail @Valid SessionBody resetPassword,
		@Context HttpServletRequest request
	) throws MandrillApiError, IOException;
	
	@POST
	@Audit
	@NotNull @ValidSession
	@Path("{account-id}/reset/{confirmation-code}")
	public Session resetPassword
	(
		@NotNull @Min(1) @PathParam("account-id") Integer accountId,
		@NotNull @Length(min = 32, max = 32) @PathParam("confirmation-code") String activationCode,
		@NotNull @ValidResetPassword SessionBody resetPassword,
		@Context HttpServletRequest request
	) throws NoSuchAlgorithmException, MandrillApiError, IOException;
	
	@GET
	@Audit
	@NotNull @ValidAccount
	@PermitAll
	public com.property.db.entities.account.Account getAccount
	(
			@Context HttpHeaders httpHeaders
	);
	
	@PUT
	@Audit
	@NotNull @ValidAccount
	@PermitAll
	public com.property.db.entities.account.Account updateAccount
	(
		@NotNull final com.property.db.entities.account.Account account
	);
	
	@DELETE
	@Audit
	@NotNull
	@PermitAll
	public Boolean deleteAccount
	(
		@Context HttpHeaders httpHeaders
	) throws MandrillApiError, IOException;

	
	
	@GET
	@Audit
	@Path("contacts")
	@NotNull @ValidContact
	@PermitAll
	public com.property.db.entities.contacts.Contact[] getContacts
	(
		@Context HttpHeaders httpHeaders
	);

	@POST
	@Audit
	@Path("contact")
	@NotNull @ValidContact
	@PermitAll
	public com.property.db.entities.contacts.Contact addContact
	(
		@NotNull @ValidContact final com.property.db.entities.contacts.Contact contact,
		@Context HttpHeaders httpHeaders
	);
	
	@GET
	@Audit
	@Path("contact/{contact-id}")
	@NotNull @ValidContact
	@PermitAll
	public com.property.db.entities.contacts.Contact getContact
	(
		@NotNull @Min(1) @PathParam("contact-id") Long contactId,
		@Context HttpHeaders httpHeaders
	);
	
	@PUT
	@Audit
	@Path("contact/{contact-id}")
	@NotNull @ValidContact
	@PermitAll
	public com.property.db.entities.contacts.Contact updateContact
	(
		@NotNull @ValidContact final com.property.db.entities.contacts.Contact contact,
		@NotNull @Min(1) @PathParam("contact-id") Long contactId,
		@Context HttpHeaders httpHeaders
	);
	
	@DELETE
	@Audit
	@Path("contact/{contact-id}")
	@PermitAll
	public Boolean deleteContact
	(
		@NotNull @Min(1) @PathParam("contact-id") Long contactId,
		@Context HttpHeaders httpHeaders
	);
}
