package com.property.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 404L;  // unique id
	
    /**
     * Create a HTTP 404 (Not Found) exception.
     */
    public NotFoundException() {
        super
        (
    		Response
    			.status(Response.Status.NOT_FOUND)
    			.build()
		);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public NotFoundException(String message) {
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