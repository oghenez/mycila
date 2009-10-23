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
import com.mycila.log.LoggerProvider;

import java.io.IOException;
import java.net.URL;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKLoggerProvider implements LoggerProvider {

    private static final LoggerProvider INSTANCE = new JDKLoggerProvider();

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

    public static LoggerProvider get() {
        return INSTANCE;
    }
        
}
