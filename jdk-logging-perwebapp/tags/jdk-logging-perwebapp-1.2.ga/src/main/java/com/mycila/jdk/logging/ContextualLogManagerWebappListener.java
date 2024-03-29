/**
 * Copyright (C) 2011 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdk.logging;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou
 */
public final class ContextualLogManagerWebappListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (ContextualLogManager.isAvailable()) {
            ContextualLogManager.get().registerClassLoader(Thread.currentThread().getContextClassLoader());
        } else {
            LogManager.getLogManager().getLogger("").info("=== No ContextualLogManager available ===");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (ContextualLogManager.isAvailable()) {
            ContextualLogManager.get().unregisterClassLoader(Thread.currentThread().getContextClassLoader());
        }
    }
}
