package com.property.db.entities.account;

public enum AccountType {
	BUYER("buyer"),
	OWNER("owner"),
	FREELANCE("freelance"),
	AGENT("agent"),
	ADMIN("admin");

	private String value;
    
    private AccountType(String value)
    {
       this.value = value;
    }

    public String toString()
    {
       return this.value; //will return , or ' instead of COMMA or APOSTROPHE
    }
}
