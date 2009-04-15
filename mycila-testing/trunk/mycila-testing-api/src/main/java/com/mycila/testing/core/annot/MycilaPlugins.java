/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.testing.core.annot;

import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.api.Cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be place in your test class to control the
 * {@link com.mycila.plugin.spi.PluginManager}. You can choose if you want to
 * load Mycila Plugins statically shared for all tests or if the plugins should
 * be loaded each time a test instance is created. Also, you have the ability
 * to control which descriptor file is used to load the plugins.
 *
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
    Cache value() default Cache.UNSHARED;

    /**
     * Overrides default plugin descriptor file to use. If null or empty, the plugin cache will not load anything: it will be up to the
     * programmer to register plugins at runtime.
     *
     * @return The plugin descriptor file location in the classpath
     */
    String descriptor() default MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
}
