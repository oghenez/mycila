package com.mycila.log.log4j;

import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Log4jLoggerProvider implements LoggerProvider {
    public Logger get(String name) {
        return new Log4jLogger(name);
    }
}
