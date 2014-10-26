package com.property.filters.versioning;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;



@Provider    // register as jersey's provider
@PreMatching //Run before
public class VersionFilter  implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		// TODO Auto-generated method stub
	}


}
