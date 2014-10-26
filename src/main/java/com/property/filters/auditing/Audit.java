package com.property.filters.auditing;

import java.lang.annotation.*;

//@Audit
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Audit {
}
