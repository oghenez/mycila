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

public class JettyRunWarHelper {

    /**
     * Returns the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     */
    public static String getDefaultValue()
    {
        return getDefaultValue("value", "");
    }


    /**
     * Returns the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application WAR file to run for class annotated with {@link JettyRunWar}.
     */
    public static String getDefaultWar()
    {
        return getDefaultValue("war", "");
    }


    /**
     * Returns the default web application context path value for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application context path value for class annotated with {@link JettyRunWar}.
     */
    public static String getDefaultContextPath()
    {
        return getDefaultValue("contextPath", "");
    }


    /**
     * Returns the default web application server port value for class annotated with {@link JettyRunWar}.
     * 
     * @return the default web application server port value for class annotated with {@link JettyRunWar}.
     */
    public static int getDefaultServerPort()
    {
        return getDefaultValue("serverPort", Integer.valueOf(0)).intValue();
    }


    /**
     * Returns the default web application URL for class annotated with {@link JettyRunWar}, doesn't end with slash '/'.
     * 
     * @return the default web application URL for class annotated with {@link JettyRunWar}, doesn't end with slash '/'.
     */
    public static String getDefaultWebappUrl()
    {
        return "http://localhost:" + getDefaultServerPort();
    }


    /**
     * Returns the web application URL from a the given test class which must be annotated with {@link JettyRunWar},
     * doesn't end with slash '/'.
     * 
     * @param testClass
     *        the annotated with {@link JettyRunWar} test class.
     * 
     * @return the web application URL from a test class which must be annotated with {@link JettyRunWar}, doesn't end
     *         with slash '/'.
     */
    public static String getWebappUrl(
            final Class<?> testClass)
    {
        final JettyRunWar jettyRunWar = testClass.getAnnotation(JettyRunWar.class);
        final String contextPath = "/".equals(jettyRunWar.contextPath())
                ? ""
                : jettyRunWar.contextPath();
        final String url = "http://localhost:" + jettyRunWar.serverPort() + contextPath;
        return url;
    }


    private static <T> T getDefaultValue(
            final String methodName,
            final T defaultValue)
    {
        T value;

        try {
            value = (T) JettyRunWar.class.getMethod(methodName).getDefaultValue();
        }
        catch (final NoSuchMethodException e) {
            value = defaultValue;
        }

        return value;

    }

}
