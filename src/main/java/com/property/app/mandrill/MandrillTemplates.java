package com.property.app.mandrill;

public enum MandrillTemplates {
	ACTIVATION("activation"),
	WELCOME("welcome"),
	FORGOTPASSWORD("forgot-password"),
	PASSWORDRESET("reset-password"),
	ACCOUNTDELETED("account-deleted");

	private String value;
    
    private MandrillTemplates(String value)
    {
       this.value = value;
    }

    public String toString()
    {
       return this.value; //will return , or ' instead of COMMA or APOSTROPHE
    }
}
