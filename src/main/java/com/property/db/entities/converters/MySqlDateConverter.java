package com.property.db.entities.converters;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

public class MySqlDateConverter implements Converter<Date> {
	
	private final static SimpleDateFormat outputDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private final static SimpleDateFormat[] dateFormaters = new SimpleDateFormat[]
			{
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
				new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH),
			};
	
	public final static MySqlDateConverter instance = new MySqlDateConverter();
	public MySqlDateConverter() {}
	  
	@Override
	public Date deserialize(ObjectReader reader, Context ctx) 
	{
		for(SimpleDateFormat dateFormater :dateFormaters)
		{
	        try
	        {
	        	java.util.Date date = dateFormater.parse(reader.valueAsString());
	        	return new Date(date.getTime());
	        }
	        catch (ParseException e) {}
	    }
		return null;
	}

	@Override
	public void serialize(Date date, ObjectWriter writer, Context ctx)
		throws IOException 
	{
		writer.writeValue(outputDateFormater.format(date));
	}
}
