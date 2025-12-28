package org.example.springmanager2.Entity.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComplexIntId {
    // Optional: prefix or pattern
    int start() default 1000;
}

