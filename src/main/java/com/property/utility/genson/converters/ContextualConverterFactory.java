package com.property.utility.genson.converters;

import com.owlike.genson.Converter;
import com.owlike.genson.Genson;
import com.owlike.genson.convert.ContextualFactory;
import com.owlike.genson.reflect.BeanProperty;
import com.property.utility.genson.annotations.Censor;

public class ContextualConverterFactory implements ContextualFactory<String>
{
	 @Override
	 public Converter<String> create(BeanProperty property, Genson genson) {
		Censor ann = property.getAnnotation(Censor.class);
		if (ann != null) 
			return CensorConverter.getInstance();
		else 
			return null;
	 }
}
