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
public final class MycilaTesting {

    public static final String DEFAULT_PLUGIN_DESCRIPTOR = "META-INF/mycila/testing/plugins.properties";

    private static final Map<String, MycilaTesting> instances = new HashMap<String, MycilaTesting>();
    private static MycilaTesting customTestHandler;

    private final PluginManager<TestPlugin> pluginManager;

    private MycilaTesting() {
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class);
    }


    private MycilaTesting(String descriptor) {
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class, descriptor);
    }


    public PluginManager<TestPlugin> pluginManager() {
        return pluginManager;
    }

    public TestHandler handle(Object testInstance) {
        return new TestContext(pluginManager, testInstance);
    }

    /**
     * Get a static TestSetup with the default plugin descriptor which is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins.
     *         This instance is registered statically to avoid reloading plugins each time
     */
    public static MycilaTesting staticDefaultSetup() {
        return staticSetup(DEFAULT_PLUGIN_DESCRIPTOR);
    }

    /**
     * Creates a new TestSetup the default plugin descriptor which is {@link #DEFAULT_PLUGIN_DESCRIPTOR}
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting newDefaultSetup() {
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
    public static MycilaTesting staticSetup(String pluginDescriptor) {
        MycilaTesting testSetup = instances.get(pluginDescriptor);
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
    public static MycilaTesting newSetup(String pluginDescriptor) {
        return new MycilaTesting(pluginDescriptor);
    }

    /**
     * Get a static custom TestSetup instance with an empty {@link com.mycila.plugin.spi.PluginManager}.
     * It will be up to you to add your own plugins at runtime
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins.
     *         This instance is registered statically to avoid reloading and recreating plugins each time.
     */
    public static MycilaTesting staticCustomSetup() {
        if (customTestHandler == null) {
            customTestHandler = newCustomSetup();
        }
        return customTestHandler;
    }

    /**
     * Creates a new custom TestSetup with an empty {@link com.mycila.plugin.spi.PluginManager}.
     * It will be up to you to add your own plugins at runtime
     *
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting newCustomSetup() {
        return new MycilaTesting();
    }

    /**
     * Get a MycilaTesting instance using the strategy defined for this class potentially annotated by
     * {@link com.mycila.testing.core.MycilaPlugins}.
     *
     * @param c test class
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting from(Class<?> c) {
        return from(c == null ? null : c.getAnnotation(MycilaPlugins.class));
    }

    /**
     * Get a MycilaTesting instance using the strategy defined in the provided annotation.
     *
     * @param mycilaPlugins the annotation
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting from(MycilaPlugins mycilaPlugins) {
        if(mycilaPlugins == null) {
            return staticDefaultSetup();
        }
        boolean descBlank = mycilaPlugins.descriptor() == null || mycilaPlugins.descriptor().trim().length() == 0;
        switch (mycilaPlugins.cache()) {
            case SHARED:
                return descBlank ? staticCustomSetup() : staticSetup(mycilaPlugins.descriptor());
            case PER_TEST:
                return descBlank ? newCustomSetup() : newSetup(mycilaPlugins.descriptor());
        }
        throw new AssertionError("Use case not defined for value of enum Cache: " + mycilaPlugins.cache());
    }

}
