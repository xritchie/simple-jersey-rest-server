package org.resources.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.text.SimpleDateFormat;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import com.property.ApplicationResourceConfig;
import com.property.connections.HibernateSessionFacotry;
import com.property.utility.genson.GensonProvider;


public class GrizzlyWebTest extends JerseyTest {

	protected static SimpleDateFormat sdf = null;
	protected static GensonProvider gensonProvider = null;
	protected static HibernateSessionFacotry sessionFactory = null;

	@Override 
    protected Application configure() { 
            enable(TestProperties.LOG_TRAFFIC); 
            enable(TestProperties.DUMP_ENTITY);
            
            return new ApplicationResourceConfig(); 
    }

	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
	    return new TestContainerFactory() {
	        @Override

	        public TestContainer create(final URI baseUri, ApplicationHandler application) 
        		throws IllegalArgumentException {
	            return new TestContainer() {
	                private HttpServer server;

	                @Override
	                public ClientConfig getClientConfig() {
	                    return null;
	                }

	                @Override
	                public URI getBaseUri() {
	                    return baseUri;
	                }
	                
					@Override
	                public void start() {
	                    try {
	                        this.server = 
	                        		GrizzlyWebContainerFactory.create(baseUri);

	                        WebappContext context = new WebappContext("WebappContext", "");
	                        
	                        context.addContextInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
	                        context.addListener(org.springframework.web.context.ContextLoaderListener.class);

	                        ServletRegistration registration = context.addServlet("ServletContainer", org.glassfish.jersey.servlet.ServletContainer.class);
	                        registration.addMapping("/*");
	                        registration.setInitParameter("jersey.config.server.provider.packages", "com.property");
	                        registration.setInitParameter("com.sun.jersey.config.feature.Trace", "true");

	                        context.deploy(server);	                        
	                    } catch (ProcessingException e) {
	                        throw new TestContainerException(e);
	                    } catch (IOException e) {
	                        throw new TestContainerException(e);
	                    }
	                }

	                @Override
	                public void stop() {
	                    this.server.stop();
	                }
	            };
	        }
	    };
	}
		
	public void setUp() throws Exception {
		super.setUp();
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		assertNotNull(sdf);
		
		gensonProvider = new GensonProvider();
		assertNotNull(gensonProvider);
				
		sessionFactory = new HibernateSessionFacotry();
		assertNotNull(sessionFactory);
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public <T> T convertResponse(Response response, Class<T> classType) throws IOException {
		assertNotNull(response);
		
		StringWriter writer = new StringWriter();
		IOUtils.copy((BufferedInputStream)response.getEntity(), writer, null);
		String payload = writer.toString();

		assertNotNull(payload);
		return gensonProvider.getGenson().deserialize(payload, classType);
	}
	
}
