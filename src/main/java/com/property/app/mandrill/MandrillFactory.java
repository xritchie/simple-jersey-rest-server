package com.property.app.mandrill;

import org.springframework.stereotype.Component;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.property.utility.SpringPropertiesUtil;

@Component("mandrillFactory")
public class MandrillFactory {
	
	MandrillApi mandrillApi = null;
	
	public MandrillFactory()
	{	
		createMandrillInstance();
	}
		
	public MandrillApi getMandrillApi() {
		return mandrillApi;
	}

	public void setMandrillApi(MandrillApi mandrillApi) {
		this.mandrillApi = mandrillApi;
	}
	
	private static String getMandrillApiKey()
	{
		return SpringPropertiesUtil.getProperty("mandrill-api-key");
	}

	public void createMandrillInstance()
	{
		if (mandrillApi == null)
			mandrillApi = new MandrillApi(getMandrillApiKey());
	}
}
