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

import com.mycila.log.AbstractLogger;
import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;
import com.mycila.log.Loggers;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Sample {
    public static void main(String[] args) {
        Loggers.use(new LoggerProvider() {
            public Logger get(String name) {
                final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(name);
                return new AbstractLogger() {
                    public boolean canLog(com.mycila.log.Level level) {
                        switch (level) {
                            case TRACE:
                                return logger.isEnabledFor(org.apache.log4j.Level.TRACE);
                            case DEBUG:
                                return logger.isEnabledFor(org.apache.log4j.Level.DEBUG);
                            case INFO:
                                return logger.isEnabledFor(org.apache.log4j.Level.INFO);
                            case WARN:
                                return logger.isEnabledFor(org.apache.log4j.Level.WARN);
                            case ERROR:
                                return logger.isEnabledFor(org.apache.log4j.Level.ERROR);
                            default:
                                return false;
                        }
                    }
                    @Override
                    protected void doLog(com.mycila.log.Level level, Throwable throwable, Object message, Object... args) {
                        switch (level) {
                            case TRACE:
                                logger.log(org.apache.log4j.Level.TRACE, String.format(String.valueOf(message), args), throwable);
                                break;
                            case DEBUG:
                                logger.log(org.apache.log4j.Level.DEBUG, String.format(String.valueOf(message), args), throwable);
                                break;
                            case INFO:
                                logger.log(org.apache.log4j.Level.INFO, String.format(String.valueOf(message), args), throwable);
                                break;
                            case WARN:
                                logger.log(org.apache.log4j.Level.WARN, String.format(String.valueOf(message), args), throwable);
                                break;
                            case ERROR:
                                logger.log(org.apache.log4j.Level.ERROR, String.format(String.valueOf(message), args), throwable);
                                break;
                            default:

                        }
                    }
                };
            }
        });
    }
}
