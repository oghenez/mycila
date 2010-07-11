package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MetadataBuilder {
    <T> PluginMetadata<T> getMetadata(Class<T> pluginClass);
}
