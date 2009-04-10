package com.mycila.log;

import static com.mycila.log.Level.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractLogger implements Logger {
    public boolean canTrace() {
        return canLog(TRACE);
    }

    public final void trace(String message, Object... args) {
        if(canLog(TRACE)) {
            doLog(TRACE, null, message, args);
        }
    }

    public final void trace(Throwable throwable, String message, Object... args) {
        if(canLog(TRACE)) {
            doLog(TRACE, throwable, message, args);
        }
    }

    public final boolean canDebug() {
        return canLog(DEBUG);
    }

    public final void debug(String message, Object... args) {
        if(canLog(DEBUG)) {
            doLog(DEBUG, null, message, args);
        }
    }

    public final void debug(Throwable throwable, String message, Object... args) {
        if(canLog(DEBUG)) {
            doLog(DEBUG, throwable, message, args);
        }
    }

    public final boolean canInfo() {
        return canLog(INFO);
    }

    public final void info(String message, Object... args) {
        if(canLog(INFO)) {
            doLog(INFO, null, message, args);
        }
    }

    public final void info(Throwable throwable, String message, Object... args) {
        if(canLog(INFO)) {
            doLog(INFO, throwable, message, args);
        }
    }

    public final boolean canWarn() {
        return canLog(WARN);
    }

    public final void warn(String message, Object... args) {
        if(canLog(WARN)) {
            doLog(WARN, null, message, args);
        }
    }

    public final void warn(Throwable throwable, String message, Object... args) {
        if(canLog(WARN)) {
            doLog(WARN, throwable, message, args);
        }
    }

    public final boolean canError() {
        return canLog(ERROR);
    }

    public final void error(String message, Object... args) {
        if(canLog(ERROR)) {
            doLog(ERROR, null, message, args);
        }
    }

    public final void error(Throwable throwable, String message, Object... args) {
        if(canLog(ERROR)) {
            doLog(ERROR, throwable, message, args);
        }
    }

    public final void log(Level level, String message, Object... args) {
        if(canLog(level)) {
            doLog(level, null, message, args);
        }
    }

    public final void log(Level level, Throwable throwable, String message, Object... args) {
        if (canLog(level)) {
            doLog(level, throwable, message, args);
        }
    }

    protected abstract void doLog(Level level, Throwable throwable, String message, Object... args);

}
