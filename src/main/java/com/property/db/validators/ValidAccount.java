package com.property.db.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.property.db.entities.account.Account;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidAccount.Validator.class, ValidAccount.ListValidator.class})
public @interface ValidAccount {

	String message() default "{com.property.validation.constraints.account}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidAccount, Account> {

    	@SuppressWarnings("unused")
		private UriInfo uriInfo;

        public Validator(@Context final UriInfo uriInfo) {
            this.uriInfo = uriInfo;
        }
    	
		@Override
		public void initialize(ValidAccount validAccount) {
		}

		@Override
		public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {
			
			return 
				(account.getId() != null)
				&& (account.getEmail() != null)
				&& (account.getType() != null)
				&& (account.getState() != null)
				&& (account.getPasswordSalt() == null) 
				&& (account.getPassword() == null)
				&& (account.getEncryptCount() == null)
				&& (account.getPersistanceToken() == null)
				&& (account.getPerishableToken() == null)
				&& (account.getSingleAccessToken() == null)
				&& (account.getRegistrationIp() == null);
		}
    }
    
    public class ListValidator implements ConstraintValidator<ValidAccount, List<Account>> {

    	@Context
        private UriInfo uriInfo;

        private Validator validator;

        @Override
        public void initialize(final ValidAccount validAccount) {
            validator = new Validator(uriInfo);
        }

		@Override
		public boolean isValid(List<Account> accounts, ConstraintValidatorContext constraintValidatorContext) {
			boolean isValid = true;
            for (final Account account : accounts) {
                isValid &= validator.isValid(account, constraintValidatorContext);
            }
            return isValid;
		}
    }
}
