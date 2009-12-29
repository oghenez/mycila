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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Loggers {

    private static enum Usage {
        NONE, LOG4J, CUSTOM, JDK
    }

    private static volatile Usage current;
    private static volatile LoggerProvider loggerProvider; static {
        try {
            useSystemProperty();
        } catch (Exception e) {
            useNone();
        }
    }

    private Loggers() {
    }

    /**
     * Configure Mycila Logger to use JDK logging. This is the default behavior.
     */
    public static synchronized void useJDK() {
        if (current != Usage.JDK) {
            current = Usage.JDK;
            Loggers.loggerProvider = LoggerProviders.cache(LoggerProviders.jdk());
        }
    }

    /**
     * Configure Mycila Logger to use Log4J
     */
    public static synchronized void useLog4j() {
        if (current != Usage.LOG4J) {
            current = Usage.LOG4J;
            Loggers.loggerProvider = LoggerProviders.cache(LoggerProviders.log4j());
        }
    }

    /**
     * Configure Mycila Logger to use no logger at all and thus won't log anything
     */
    public static synchronized void useNone() {
        if (current != Usage.NONE) {
            current = Usage.NONE;
            Loggers.loggerProvider = LoggerProviders.cache(LoggerProviders.nop());
        }
    }

    /**
     * Read the system property 'mycila.log.provider' to get the name of a class to instanciate,
     * which implementing {@link com.mycila.log.LoggerProvider}
     */
    public static synchronized void useSystemProperty() {
        current = Usage.CUSTOM;
        Loggers.loggerProvider = LoggerProviders.cache(LoggerProviders.fromSystemProperty());
    }

    /**
     * Specify a custom {@link com.mycila.log.LoggerProvider} that will returns {@link Logger instances}.
     * {@link com.mycila.log.LoggerProvider} can be composed using {@link com.mycila.log.LoggerProviders}
     *
     * @param loggerProvider The logger provider
     */
    public static synchronized void use(LoggerProvider loggerProvider) {
        current = Usage.CUSTOM;
        Loggers.loggerProvider = loggerProvider;
    }

    /**
     * Specify a custom {@link com.mycila.log.LoggerProvider} that will returns {@link Logger instances}.
     * {@link com.mycila.log.LoggerProvider} can be composed using {@link com.mycila.log.LoggerProviders}
     *
     * @param loggerProvider The logger provider
     */
    public static synchronized void useAndCache(LoggerProvider loggerProvider) {
        use(LoggerProviders.cache(loggerProvider));
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
