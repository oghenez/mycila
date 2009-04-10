package com.mycila.plugin.spi;

import org.testng.annotations.BeforeSuite;

import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SetupLoggerTest {
    @BeforeSuite
    public void test() throws Exception {
        LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
    }
}
