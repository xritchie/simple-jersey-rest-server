package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.property.db.entities.sessions.Session;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidSession.Validator.class})
public @interface ValidSession {
	String message() default "{com.property.validation.constraints.session}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidSession, Session> {

		@Override
		public void initialize(ValidSession validSession) {}

		@Override
		public boolean isValid(Session session, ConstraintValidatorContext constraintValidatorContext) 
		{
			return 
					(session !=  null) &&
					(session.getSessionKey() != null) &&
					(session.getAccountId() != null) &&
					(session.isValidSession());
		}
    }
}
