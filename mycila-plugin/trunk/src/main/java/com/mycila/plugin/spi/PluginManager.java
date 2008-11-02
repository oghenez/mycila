package com.mycila.plugin.spi;

import com.mycila.plugin.api.Plugin;
import com.mycila.plugin.api.PluginCache;
import com.mycila.plugin.api.PluginLoader;
import com.mycila.plugin.api.PluginResolver;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginManager<T extends Plugin> {

    private final PluginLoader<T> loader;
    private final PluginResolver<T> resolver;
    private final PluginCache<T> cache;

    public PluginManager(Class<T> pluginType, String pluginDescriptor) {
        loader = new DefaultPluginLoader<T>(pluginType, pluginDescriptor);
        cache = new DefaultPluginCache<T>(loader);
        resolver = new DefaultPluginResolver<T>(cache);
    }

    public PluginCache<T> getCache() {
        return cache;
    }

    public PluginLoader<T> getLoader() {
        return loader;
    }

    public PluginResolver<T> getResolver() {
        return resolver;
    }
}
