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

import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_CONTEXT_PATH;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_DO_DEPLOY_WEBAPP;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_DO_START_SERVER;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_SERVER_PORT;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_SKIP;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_WAR_LOCATION;
import static java.lang.annotation.ElementType.METHOD;
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
 * <p>
 * Here's an example which run webapp accessible by default at URL : {@code http://localhost:9090}
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
 *          String basedUrl = JettyRunWarHelper.getWebappUrl(this.getClass());
 *          Assert.assertEquals(&quot;http://localhost:9090&quot;, basedUrl);
 *      }
 * }
 * //
 * </pre>
 * 
 * @author amertum
 * 
 * @see JettyRunWarHelper
 */
@Target({
        TYPE, METHOD
})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface JettyRunWar {

    /**
     * The path of the WAR file to load. The search is based on the current directory.
     * <p>
     * Default is {@link #DEFAULT_WAR_LOCATION}. If there is more than one WAR file the test will fail, else the WAR
     * file will be loaded and the test run.
     * <p>
     * WAR locator strategy :
     * <ul>
     * <li>default path is either relative or absolute
     * <li>starts with "reg:" to enable the following to be java regular expression for this path
     * <ul>
     * <li>the regular expression should starts with the current directory {@code './'}, which translated in java
     * pattern is {@code '\\.\\/'}
     * </ul>
     * <li>starts with "ant:" to enable the following to be ant path expression for this path, ie:
     * &#42;&#42;/webapp-*.war
     * <ul>
     * <li>? matches one character
     * <li>* matches zero or more characters
     * <li>** matches zero or more 'directories' in a path
     * </ul>
     * <li>starts with "sys:" to enable the following to be system property expression for this path
     * </ul>
     * 
     * @return path of the WAR file to load.
     */
    String value() default DEFAULT_WAR_LOCATION;


    /**
     * The web application server port. Default is {@link AbstractDefaultJettyRunWarConfig#DEFAULT_SERVER_PORT}.
     * 
     * @return the web application server port.
     */
    int serverPort() default DEFAULT_SERVER_PORT;


    /**
     * The web application context path. Must starts with a slash '/' but doesn't end with one
     * except for root context path. Default is {@link AbstractDefaultJettyRunWarConfig#DEFAULT_CONTEXT_PATH}.
     * 
     * @return the web application context path.
     */
    String contextPath() default DEFAULT_CONTEXT_PATH;


    /**
     * True to skip starting server or deploying webapp. Default is {@link AbstractDefaultJettyRunWarConfig#DEFAULT_SKIP}.
     * 
     * @return if skip starting server or deploying webapp.
     */
    boolean skip() default DEFAULT_SKIP;


    /**
     * True to start a new server (and stop the old one), false to start a server only if there is no running
     * one. If {@code true} then {@link #deployWebapp()} is logically force to true.
     * 
     * @return true to start a new server (and stop the old one), false to start a server only if there is no running
     *         one.
     */
    boolean startServer() default DEFAULT_DO_START_SERVER;


    /**
     * True to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     * deployed one.
     * 
     * @return true to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     *         deployed one.
     */
    boolean deployWebapp() default DEFAULT_DO_DEPLOY_WEBAPP;


    /**
     * The server lifecycle listener which allow customization of the server configuration. Default is
     * {@link NopServerLifeCycleListener}.
     * 
     * @return the server lifecycle listener.
     */
    Class<? extends ServerLifeCycleListener> serverLifeCycleListener() default NopServerLifeCycleListener.class;


    /**
     * The {@link JettyRunWarConfig} class which define the WAR to deploy, how and when. This configuration is overriden
     * by
     * specific values : {@link #value()}, {@link #serverPort()}, {@link #contextPath()},
     * {@link #serverLifeCycleListener()}, {@link #skip()}.
     * 
     * @return the {@link JettyRunWarConfig} class.
     */
    Class<? extends JettyRunWarConfig> config() default DefaultJettyRunWarConfig.class;

}
