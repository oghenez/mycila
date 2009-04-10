package com.mycila.log.log4j;

import com.mycila.log.AbstractLogger;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.text.MessageFormat;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Log4jLogger extends AbstractLogger {

    private final Logger logger;

    public Log4jLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    public boolean canLog(com.mycila.log.Level level) {
        switch (level) {
            case TRACE:
                return logger.isEnabledFor(Level.TRACE);
            case DEBUG:
                return logger.isEnabledFor(Level.DEBUG);
            case INFO:
                return logger.isEnabledFor(Level.INFO);
            case WARN:
                return logger.isEnabledFor(Level.WARN);
            case ERROR:
                return logger.isEnabledFor(Level.ERROR);
            default:
                return false;
        }
    }

    protected void doLog(com.mycila.log.Level level, Throwable throwable, String message, Object... args) {
        switch (level) {
            case TRACE:
                logger.log(Level.TRACE, MessageFormat.format(message, args), throwable);
                break;
            case DEBUG:
                logger.log(Level.DEBUG, MessageFormat.format(message, args), throwable);
                break;
            case INFO:
                logger.log(Level.INFO, MessageFormat.format(message, args), throwable);
                break;
            case WARN:
                logger.log(Level.WARN, MessageFormat.format(message, args), throwable);
                break;
            case ERROR:
                logger.log(Level.ERROR, MessageFormat.format(message, args), throwable);
                break;
            default:

        }
    }

}