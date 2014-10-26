package com.property.db.entities.sessions;

	
public enum SessionLogoutType {
	LOGOUT("logout"),
	EXPIRED("expired"),
	KICKED("kicked"),
	LOGIN("login");

	private String value;
    
    private SessionLogoutType(String value)
    {
       this.value = value;
    }

    public String toString()
    {
       return this.value;
    }
}
