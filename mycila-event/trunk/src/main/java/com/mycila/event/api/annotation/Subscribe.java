package com.mycila.event.api.annotation;

import com.mycila.event.api.util.ref.Reachability;

import java.lang.annotation.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Subscribe {
    String topic() default "";

    Reachability reach() default Reachability.WEAK;
}
