package com.mycila.log.nop;

import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NopLoggerProvider implements LoggerProvider {
    public Logger get(String name) {
        return NopLogger.INSTANCE;
    }
}
