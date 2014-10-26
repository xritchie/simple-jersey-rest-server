package com.property.utility.genson.converters;

import java.io.IOException;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

public class CensorConverter implements Converter<String> {
	
	public static CensorConverter instance = new CensorConverter();
	public CensorConverter() {}
	
	public static CensorConverter getInstance() {
		return instance;
	}

	@Override
	public String deserialize(ObjectReader reader, Context ctx) 
			throws IOException 
	{
		return null;
	}

	@Override
	public void serialize(String password, ObjectWriter writer, Context ctx)
		throws IOException 
	{
		writer.writeValue(password.replaceAll(".", "*"));
	}
}
