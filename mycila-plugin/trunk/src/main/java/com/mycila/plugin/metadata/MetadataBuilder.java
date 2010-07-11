package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MetadataBuilder {
    PluginMetadata getMetadata(Object plugin);
}
