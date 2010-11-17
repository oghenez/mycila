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

package com.mycila.testing.plugins.jetty;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.mycila.testing.junit.MycilaJunitRunner;

/**
 * This annotation enables the loading of a packaged web application (ie: a WAR file) by a Servlet Container when used
 * with junit runner {@link org.junit.runner.RunWith} and {@link MycilaJunitRunner}. This is useful to run functional
 * tests.
 * 
 * Here an example :
 * 
 * <pre>
 * &#064;RunWith(MycilaJunitRunner.class)
 * &#064;RunWar
 * public class ExampleTest {
 *     &#064;Test
 *      public void testSomething() {
 *          ...
 *      }
 * 
 * 
 *     // required to enable the JettyMycilaPlugin using RunWar annotation 
 *     &#064;ConfigureMycilaPlugins
 *     protected void configure(
 *             final PluginManager&lt;JettyMycilaPlugin&gt; pluginManager)
 *     {
 *         pluginManager.getCache().registerPlugin(&quot;jettyPlugin&quot;, new JettyMycilaPlugin());
 *     }
 * 
 * }
 * //
 * </pre>
 */
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@Documented
public @interface JettyRunWar {

    /**
     * alias for {@link #war()}.
     * 
     * @return alias for {@link #war()}.
     * 
     * @see #war()
     */
    String value() default "";


    /**
     * The path of the WAR file to load. The search is based on the current directory.
     * <p>
     * If the default value "" is used, a search of all WAR file based on the current directory will be done. If there
     * is more than one WAR file the test will fail, else the WAR file will be loaded and the test run.
     * <p>
     * WAR locator strategy :
     * <ul>
     * <li>default path is either relative or absolute;
     * <li>starts with "reg:" to enable the following to be java regular expression for this path;
     * <ul>
     * <li>the regular expression should starts with the current directory {@code './'}, which translated in java
     * pattern is {@code '\\.\\/'}
     * </ul>
     * <li>starts with "ant:" to enable the following to be ant path expression for this path;
     * <li>starts with "sys:" to enable the following to be system property expression for this path.
     * </ul>
     * 
     * @return path of the WAR file to load.
     */
    String war() default "";


    /**
     * The web application context path.
     * 
     * @return the web application context path.
     */
    String contextPath() default "/";


    /**
     * The web application server port.
     * 
     * @return the web application server port.
     */
    int serverPort() default 9090;

    // TODO Class<Config> config(); LifeCycleListener before, after

}
