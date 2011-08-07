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
import static com.mycila.testing.plugins.jetty.config.DefaultConfig.configFrom;

import java.lang.reflect.Method;

import com.mycila.testing.plugins.jetty.config.Config;

/**
 * Some helper methods to deal with webapp context.
 */
public class WebappHelper {

    /**
     * Returns the web application URL from a the given test class instance which must be annotated with
     * {@link JettyRunWar} and doesn't end with slash '/'.
     * 
     * @param instance
     *        the test object.
     * 
     * @return the web application URL.
     */
    public static String getWebappUrl(
            final Object instance)
    {
        return getWebappUrl(configFrom(instance.getClass()));
    }


    /**
     * Returns the web application URL from a the given test method which should be annotated with {@link JettyRunWar}
     * (or its {@link Method#getDeclaringClass()} at least) and doesn't end with slash '/'.
     * 
     * @param instance
     *        the test object.
     * @param methodName
     *        the test method name.
     * 
     * @return the web application URL.
     */
    public static String getWebappUrl(
            final Object instance,
            final String methodName)
    {
        try {
            return getWebappUrl(instance.getClass().getMethod(methodName));
        }
        catch (final SecurityException e) {
            throw propagate(e);
        }
        catch (final NoSuchMethodException e) {
            throw propagate(e);
        }
    }


    /**
     * Returns the web application URL from a the given test method which should be annotated with {@link JettyRunWar}
     * (or
     * its {@link Method#getDeclaringClass()} at least) and doesn't end with slash '/'.
     * 
     * @param method
     *        the test method.
     * 
     * @return the web application URL.
     */
    public static String getWebappUrl(
            final Method method)
    {
        return getWebappUrl(configFrom(method));
    }


    private static String getWebappUrl(
            final Config config)
    {
        final String path = config.getContextPath();

        final String contextPath = "/".equals(path)
                ? ""
                : path;
        final String url = "http://localhost:" + config.getServerPort() + contextPath;

        return url;
    }

}
