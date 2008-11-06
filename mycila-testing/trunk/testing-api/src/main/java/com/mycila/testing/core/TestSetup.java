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

package com.mycila.testing.core;

import com.mycila.plugin.spi.PluginManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestSetup {

    private static TestSetup defaultTestSetup;

    private final PluginManager<TestPlugin> pluginManager = new PluginManager<TestPlugin>(TestPlugin.class, "META-INF/mycila/testing/plugins.properties");

    public PluginManager<TestPlugin> getPluginManager() {
        return pluginManager;
    }

    public void setup(Object testInstance) {
        TestContext context = new TestContext(pluginManager, testInstance);
        context.execute();
    }

    public static TestSetup get() {
        if (defaultTestSetup == null) {
            defaultTestSetup = new TestSetup();
        }
        return defaultTestSetup;
    }

    public static void set(Object testInstance) {
        get().setup(testInstance);
    }
}
