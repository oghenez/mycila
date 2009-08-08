package com.mycila.log.jdk;

import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;

import java.io.IOException;
import java.net.URL;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKLoggerProvider implements LoggerProvider {

    public JDKLoggerProvider() {
        URL config = Thread.currentThread().getContextClassLoader().getResource("logging.properties");
        if (config != null) {
            try {
                LogManager.getLogManager().readConfiguration(config.openStream());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    public Logger get(String name) {
        return new JDKLogger(name);
    }
}
