package com.mycila.plugin.discovery;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginDiscovery {
    Iterable<? extends Class<?>> scan() throws PluginDiscoveryException;
}
