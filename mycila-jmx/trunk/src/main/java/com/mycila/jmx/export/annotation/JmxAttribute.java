package com.mycila.jmx.export.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface JmxAttribute {
    /**
     * Equivalent to {@link #name()}}
     */
    String value() default "";

    String name() default "";

    String description() default "";

    Access access() default Access.RO;
}