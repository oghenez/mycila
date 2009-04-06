package com.mycila.testing.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface MycilaPlugins {

    /**
     * Specify if this test instance should use the plugins shared statically amongst all test instance
     * or if the plugins should be reloaded for this test.
     *
     * @return The cache strategy to use for plugins
     */
    Cache cache() default Cache.SHARED;

    /**
     * Overrides default plugin descriptor file to use. If null or empty, the plugin cache will not load anything: it will be up to the
     * programmer to register plugins at runtime.
     *
     * @return The plugin descriptor file location in the classpath
     */
    String descriptor() default MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
}
