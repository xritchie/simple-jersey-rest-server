package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.property.db.entities.account.Account;
import com.property.db.entities.account.AccountType;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidPostAccount.Validator.class})
public @interface ValidPostAccount {
	String message() default "{com.property.validation.constraints.account}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidPostAccount, Account> {

		@Override
		public void initialize(ValidPostAccount validAccount) {
		}

		@Override
		public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {
			
			return 
				(account.getEmail() != null)
				&& (account.getType() != null)
				&& (account.getType() != AccountType.ADMIN)
				&& (account.getAlias() != null)
				&& (account.getFirstName() != null)
				&& (account.getLastName() != null);
		}
    }
}
