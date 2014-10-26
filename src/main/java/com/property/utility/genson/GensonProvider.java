package com.property.utility.genson;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.elasticlysearchable.elasticsearch.utility.ESUtility;
import org.freehold.utility.genson.GensonUtility;

import com.owlike.genson.Converter;
import com.owlike.genson.Factory;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;


@Provider
public class GensonProvider implements ContextResolver<Genson>  {
	private static Genson genson = null;

	public GensonProvider() throws IOException
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

	
	@Override
	public Genson getContext(Class<?> type) {
		if (genson == null)
			try {
				createGensonInstance();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return genson;
	}

	
	public Genson getGenson() {
		return genson;
	}
	
	public static GensonBuilder generateBuilder()  throws IOException
	{
		GensonBuilder builder = 
				new GensonBuilder()
					.useFields(true, VisibilityFilter.PRIVATE)
					.setMethodFilter(VisibilityFilter.PACKAGE_PUBLIC)
					.setSkipNull(true)
					.useRuntimeType(true);

			//GET ES FACTORIES
			List<Factory<Converter<?>>> factories = ESUtility.loadGensonFactories();
			for(Factory<Converter<?>> factoty : factories)
				builder.withConverterFactory(factoty);
			
			//GET ES CONVERTERS
			List<Converter<?>> esConverters = ESUtility.loadGensonConverters();
			builder.withConverters(esConverters.toArray(new Converter<?>[esConverters.size()]));

			//GET RESOURCE CONVERTERS
			List<Converter<?>> resourceConverters = GensonUtility.loadGensonConverters("com.property.db.entities.converters.");
			builder.withConverters(resourceConverters.toArray(new Converter<?>[resourceConverters.size()]));
			
			return builder;
	}

	private void createGensonInstance() throws IOException
	{
		if (genson != null)
			return; 
		
		GensonBuilder builder = generateBuilder();
		genson = builder.create();
	}

}
