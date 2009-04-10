package com.mycila.log.jdk;

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import org.testng.annotations.Test;

import java.util.logging.LogManager;


/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKLoggerTest {

    @Test
    public void test_log_only_INFO_and_more_by_default() throws Exception {
        LogManager.getLogManager().reset();
        LogManager.getLogManager().readConfiguration();
        Logger logger = Loggers.get(JDKLoggerTest.class);
        logger.trace("trace {0}", "arg");
        logger.trace(new IllegalStateException("trace"), "trace {0}", "arg");
        logger.debug("debug {0}", "arg");
        logger.debug(new IllegalStateException("debug"), "debug {0}", "arg");
        logger.info("info {0}", "arg");
        logger.info(new IllegalStateException("info"), "info {0}", "arg");
        logger.warn("warn {0}", "arg");
        logger.warn(new IllegalStateException("warn"), "warn {0}", "arg");
        logger.error("error {0}", "arg");
        logger.error(new IllegalStateException("error"), "error {0}", "arg");
    }

    @Test
    public void test_custom_config() throws Exception {
        LogManager.getLogManager().reset();
        LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
        Logger logger = Loggers.get(JDKLoggerTest.class);
        logger.trace("trace {0}", "arg");
        logger.trace(new IllegalStateException("trace"), "trace {0}", "arg");
        logger.debug("debug {0}", "arg");
        logger.debug(new IllegalStateException("debug"), "debug {0}", "arg");
        logger.info("info {0}", "arg");
        logger.info(new IllegalStateException("info"), "info {0}", "arg");
        logger.warn("warn {0}", "arg");
        logger.warn(new IllegalStateException("warn"), "warn {0}", "arg");
        logger.error("error {0}", "arg");
        logger.error(new IllegalStateException("error"), "error {0}", "arg");
    }

}
