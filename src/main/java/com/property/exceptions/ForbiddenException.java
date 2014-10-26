package com.property.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForbiddenException extends WebApplicationException {

	private static final long serialVersionUID = 403L;  // unique id
	
    /**
     * Create a HTTP 403 (Not Found) exception.
     */
    public ForbiddenException() {
    	super
    	(
			Response
				.status(Response.Status.FORBIDDEN)
				.build()
		);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public ForbiddenException(String message) {
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