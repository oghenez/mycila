package com.mycila.testing.core;

import com.mycila.plugin.PluginManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestManager {

    PluginManager getPluginManager();

    TestManager prepare(Object testInstance);

    TestManager excludePlugins(String... plugins);
}
