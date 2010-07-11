package com.mycila.plugin.metadata.model;

import com.mycila.plugin.metadata.PluginExport;
import com.mycila.plugin.metadata.PluginMetadata;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExportMethodImpl<T> implements PluginExport<T> {

    public ExportMethodImpl(Method method, Class<T> type) {

    }

    @Override
    public PluginMetadata getPluginMetadata() {
        return null;
    }
}
