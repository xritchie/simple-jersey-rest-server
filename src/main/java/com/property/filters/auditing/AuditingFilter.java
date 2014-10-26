package com.property.filters.auditing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.mongodb.util.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.property.connections.MongoServer;
import com.property.utility.ConfigUtility;
import com.property.utility.genson.GensonLogger;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.PreMatching;

//@Provider    					//Is used to register the filter with jersey since were using dynamic features it is not required
@PreMatching 					//Run before
public class AuditingFilter implements ContainerRequestFilter, ContainerResponseFilter  {

	@Autowired
	@Qualifier("default")
	private MongoServer mongoServer;
	
	@Autowired
	@Qualifier("default")
	private GensonLogger gensonLogger;

	private final AtomicLong _id = new AtomicLong(0);

	private void AppendContainer(final Map<String, Object> requestContainer, final Map<String, String> requestData) {
		if (requestData != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = requestData.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				requestContainer.put((String) pairs.getKey(), pairs.getValue());
			}
		}
	}
	
	private void AppendContainer(final Map<String, Object> requestContainer, final MultivaluedMap<String, String> requestData) {
		if (requestData != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = requestData.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (!requestContainer.containsKey(pairs.getKey()))
					requestContainer.put((String) pairs.getKey(), pairs.getValue());
			}
		}
	}

	public Object DeserializeEntity(ContainerRequestContext request, Boolean isRequest) throws Exception {
		try {
			StringWriter writer = new StringWriter();
			InputStream stream = request.getEntityStream();
			if (stream != null)
			{
				IOUtils.copy(stream, writer, "UTF8");
				String postObjStr = writer.toString();
				if (isRequest)
					request.setEntityStream(new ByteArrayInputStream(postObjStr.getBytes()));
	
				Object obj = gensonLogger.getGenson().deserialize(postObjStr, Object.class);
				return obj;
			}
			
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	public void ExtractException(Map<String, Object> container, Throwable throwable)
	{
		if (throwable != null)
		{
			container.put("log-err", throwable.getMessage());
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
			container.put("log-stacktrace", sw.toString());
		}
	}
	
	public void GenerateRequest(Map<String, Object> requestContainer, ContainerRequestContext request, Boolean isRequest) throws Exception
	{
		requestContainer.put("transection-id", (Long)request.getProperty("Transection-Id"));
		requestContainer.put("thread-id", Thread.currentThread().getName());
		requestContainer.put("method", request.getMethod());
		requestContainer.put("uri", request.getUriInfo().getRequestUri().toASCIIString());
		requestContainer.put("started", (Date)request.getProperty("started"));
		requestContainer.put("ended", (Date)request.getProperty("ended"));
		requestContainer.put("took", (Long)request.getProperty("took"));

		AppendContainer(requestContainer, request.getHeaders());
		AppendContainer
		(
				requestContainer, 
				request
					.getCookies()
					.entrySet()
					.stream()
					.map(v -> new AbstractMap.SimpleEntry<String, String>(v.getKey(), (gensonLogger.getGenson().serialize(v.getValue()))))
					.collect(Collectors.toMap(v -> v.getKey(), v -> v.getValue()))
		);

		Object obj = DeserializeEntity(request, isRequest);
		if (obj != null)
			requestContainer.put("entity", obj);
	}
			
	public void GenerateResponse(Map<String, Object> responseContainer, ContainerResponseContext response) throws IOException
	{
		responseContainer.put("thread-id", Thread.currentThread().getName());
		responseContainer.put("status", response.getStatus());
		if (response.getStatusInfo() != null) responseContainer.put("status-type-code",  response.getStatusInfo().getStatusCode());
		if (response.getStatusInfo() != null) responseContainer.put("status-type-reason",  response.getStatusInfo().getReasonPhrase());
		if (response.getMediaType() != null) responseContainer.put("content-type", response.getMediaType().toString());
		if (response.getEntityType() != null) responseContainer.put("entity-type", response.getEntityType().toString());
		
		responseContainer.put("http-headers", response.getHeaders());
		
		//ExtractException(responseContainer, response.getMappedThrowable());

		Object entity = response.getEntity();
		if (entity != null)
			responseContainer.put("entity", entity);
	}
	
	public void WriteRequestContainer(DBCollection collection, final Map<String, Object> requestContainer) {
		if ((requestContainer != null) && (collection != null)) {
			try {				
				String json = gensonLogger.getGenson().serialize(requestContainer);
				BasicDBObject toSave = (BasicDBObject)JSON.parse(json);
				collection.save(toSave, WriteConcern.ACKNOWLEDGED);
			} catch (Exception e) {
				System.out.println("-- Unable to Log --");
				System.out.println("Error: " + e.getMessage());
				System.out.println("StackTrace: ");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void filter(ContainerRequestContext request) 
		throws IOException 
	{
		final long id = this._id.incrementAndGet();
		Map<String, Object> requestContainer = new HashMap<String, Object>();
		
		try
		{
			request.setProperty("Transection-Id", id);
			request.setProperty("started", new Date());
			GenerateRequest(requestContainer, request, true);
		}
		catch (Exception ex)
		{
			if (ConfigUtility.isDebugMode())
				ex.printStackTrace();
			
			requestContainer.put("log-err", ex.getMessage());
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			requestContainer.put("log-stacktrace", sw.toString());
		} finally {
			WriteRequestContainer(mongoServer.GetRequestCollection(), requestContainer);
		}
	}

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) 
			throws IOException 
	{
		Map<String, Object> globalContainer = new HashMap<String, Object>();
		Map<String, Object> requestContainer = new HashMap<String, Object>();
		Map<String, Object> responseContainer = new HashMap<String, Object>();
		
		Date ended = new Date();
		Date started = (Date)request.getProperty("started");
		Long differenceInMillis = null;
		if ((ended != null) && (started != null))
			differenceInMillis = ended.getTime() - started.getTime();
		
		if (ended != null) request.setProperty("ended", ended);
		if (differenceInMillis != null) request.setProperty("took", differenceInMillis);

		try {			
			GenerateRequest(requestContainer, request, false);
			GenerateResponse(responseContainer, response);
			
			globalContainer.put("request", requestContainer);
			globalContainer.put("response", responseContainer);
		} catch (Exception ex) {
			if (ConfigUtility.isDebugMode())
				ex.printStackTrace();
			
			ExtractException(globalContainer, ex);
		} finally {
			WriteRequestContainer(mongoServer.GetResponseCollection(), globalContainer);
		}
	}

}
