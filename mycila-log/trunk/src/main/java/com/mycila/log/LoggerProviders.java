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

import com.mycila.log.jdk.JDKLoggerProvider;
import com.mycila.log.log4j.Log4jLoggerProvider;
import com.mycila.log.nop.NopLoggerProvider;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LoggerProviders {

    private LoggerProviders() {
    }

    /**
     * Caches Logger instances in a SoftHashMap so that garbadge collector can remove entries on memory demand.
     *
     * @param loggerProvider The LoggerProvider to cache
     * @return A caching wrapper for the given LoggerProvider
     */
    public static LoggerProvider cache(final LoggerProvider loggerProvider) {
        return new LoggerProvider() {
            final Map<String, Logger> cache = new SoftHashMap<String, Logger>();

            public Logger get(String name) {
                Logger logger;
                if ((logger = cache.get(name)) == null) {
                    synchronized (cache) {
                        if ((logger = cache.get(name)) == null) {
                            logger = loggerProvider.get(name);
                            cache.put(name, logger);
                        }
                    }
                }
                return logger;
            }
        };
    }

    /**
     * Read the system property 'mycila.log.provider' to get the name of a class to instanciate,
     * which implementing {@link com.mycila.log.LoggerProvider}
     *
     * @return The logger specified in the system property
     */
    public static LoggerProvider fromSystemProperty() {
        String className = System.getProperty("mycila.log.provider");
        try {
            return (LoggerProvider) Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error loading class '" + className + "' defined for system property 'mycila.log.provider': " + e.getMessage(), e);
        }
    }

    /**
     * @return Log4J wrapper
     */
    public static LoggerProvider log4j() {
        return Log4jLoggerProvider.get();
    }

    /**
     * @return JDK Logging wrapper
     */
    public static LoggerProvider jdk() {
        return JDKLoggerProvider.get();
    }

    /**
     * @return NO OP wrapper, to disable logging
     */
    public static LoggerProvider nop() {
        return NopLoggerProvider.get();
    }

}
