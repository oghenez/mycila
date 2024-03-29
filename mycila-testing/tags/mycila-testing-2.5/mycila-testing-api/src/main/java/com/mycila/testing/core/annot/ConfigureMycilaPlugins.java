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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be place on any method in your test class to be able to configure the
 * {@link com.mycila.plugin.spi.PluginManager} at runtime.
 * <p/>
 * The method must have one argument, which is the plugin manager. In example:
 * <p/>
 * {@code @ConfigureMycilaPlugins
 * void configure(PluginManager<TestPlugin> pluginManager) {}}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
public @interface ConfigureMycilaPlugins {

}