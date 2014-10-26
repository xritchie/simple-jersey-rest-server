package com.property;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/*")
public class ApplicationResourceConfig extends ResourceConfig {
	
	public ApplicationResourceConfig()
	{
		packages("com.property");
		register(org.springframework.web.context.request.RequestContextListener.class);
		register(org.glassfish.jersey.servlet.ServletContainer.class);
		register(org.glassfish.jersey.server.spring.SpringLifecycleListener.class);
		register(org.glassfish.jersey.server.validation.ValidationFeature.class);
		register(org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature.class); 
	}
}
