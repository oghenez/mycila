package com.mycila.plugin.metadata;

import com.mycila.plugin.Provider;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginExport<T> {
    PluginMetadata getPluginMetadata();

    Class<T> getType();

    Class<?> getScope();

    Provider<? extends T> getProvider();

    String toString();
}
