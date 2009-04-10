package com.mycila.log;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Logger {

    boolean canTrace();

    void trace(String message, Object... args);

    void trace(Throwable throwable, String message, Object... args);

    boolean canDebug();

    void debug(String message, Object... args);

    void debug(Throwable throwable, String message, Object... args);

    boolean canInfo();

    void info(String message, Object... args);

    void info(Throwable throwable, String message, Object... args);

    boolean canWarn();

    void warn(String message, Object... args);

    void warn(Throwable throwable, String message, Object... args);

    boolean canError();

    void error(String message, Object... args);

    void error(Throwable throwable, String message, Object... args);

    boolean canLog(Level level);

    void log(Level level, String message, Object... args);

    void log(Level level, Throwable throwable, String message, Object... args);
}
