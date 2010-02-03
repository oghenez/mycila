package com.mycila.jmx.export.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface JMXBean {

    /**
     * Equivalent to {@link #objectName()}}
     */
    public abstract String value() default "";

    public abstract String objectName() default "";

    public abstract String description() default "";
}