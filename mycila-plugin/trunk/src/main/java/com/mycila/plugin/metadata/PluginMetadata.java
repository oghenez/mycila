package com.mycila.plugin.metadata;

import com.mycila.plugin.Invokable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginMetadata {
    Class<?> getType();
    Object getTarget();

    String getName();
    String getDescription();
    String toString();

    Iterable<? extends Class<?>> getActivateAfter();
    Iterable<? extends Class<?>> getActivateBefore();

    Iterable<? extends PluginExport<?>> getExports();

    Invokable onStart();
    Invokable onStop();
}
