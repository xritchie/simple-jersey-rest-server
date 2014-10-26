package com.property.utility;

import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("utilityObjects")
@Qualifier("default")
@Scope("singleton")
public class UtilityObjects {

	private Properties configProp = null;
	private SimpleDateFormat sdf = null;
	
	public UtilityObjects()
	{	
		createConfigProperties();
		createSimpleDateFormater();
	}
		
	@PostConstruct
	public void init()
	{
	}
	
	@PreDestroy
	public void destroy()
	{
	}
	
	public Properties getConfigProp() {
		createConfigProperties();
		return configProp;
	}

	public SimpleDateFormat getSdf() {
		createSimpleDateFormater();
		return sdf;
	}

	public void createConfigProperties()
	{
		if (configProp == null)
			configProp = null;
	}
	
	public void createSimpleDateFormater()
	{
		if (sdf == null)
			sdf = new SimpleDateFormat("dd/MM/yyyy");
	}
}
