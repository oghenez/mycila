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

import static com.google.common.base.Throwables.propagate;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_CONTEXT_PATH;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_SERVER_PORT;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_WAR_LOCATION;

public class JettyRunWarHelper {
    
    /**
     * Returns the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     */
    public static String getDefaultValue()
    {
        return DEFAULT_WAR_LOCATION;
    }
    

    /**
     * Returns the default web application context path value for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application context path value for class annotated with {@link JettyRunWar}.
     */
    public static String getDefaultContextPath()
    {
        return DEFAULT_CONTEXT_PATH;
    }
    

    /**
     * Returns the default web application server port valuegetWebappRoot for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application server port value for class annotated with {@link JettyRunWar}.
     */
    public static int getDefaultServerPort()
    {
        return DEFAULT_SERVER_PORT;
    }
    

    /**
     * Returns the default web application URL for class annotated with {@link JettyRunWar}, doesn't end with slash '/'.
     * 
     * @return the default web application URL for class annotated with {@link JettyRunWar}, doesn't end with slash '/'.
     */
    public static String getDefaultWebappUrl()
    {
        return "http://localhost:" + DEFAULT_SERVER_PORT;
    }
    

    /**
     * Returns the web application URL from a the given test class which must be annotated with {@link JettyRunWar},
     * doesn't end with slash '/'.
     * 
     * @param testClass
     *            the annotated with {@link JettyRunWar} test class.
     * 
     * @return the web application URL from a test class which must be annotated with {@link JettyRunWar}, doesn't end
     *         with slash '/'.
     */
    public static String getWebappUrl(
            final Class<?> testClass)
    {
        final JettyRunWarConfig config = getConfig(testClass);
        
        final String contextPath = "/".equals(config.getContextPath())
                ? ""
                : config.getContextPath();
        final String url = "http://localhost:" + config.getServerPort() + contextPath;
        return url;
    }
    

    /**
     * Returns a {@link JettyRunWarConfig} for the annotated {@code class}.
     * 
     * @param c
     *            a {@code class} annotated with {@link JettyRunWar}.
     * 
     * @return a {@link JettyRunWarConfig} for the annotated {@code class}.
     * 
     * @throws IllegalArgumentException
     *             if {@code class} is not annotated with {@link JettyRunWar}.
     */
    public static JettyRunWarConfig<JettyRunWar> getConfig(
            final Class<?> c)
    {
        if (!c.isAnnotationPresent(JettyRunWar.class)) {
            throw new IllegalArgumentException(c + " must be annotated with " + JettyRunWar.class);
        }
        
        return getConfig(c.getAnnotation(JettyRunWar.class));
    }
    

    /**
     * Returns a {@link JettyRunWarConfig} for the {@link JettyRunWar} annotation.
     * 
     * @param jettyRunWar
     *            a {@link JettyRunWar} annotation.
     * 
     * @return a {@link JettyRunWarConfig} for the {@link JettyRunWar} annotation.
     */
    public static JettyRunWarConfig<JettyRunWar> getConfig(
            final JettyRunWar jettyRunWar)
    {
        try {
            final JettyRunWarConfig<JettyRunWar> config = new OverrideJettyRunWarConfig(
                    jettyRunWar.config().newInstance());
            config.init(jettyRunWar);
            
            return config;
        }
        catch (final InstantiationException e) {
            throw propagate(e);
        }
        catch (final IllegalAccessException e) {
            throw propagate(e);
        }
    }
    
}
