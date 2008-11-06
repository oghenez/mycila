package com.mycila.plugin.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginBinding<T extends Plugin> extends Comparable<PluginBinding<T>> {
    T getPlugin();

    String getName();
}
