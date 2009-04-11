package com.mycila.testing;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKLogging {
    static {
        System.out.println("=== INIT JDK LOGGING FROM logging.properties ===");
        try {
            LogManager.getLogManager().readConfiguration(JDKLogging.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public static void init() {
    }
}
