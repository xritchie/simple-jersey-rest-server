package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.hibernate.validator.constraints.Email;

import com.property.db.entities.sessions.SessionBody;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidResetEmail.Validator.class})
public @interface ValidResetEmail {
	String message() default "{com.property.validation.constraints.reset-email}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidResetEmail, SessionBody> {

		@Override
		public void initialize(ValidResetEmail validResetEmail) {
		}

		@Override
		public boolean isValid(SessionBody sessionBody, ConstraintValidatorContext constraintValidatorContext) {

			return 
				(sessionBody.getEmail() != null);// && (ValidEmail.Validator.isValid(sessionBody.getEmail()));
		}
    }
}
