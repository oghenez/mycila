/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.testing.plugins.jetty;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugins.jetty.config.DefaultConfig;
import com.mycila.testing.plugins.jetty.config.RawConfig;

/**
 * This annotation enables the loading of a packaged web application (ie: a WAR file) by a Servlet Container when used
 * with junit runner {@link org.junit.runner.RunWith} and {@link MycilaJunitRunner}. This is useful to run functional
 * and integration tests (its).
 * <p>
 * Here's an example which run webapp accessible by default at URL : {@code http://localhost:9090/its}
 * 
 * <pre>
 * &#064;RunWith(MycilaJunitRunner.class)
 * &#064;JettyRunWar
 * public class ExampleTest {
 * 
 *     &#064;Test
 *      public void testSomething() {
 *          ...
 *          // you can use JettyRunWarHelper to automatically retrieve the webapp based URL
 *          String basedUrl = WebappHelper.getWebappUrl(this);
 *          Assert.assertEquals(&quot;http://localhost:9090/its&quot;, basedUrl);
 *      }
 * }
 * //
 * </pre>
 * 
 * @author amertum
 * 
 * @see WebappHelper
 */
@Target({
        TYPE, METHOD
})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface JettyRunWar {

    /**
     * Returns the {@link RawConfig} {@code class} which defines how to deploy the webapp
     * 
     * @return the {@link RawConfig} {@code class} which defines how to deploy the webapp, default {@link DefaultConfig}
     *         .
     * 
     * @see DefaultConfig
     * @see RawConfig
     */
    Class<? extends RawConfig> value() default DefaultConfig.class;

}
