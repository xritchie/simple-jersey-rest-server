package com.property.resources;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import javax.annotation.security.PermitAll;

import com.property.db.entities.sessions.SessionBody;
import com.property.db.validators.ValidSession;
import com.property.db.validators.ValidSessionBody;
import com.property.exceptions.UnauthorizedException;
import com.property.filters.auditing.Audit;

@Service
@Consumes( MediaType.APPLICATION_JSON )
@Produces( { MediaType.APPLICATION_JSON })
@PermitAll
public interface Session {

	@POST
	@Audit
	@NotNull @ValidSession
	@Path("login")
	public com.property.db.entities.sessions.Session login
	(
		@NotNull @ValidSessionBody SessionBody sessionBody,
		@Context HttpServletRequest request
	) throws UnauthorizedException, NoSuchAlgorithmException;
	
	@POST
	@Audit
	@Path("logout")
	public Boolean logout
	(
		@Context HttpHeaders httpHeaders
	);

	@GET
	@Audit
	public com.property.db.entities.sessions.Session refreshSession
	(
		@Context HttpHeaders httpHeaders
	);
}
