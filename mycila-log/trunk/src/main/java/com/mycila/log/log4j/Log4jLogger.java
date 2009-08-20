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
package com.mycila.log.log4j;

import com.mycila.log.AbstractLogger;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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

    @Override
    protected void doLog(com.mycila.log.Level level, Throwable throwable, String message, Object... args) {
        switch (level) {
            case TRACE:
                logger.log(Level.TRACE, String.format(message, args), throwable);
                break;
            case DEBUG:
                logger.log(Level.DEBUG, String.format(message, args), throwable);
                break;
            case INFO:
                logger.log(Level.INFO, String.format(message, args), throwable);
                break;
            case WARN:
                logger.log(Level.WARN, String.format(message, args), throwable);
                break;
            case ERROR:
                logger.log(Level.ERROR, String.format(message, args), throwable);
                break;
            default:

        }
    }

}