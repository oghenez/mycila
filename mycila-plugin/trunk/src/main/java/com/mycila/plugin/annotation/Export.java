package com.mycila.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a public method of a plugin to export a service.
 * The method must have no parameter and return the type of service to export,
 * which can be overriden by the type parameter.
 * <p/>
 * You can also add to the method a scope annotation and provides any scope you want.
 * By default, no scope is assumed and the method will be called each time an export is used.
 * <br>
 * In example, if you want your export to be singleton, you can use:
 * <code>@Scope(Singleton.class)</code>
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Export {
    Class<?> type() default METHOD_RETURN_TYPE.class;

    static final class METHOD_RETURN_TYPE {
    }
}
