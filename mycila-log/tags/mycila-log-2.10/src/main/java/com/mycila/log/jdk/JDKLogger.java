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
package com.mycila.log.jdk;

import com.mycila.log.AbstractLogger;

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

    @Override
    protected void doLog(com.mycila.log.Level level, Throwable throwable, Object message, Object... args) {
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

    private LogRecord buildLogRecord(Level level, Throwable throwable, Object message, Object... args) {
        LogRecord logRecord = new LogRecord(level, String.format(String.valueOf(message), args));
        logRecord.setLoggerName(logger.getName());
        logRecord.setSourceClassName(logger.getName());
        logRecord.setSourceMethodName(Thread.currentThread().getName());
        logRecord.setThrown(throwable);
        return logRecord;
    }
}
