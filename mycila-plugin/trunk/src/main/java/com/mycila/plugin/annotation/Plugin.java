package com.mycila.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class annotated by @Plugin will be treated as a plugin.
 * A plugin is uniquely identified by its full class name.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Plugin {

    /**
     * Specify the plugin name.
     */
    String name() default "";

    /**
     * Set a description about what this plugin does
     */
    String description() default "";
}
