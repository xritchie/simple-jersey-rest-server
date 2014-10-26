package com.property.db.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.property.db.entities.account.Account;


@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidEmail.Validator.class})
public @interface ValidEmail {
String message() default "{com.property.validation.constraints.email}";
	
	Class<?>[] groups() default {};
	 
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidEmail, String> {
		    	
    	@SuppressWarnings("unused")
		private UriInfo uriInfo;

        public Validator(@Context final UriInfo uriInfo) {
            this.uriInfo = uriInfo;
        }
    	
		@Override
		public void initialize(ValidEmail validEmail) {
		}

		@Override
		public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
			 return isValid(email);
		}
		
		public static boolean isValid(String email) {
			Pattern pattern = 
				java.util.regex.Pattern.compile(
					"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$",
					java.util.regex.Pattern.CASE_INSENSITIVE
				);
					
			 Matcher m = pattern.matcher( email );
			 return m.matches();
		}
		
	
    }
}
