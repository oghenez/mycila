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
package com.mycila.log;

import static com.mycila.log.LoggerProviders.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Loggers {

    private static LoggerProvider loggerProvider; static {
        try {
            useSystemProperty();
        } catch (Exception e) {
            useJDK();
        }
    }

    private Loggers() {
    }

    /**
     * Configure Mycila Logger to use JDK logging. This is the default behavior.
     */
    public static void useJDK() {
        use(cache(JDK));
    }

    /**
     * Configure Mycila Logger to use Log4J
     */
    public static void useLog4j() {
        use(cache(LOG4J));
    }

    /**
     * Configure Mycila Logger to use no logger at all and thus won't log anything
     */
    public static void useNone() {
        use(cache(NOP));
    }

    /**
     * Read the system property 'mycila.log.provider' to get the name of a class to instanciate,
     * which implementing {@link com.mycila.log.LoggerProvider}
     */
    public static void useSystemProperty() {
        use(cache(fromSystemProperty()));
    }

    /**
     * Specify a custom {@link com.mycila.log.LoggerProvider} that will returns {@link Logger instances}.
     * {@link com.mycila.log.LoggerProvider} can be composed using {@link com.mycila.log.LoggerProviders}
     *
     * @param loggerProvider The logger provider
     */
    public static synchronized void use(LoggerProvider loggerProvider) {
        Loggers.loggerProvider = loggerProvider;
    }

    /**
     * Get the logger for specified class
     *
     * @param c The class
     * @return A logger for this class
     */
    public static Logger get(Class<?> c) {
        return get(c.getName());
    }

    /**
     * Get a named logger
     *
     * @param name The logger name
     * @return A logger for this name
     */
    public static Logger get(String name) {
        return loggerProvider.get(name);
    }
}
