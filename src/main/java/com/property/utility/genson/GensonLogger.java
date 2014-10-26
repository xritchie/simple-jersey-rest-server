package com.property.utility.genson;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.property.utility.genson.converters.ContextualConverterFactory;

@Component("gensonLogger")
@Qualifier("default")
@Scope("singleton")
public class GensonLogger {
	private Genson genson = null;

	public GensonLogger() throws IOException
	{
		createGensonInstance();
	}

	@PostConstruct
	public void init()
	{
	}
	
	@PreDestroy
	public void destroy()
	{
	}

	public Genson getGenson() {
		return genson;
	}

	private void createGensonInstance() throws IOException
	{
		if (genson != null)
			return; 
		
		GensonBuilder builder = 
				GensonProvider
					.generateBuilder()
					.withContextualFactory(new ContextualConverterFactory());
				
		genson = builder.create();
	}

}
