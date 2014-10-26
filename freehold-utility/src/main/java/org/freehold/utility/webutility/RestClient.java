package org.freehold.utility.webutility;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.owlike.genson.Genson;

public class RestClient {

	private static Client client = null;
	public static RestClient instance = new RestClient();
	
	public RestClient() 
	{
		RestClient.client =  ClientBuilder.newClient();
	}
	
	public static RestClient getInstance() {
		if (instance == null)
			instance = new RestClient();
		
		return instance;
	}

	public static Client getClient() {
		return client;
	}
	public static void setClient(Client client) {
		RestClient.client = client;
	}
	
	private static void appendHeader(Invocation.Builder invocationBuilder, MultivaluedMap<String, Object> headers)
	{
		for(Map.Entry<String, List<Object>> entry : headers.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
	}

	private static Invocation.Builder getInvocationBuilder(String url)
	{
		RestClient.getInstance();
		WebTarget webTarget = RestClient.getClient().target(url);
		return webTarget.request(MediaType.APPLICATION_JSON);
	}
	
	private static Invocation.Builder createInvocationilder(String url, MultivaluedMap<String, Object> headers)
	{
		Invocation.Builder invocationBuilder = getInvocationBuilder(url);
		if (headers != null)
			appendHeader(invocationBuilder, headers);
		
		return invocationBuilder;
	}
	
	public static Response get(String url, MultivaluedMap<String, Object> headers)
	{
		Invocation.Builder invocationBuilder = createInvocationilder(url, headers);
		return invocationBuilder.get();
	}
	
	public static Response get(String url)
	{
		return get(url, null);
	}
	
	public static Response post(String url, String payload, MultivaluedMap<String, Object> headers)
	{
		Invocation.Builder invocationBuilder = createInvocationilder(url, headers);
		return invocationBuilder.post(Entity.json(payload));
	}
	
	public static Response post(String url, String payload)
	{
		return post(url, payload, null);
	}

	public static Response put(String url, String payload, MultivaluedMap<String, Object> headers)
	{
		Invocation.Builder invocationBuilder = createInvocationilder(url, headers);
		return invocationBuilder.put(Entity.json(payload));				
	}
	
	public static Response put(String url, String payload)
	{
		return put(url, payload, null);				
	}

	public static Response delete(String url, MultivaluedMap<String, Object> headers)
	{
		Invocation.Builder invocationBuilder = createInvocationilder(url, headers);
		return invocationBuilder.delete();
	}
	
	public static Response delete(String url)
	{
		return delete(url, null);
	}
	
	public static String convertClientResponseToString(Response response, Genson genson)
	{
		if (response != null)
		{
			Object obj = response.getEntity();
			if (obj != null)
				return genson.serialize(obj);
		}
		return null;
	}
	
}
