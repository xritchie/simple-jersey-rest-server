package com.property.filters;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import com.property.filters.auditing.Audit;
import com.property.filters.auditing.AuditingFilter;
import com.property.filters.versioning.Version;
import com.property.filters.versioning.VersionFilter;

@Provider
public class ResourceFilterBindingFeature implements DynamicFeature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {
    	
    	 AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
    	 
    	context.register(com.property.filters.security.SecurityFilter.class);
    	
    	if (am.isAnnotationPresent(Audit.class))
    		context.register(AuditingFilter.class);
    	
    	if (am.isAnnotationPresent(Version.class))
    		context.register(VersionFilter.class);
    }
}
