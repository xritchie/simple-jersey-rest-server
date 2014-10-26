package com.property.exception.mappers;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException ex) {
	    return Response.status(Response.Status.PRECONDITION_FAILED).
	      entity(ex.getMessage()).
	      type("text/plain").
	      build();
  }
}
