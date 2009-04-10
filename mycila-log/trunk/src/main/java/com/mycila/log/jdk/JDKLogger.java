package com.mycila.log.jdk;

import com.mycila.log.AbstractLogger;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKLogger extends AbstractLogger {

    private final java.util.logging.Logger logger;

    public JDKLogger(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    public boolean canLog(com.mycila.log.Level level) {
        switch (level) {
            case TRACE:
                return logger.isLoggable(Level.FINEST);
            case DEBUG:
                return logger.isLoggable(Level.FINE);
            case INFO:
                return logger.isLoggable(Level.INFO);
            case WARN:
                return logger.isLoggable(Level.WARNING);
            case ERROR:
                return logger.isLoggable(Level.SEVERE);
            default:
                return false;
        }
    }

    protected void doLog(com.mycila.log.Level level, Throwable throwable, String message, Object... args) {
        switch (level) {
            case TRACE:
                logger.log(buildLogRecord(Level.FINEST, throwable, message, args));
                break;
            case DEBUG:
                logger.log(buildLogRecord(Level.FINE, throwable, message, args));
                break;
            case INFO:
                logger.log(buildLogRecord(Level.INFO, throwable, message, args));
                break;
            case WARN:
                logger.log(buildLogRecord(Level.WARNING, throwable, message, args));
                break;
            case ERROR:
                logger.log(buildLogRecord(Level.SEVERE, throwable, message, args));
                break;
            default:

        }
    }

    private LogRecord buildLogRecord(Level level, Throwable throwable, String message, Object... args) {
        LogRecord logRecord = new LogRecord(level, MessageFormat.format(message, args));
        logRecord.setLoggerName(logger.getName());
        logRecord.setSourceClassName(logger.getName());
        logRecord.setSourceMethodName(Thread.currentThread().getName());
        logRecord.setThrown(throwable);
        return logRecord;
    }
}
