package com.property.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InternalServerException  extends WebApplicationException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Create a HTTP 403 (Not Found) exception.
     */
    public InternalServerException() {
    	super
        (
    		Response
    			.status(Response.Status.INTERNAL_SERVER_ERROR)
    			.build()
		);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public InternalServerException(String message) {
    	super
		(
			Response
			.status(Response.Status.NOT_FOUND)
            .entity(message)
            .type("text/plain")
            .build()
        );
    }
}
