package org.freehold.utility.webutility;

import javax.ws.rs.core.Response;

public class CustomClientResponse {
		
	private Response response = null;
	
	public CustomClientResponse() {
		super();
	}
	
	public CustomClientResponse(Response response) {
		super();
		this.setResponse(response);
	}

	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}

	public Integer getStatus()
	{
		if (this.getResponse() != null)
			return this.getResponse().getStatus();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> c)
	{
		if (this.getResponse() != null)
			return (T)this.getResponse().getEntity();
		return null;
	}

}
