package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginExport<T> {
    PluginMetadata getPluginMetadata();
    
    String toString();
}
