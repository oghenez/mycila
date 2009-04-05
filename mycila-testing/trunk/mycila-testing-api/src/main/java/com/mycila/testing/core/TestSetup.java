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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestSetup {

    public static final String DEFAULT_PLUGIN_DESCRIPTOR = "META-INF/mycila/testing/plugins.properties";
    private static final Map<String, TestSetup> instances = new HashMap<String, TestSetup>();
    private static TestSetup customTestSetup;
    private final PluginManager<TestPlugin> pluginManager;

    private TestSetup() {
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class);
    }


    private TestSetup(String descriptor) {
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class, descriptor);
    }

    /**
     * Get the plugin manager used by this test setup instance.
     *
     * @return {@link com.mycila.plugin.spi.PluginManager} instance
     */
    public PluginManager<TestPlugin> pluginManager() {
        return pluginManager;
    }

    /**
     * Prepare a test
     *
     * @param testInstance The test instance to prepare
     * @return A Test handler that can be used to fire test events such as before and after text executions
     * @throws TestPluginException If the test preparation by plugins fail
     */
    public TestHandler prepare(Object testInstance) throws TestPluginException {
        TestContext context = new TestContext(pluginManager, testInstance);
        context.prepare();
        return context;
    }

    /**
     * Get a static TestSetup with the default plugin descriptor which is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins.
     *         This instance is registered statically to avoid reloading plugins each time
     */
    public static TestSetup staticDefaultSetup() {
        return staticSetup(DEFAULT_PLUGIN_DESCRIPTOR);
    }

    /**
     * Creates a new TestSetup the default plugin descriptor which is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static TestSetup newDefaultSetup() {
        return newSetup(DEFAULT_PLUGIN_DESCRIPTOR);
    }

    /**
     * Get a static TestSetup instance using a specific plugin descriptor to loads plugins.
     * Default plugin descriptor is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @param pluginDescriptor The plugin descriptort to use. It is a property files containing a list of plugins to load. Default plugin descriptor is
     *                         {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     * @return a TestSetup instance which can be used to prepare a test with plugins.
     *         This instance is registered statically to avoid reloading plugins each time
     */
    public static TestSetup staticSetup(String pluginDescriptor) {
        TestSetup testSetup = instances.get(pluginDescriptor);
        if (testSetup == null) {
            testSetup = newSetup(pluginDescriptor);
            instances.put(pluginDescriptor, testSetup);
        }
        return testSetup;
    }

    /**
     * Creates a new TestSetup instance using a specific plugin descriptor to loads plugins.
     * Default plugin descriptor is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @param pluginDescriptor The plugin descriptort to use. It is a property files containing a list of plugins to load. Default plugin descriptor is
     *                         {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static TestSetup newSetup(String pluginDescriptor) {
        return new TestSetup(pluginDescriptor);
    }

    /**
     * Get a static custom TestSetup instance with an empty {@link com.mycila.plugin.spi.PluginManager}.
     * It will be up to you to add your own plugins at runtime
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins.
     *         This instance is registered statically to avoid reloading and recreating plugins each time.
     */
    public static TestSetup staticCustomSetup() {
        if (customTestSetup == null) {
            customTestSetup = newCustomSetup();
        }
        return customTestSetup;
    }

    /**
     * Creates a new custom TestSetup with an empty {@link com.mycila.plugin.spi.PluginManager}.
     * It will be up to you to add your own plugins at runtime
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static TestSetup newCustomSetup() {
        return new TestSetup();
    }

}
