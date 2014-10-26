package com.property.db.entities.contacts;

public enum ContactType {
	EMAIL("email"),
	LANDLINE("landline"),
	MOBILE("mobile"),
	SKYPE("skype");

	private String value;
    
    private ContactType(String value)
    {
       this.value = value;
    }

    public String toString()
    {
       return this.value;
    }
}
