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

import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;
import com.mycila.log.LoggerProviders;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Log4jLoggerProvider implements LoggerProvider {

    private static final LoggerProvider INSTANCE = new Log4jLoggerProvider();

    public Log4jLoggerProvider() {
        String file = LoggerProviders.getConfigFile();
        if (file == null) file = "log4j.xml";
        URL config = Thread.currentThread().getContextClassLoader().getResource(file);
        if (config != null) {
            DOMConfigurator.configure(config);
        }
    }

    public Logger get(String name) {
        return new Log4jLogger(name);
    }

    public static LoggerProvider get() {
        return INSTANCE;
    }

}
