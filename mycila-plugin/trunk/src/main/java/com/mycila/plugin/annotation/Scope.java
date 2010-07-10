package com.mycila.plugin.annotation;

import com.mycila.plugin.scope.ExportProvider;
import com.mycila.plugin.scope.None;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Place a scope on an exporting method annotated by @export.
 * <br>
 * The scope can be a custom scope implementing {@link com.mycila.plugin.Provider}.
 * <br>
 * Several scopes are provided, like the famous singleton one.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Scope {
    Class<? extends ExportProvider> value() default None.class;

    Param[] params() default {};
}