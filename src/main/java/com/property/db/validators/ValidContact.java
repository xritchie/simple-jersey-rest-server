package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.property.db.entities.contacts.Contact;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidContact.Validator.class, ValidContact.ListValidator.class})
public @interface ValidContact {
	
	String message() default "{com.property.validation.constraints.contact}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidContact, Contact> {

    	@SuppressWarnings("unused")
		private UriInfo uriInfo;

        public Validator(@Context final UriInfo uriInfo) {
            this.uriInfo = uriInfo;
        }
    	
		@Override
		public void initialize(ValidContact validContact) {
		}

		@Override
		public boolean isValid(Contact contact, ConstraintValidatorContext constraintValidatorContext) {
			return 
				(contact.getAccountId() != null)
				&& (contact.getContactName() != null)
				&& (contact.getContact() != null)
				&& (contact.getContactType() != null);
		}
    }
    
    public class ListValidator implements ConstraintValidator<ValidContact, Contact[]> {

    	@Context
        private UriInfo uriInfo;

        private Validator validator;

        @Override
        public void initialize(final ValidContact validContact) {
            validator = new Validator(uriInfo);
        }

		@Override
		public boolean isValid(Contact[] contacts, ConstraintValidatorContext constraintValidatorContext) {
			boolean isValid = true;
            for (final Contact contact : contacts) {
                isValid &= validator.isValid(contact, constraintValidatorContext);
            }
            return isValid;
		}
    }
}
