package com.mycila.event.api.annotation;

import com.mycila.event.api.Reachability;

import java.lang.annotation.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Inherited
public @interface Reference {
    Reachability value();
}