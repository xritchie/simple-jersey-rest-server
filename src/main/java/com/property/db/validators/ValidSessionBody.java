package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.property.db.entities.sessions.SessionBody;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidSessionBody.Validator.class})
public @interface ValidSessionBody {
	String message() default "{com.property.validation.constraints.invalid-credentials}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidSessionBody, SessionBody> {

		@Override
		public void initialize(ValidSessionBody validSessionBody) {
		}

		@Override
		public boolean isValid(SessionBody sessionBody, ConstraintValidatorContext constraintValidatorContext) 
		{
			return 
				(sessionBody.getPassword() != null) &&
				(sessionBody.getEmail() != null);
		}
    }
}
