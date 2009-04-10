package com.mycila.log;

import com.mycila.log.jdk.JDKLogger;
import com.mycila.log.log4j.Log4jLogger;

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

    public static void useJDK() {
        use(new LoggerProvider() {
            public Logger get(String name) {
                return new JDKLogger(name);
            }
        });
    }

    public static void useLog4j() {
        use(new LoggerProvider() {
            public Logger get(String name) {
                return new Log4jLogger(name);
            }
        });
    }

    public static void useSystemProperty() {
        try {
            use((LoggerProvider) Thread.currentThread().getContextClassLoader().loadClass(System.getProperty("mycila.log.provider")).newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void use(LoggerProvider loggerProvider) {
        Loggers.loggerProvider = loggerProvider;
    }

    public static Logger get(Class<?> c) {
        return get(c.getName());
    }

    public static Logger get(String name) {
        return loggerProvider.get(name);
    }
}
