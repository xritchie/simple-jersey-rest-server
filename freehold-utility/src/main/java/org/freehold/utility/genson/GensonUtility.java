package org.freehold.utility.genson;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.ClassPath;

import com.owlike.genson.Converter;
import com.owlike.genson.Factory;

public class GensonUtility {
	public static List<Converter<?>> loadGensonConverters(String fetchFromPackage) throws IOException
	{
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		List<Converter<?>> converters = new ArrayList<Converter<?>>();
		for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
		  if (info.getName().startsWith(fetchFromPackage)) {
		    final Class<?> clazz = info.load();
		    
		    Constructor<?> ctor;
			try {
				ctor = clazz.getConstructor();
				Converter<?> converter = (Converter<?>)ctor.newInstance();
				converters.add(converter);
			} catch (Exception e) {
				continue;
			}
		  }
		}
		
		return converters;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Factory<Converter<?>>> loadGensonFactories(String fetchFromPackage) throws IOException
	{
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		List<Factory<Converter<?>>> factories = new ArrayList<Factory<Converter<?>>>();
		for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
		  if (info.getName().startsWith(fetchFromPackage)) {
		    final Class<?> clazz = info.load();
		    Constructor<?> ctor;
			try {
				ctor = clazz.getConstructor();
				Factory<Converter<?>> factory = (Factory<Converter<?>>)ctor.newInstance();
				factories.add(factory);
			} catch (Exception e) {
				continue;
			}
		  }
		}
		
		return factories;
	}
}
