package com.mycila.log;

import com.mycila.log.jdk.JDKLogger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Loggers {

    private static LoggerProvider loggerProvider;

    static {
        try {
            loggerProvider = (LoggerProvider) Thread.currentThread().getContextClassLoader().loadClass(System.getProperty("mycila.log.provider")).newInstance();
        } catch (Exception ignored) {
            loggerProvider = new LoggerProvider() {
                public Logger get(String name) {
                    return new JDKLogger(name);
                }
            };
        }
    }

    private Loggers() {
    }

    public static void providedBy(LoggerProvider loggerProvider) {
        Loggers.loggerProvider = loggerProvider;
    }

    public static Logger get(Class<?> c) {
        return get(c.getName());
    }

    public static Logger get(String name) {
        return loggerProvider.get(name);
    }
}
