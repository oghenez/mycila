package com.mycila.testing.plugin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Verify that a test has thrown an exception
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface ExpectException {

    static final String NO_MESSAGE = "com.mycila.testing.plugin.annotation.ExpectException.NO_MESSAGE";

    Class<? extends Throwable> type() default Throwable.class;

    String message() default NO_MESSAGE;

    String containing() default NO_MESSAGE;
}
