package com.mycila.plugin.discovery;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CustomPluginDiscovery implements PluginDiscovery {

    private final Set<Class<?>> plugins = new CopyOnWriteArraySet<Class<?>>();

    public CustomPluginDiscovery add(Class<?> plugin) {
        plugins.add(plugin);
        return this;
    }

    public CustomPluginDiscovery add(Class<?>... plugins) {
        return add(Arrays.asList(plugins));
    }

    public CustomPluginDiscovery add(Iterable<? extends Class<?>> plugins) {
        for (Class<?> plugin : plugins)
            add(plugin);
        return this;
    }

    @Override
    public Iterable<? extends Class<?>> scan() {
        return plugins;
    }
}
