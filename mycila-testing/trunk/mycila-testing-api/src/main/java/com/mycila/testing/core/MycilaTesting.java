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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.plugin.spi.PluginManager;
import com.mycila.testing.core.annot.ConfigureMycilaPlugins;
import com.mycila.testing.core.annot.MycilaPlugins;
import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.core.api.TestNotifier;
import com.mycila.testing.core.plugin.TestPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaTesting {

    public static final String DEFAULT_PLUGIN_DESCRIPTOR = "META-INF/mycila/testing/plugins.properties";
    private static final Logger LOGGER = Loggers.get(MycilaTesting.class);
    private static final Map<String, MycilaTesting> instances = new HashMap<String, MycilaTesting>();
    private static MycilaTesting customTestHandler;

    private final PluginManager<TestPlugin> pluginManager;

    private MycilaTesting() {
        LOGGER.debug("Creating new empty plugin manager");
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class);
    }


    private MycilaTesting(String descriptor) {
        LOGGER.debug("Creating new plugin manager from descriptor {0}", descriptor);
        pluginManager = new PluginManager<TestPlugin>(TestPlugin.class, descriptor);
    }


    public PluginManager<TestPlugin> pluginManager() {
        return pluginManager;
    }

    public TestNotifier createNotifier(Object testInstance) {
        notNull("Test instance", testInstance);
        return new TestContextImpl(pluginManager, testInstance);
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
        notNull("Plugin descriptor", pluginDescriptor);
        MycilaTesting testSetup = instances.get(pluginDescriptor);
        if (testSetup == null) {
            testSetup = newSetup(pluginDescriptor);
            LOGGER.debug("Registering new shared plugins for descriptor {0}", pluginDescriptor);
            instances.put(pluginDescriptor, testSetup);
        } else {
            LOGGER.debug("Reusing shared plugins for descriptor {0}", pluginDescriptor);
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
        notNull("Plugin descriptor", pluginDescriptor);
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
            LOGGER.debug("Registering new shared empty plugin manager");
            customTestHandler = newCustomSetup();
        } else {
            LOGGER.debug("Reusing existing shared empty plugin manager");
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
     * {@link com.mycila.testing.core.annot.MycilaPlugins}.
     *
     * @param c test class
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting from(Class<?> c) {
        notNull("Test class", c);
        return from(c.getAnnotation(MycilaPlugins.class));
    }

    /**
     * Get a MycilaTesting instance using the strategy defined in the provided annotation.
     *
     * @param mycilaPlugins the annotation
     * @return a TestSetup instance which can be used to prepare a test with plugins
     */
    public static MycilaTesting from(MycilaPlugins mycilaPlugins) {
        notNull("MycilaPlugins annotation", mycilaPlugins);
        if (mycilaPlugins.value() == null) {
            return newDefaultSetup();
        }
        boolean descBlank = mycilaPlugins.descriptor() == null || mycilaPlugins.descriptor().trim().length() == 0;
        switch (mycilaPlugins.value()) {
            case SHARED:
                return descBlank ? staticCustomSetup() : staticSetup(mycilaPlugins.descriptor());
            case UNSHARED:
                return descBlank ? newCustomSetup() : newSetup(mycilaPlugins.descriptor());
        }
        throw new AssertionError("Use case not defined for value of enum Cache: " + mycilaPlugins.value());
    }

    /**
     * Configure the Plugin manager of this MycilaTesting from the given class. It will search for all methods
     * annotated by {@link com.mycila.testing.core.annot.ConfigureMycilaPlugins}s and call those having the
     * {@link com.mycila.plugin.spi.PluginManager} as a parameter:
     * {@code @ConfigureMycilaPlugins void configure(PluginManager<TestPlugin> pluginManager) {...} }
     *
     * @param testInstance The object having configure methods
     * @return this
     */
    public MycilaTesting configure(Object testInstance) {
        notNull("Test instance", testInstance);
        List<Method> methods = new ArrayList<Method>();
        Class<?> c = testInstance.getClass();
        do {
            //TODO: METHOD OVERRIDE CHECK
            for (Method method : c.getDeclaredMethods()) {
                Class<?>[] types = method.getParameterTypes();
                if (method.isAnnotationPresent(ConfigureMycilaPlugins.class) && types.length == 1 && types[0].equals(PluginManager.class)) {
                    method.setAccessible(true);
                    methods.add(0, method);
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        if (!methods.isEmpty()) {
            PluginManager<TestPlugin> pluginManager = pluginManager();
            for (Method method : methods) {
                LOGGER.debug("Configuring plugin manager through method {0}.{1}...", method.getDeclaringClass().getName(), method.getName());
                try {
                    method.invoke(testInstance, pluginManager);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
                }
            }
        }
        return this;
    }
}
