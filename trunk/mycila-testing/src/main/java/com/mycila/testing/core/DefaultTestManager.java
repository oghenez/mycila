package com.mycila.testing.core;

import com.mycila.plugin.PluginManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultTestManager implements TestManager {

    private static final String descriptor = "META-INF/mycila/testing/plugins.properties";

    private String[] exclusions = new String[0];

    public TestManager excludePlugins(String... plugins) {
        this.exclusions = plugins;
        return this;
    }

    public PluginManager getPluginManager() {
        return null;
    }

    public TestManager prepare(Object testInstance) {
        return this;
    }
}
