package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginMetadata<T> {
    Class<T> getPluginClass();
    T getPlugin();

    String getName();
    String getDescription();
    String toString();

    Iterable<? extends Class<?>> getActivateAfter();
    Iterable<? extends Class<?>> getActivateBefore();

    Iterable<? extends PluginExport<?>> getExports();

    void onStart() throws InvokeException;
    void onStop() throws InvokeException;
}
