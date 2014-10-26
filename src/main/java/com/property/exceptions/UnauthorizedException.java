package com.property.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UnauthorizedException extends WebApplicationException{
	private static final long serialVersionUID = 401L;  // unique id
	
    /**
     * Create a HTTP 401 (Unauthorized) exception.
     */
    public UnauthorizedException() {
    	super
    	(
			Response
				.status(Response.Status.UNAUTHORIZED)
				.build()
		);
    }

    /**
     * Create a HTTP 401 (Unauthorized) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public UnauthorizedException(String message) {
        super
        (
    		Response
    			.status(Response.Status.UNAUTHORIZED)
    			.entity(message)
    			.type("text/plain")
    			.build()
		);
    }
}
