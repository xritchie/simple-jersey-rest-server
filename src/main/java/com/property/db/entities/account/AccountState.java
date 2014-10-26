package com.property.db.entities.account;

public enum AccountState {
	PENDING("pending"),
	ACTIVE("active"),
	INACTIVE("inactive"),
	BLOCKED("blocked"),
	DELETED("deleted");

	private String value;
    
    private AccountState(String value)
    {
       this.value = value;
    }

    public String toString()
    {
       return this.value; //will return , or ' instead of COMMA or APOSTROPHE
    }
}
