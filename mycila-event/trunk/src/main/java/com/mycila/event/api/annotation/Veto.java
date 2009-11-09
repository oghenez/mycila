package com.mycila.event.api.annotation;

import com.mycila.event.api.util.ref.Reachability;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Veto {
    public abstract String topic() default "";

    Reachability reach() default Reachability.WEAK;
}