package com.property.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class PreconditionFailedException extends WebApplicationException{
	private static final long serialVersionUID = 412L;  // unique id
	
    /**
     * Create a HTTP 403 (Not Found) exception.
     */
    public PreconditionFailedException() {
    	super
    	(
			Response
				.status(Response.Status.PRECONDITION_FAILED)
				.build()
		);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public PreconditionFailedException(String message) {
        super
        (
    		Response
    			.status(Response.Status.FORBIDDEN)
    			.entity(message)
    			.type("text/plain")
    			.build()
		);
    }
}
