package com.property.utility;

public class ConfigUtility {

	public static Boolean isSingleSessionEnabled()
	{
		String singleSessionEnabled = SpringPropertiesUtil.getProperty("single-session");
		return singleSessionEnabled == null ? true : Boolean.valueOf(singleSessionEnabled);
	}
	
	public static Boolean isDebugMode()
	{
		String debugMode = SpringPropertiesUtil.getProperty("debug-mode");
		return debugMode == null ? false : Boolean.valueOf(debugMode);
	}
	
	public static Long getSessionExpiration()
	{
		String sessionExpiration = SpringPropertiesUtil.getProperty("session-timeout");
		return sessionExpiration == null ? 0L : Long.valueOf(sessionExpiration);
	}
}
