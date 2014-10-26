package com.property.filters.versioning;

import java.lang.annotation.*;

//@Version
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Version {
	String value();
}
