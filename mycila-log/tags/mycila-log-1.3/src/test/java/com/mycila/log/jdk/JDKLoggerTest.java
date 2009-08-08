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
        Loggers.useJDK();
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
