package com.mycila.log;

import com.mycila.log.jdk.JDKLogger;
import com.mycila.log.log4j.Log4jLogger;
import com.mycila.log.nop.NopLogger;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LoggerProviders {

    private LoggerProviders() {
    }

    /**
     * Provides Log4J Loggers
     */
    public static final LoggerProvider LOG4J = new LoggerProvider() {
        public Logger get(String name) {
            return new Log4jLogger(name);
        }
    };

    /**
     * Provides JDK Loggers
     */
    public static final LoggerProvider JDK = new LoggerProvider() {
        public Logger get(String name) {
            return new JDKLogger(name);
        }
    };

    /**
     * Provides an empty logger
     */
    public static final LoggerProvider NOP = new LoggerProvider() {
        public Logger get(String name) {
            return NopLogger.INSTANCE;
        }
    };

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
        try {
            return (LoggerProvider) Thread.currentThread().getContextClassLoader().loadClass(System.getProperty("mycila.log.provider")).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
