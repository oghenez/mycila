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

    static {Loggers.useJDK();}
    
    @Test
    public void test_log_only_INFO_and_more_by_default() throws Exception {
        LogManager.getLogManager().reset();
        LogManager.getLogManager().readConfiguration();
        Logger logger = Loggers.get(JDKLoggerTest.class);
        logger.trace("trace %s", "arg");
        logger.trace(new IllegalStateException("trace"), "trace %s", "arg");
        logger.debug("debug %s", "arg");
        logger.debug(new IllegalStateException("debug"), "debug %s", "arg");
        logger.info("info %s", "arg");
        logger.info(new IllegalStateException("info"), "info %s", "arg");
        logger.warn("warn %s", "arg");
        logger.warn(new IllegalStateException("warn"), "warn %s", "arg");
        logger.error("error %s", "arg");
        logger.error(new IllegalStateException("error"), "error %s", "arg");
    }

    @Test
    public void test_custom_config() throws Exception {
        LogManager.getLogManager().reset();
        Logger logger = Loggers.get(JDKLoggerTest.class);
        logger.trace("trace %s", "arg");
        logger.trace(new IllegalStateException("trace"), "trace %s", "arg");
        logger.debug("debug %s", "arg");
        logger.debug(new IllegalStateException("debug"), "debug %s", "arg");
        logger.info("info %s", "arg");
        logger.info(new IllegalStateException("info"), "info %s", "arg");
        logger.warn("warn %s", "arg");
        logger.warn(new IllegalStateException("warn"), "warn %s", "arg");
        logger.error("error %s", "arg");
        logger.error(new IllegalStateException("error"), "error %s", "arg");
    }

}
