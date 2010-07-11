package com.mycila.plugin.metadata;

import com.mycila.plugin.metadata.model.PluginMetadataImpl;
import com.mycila.plugin.util.AopUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMetadataBuilder implements MetadataBuilder {
    @Override
    public final <T> PluginMetadata<T> getMetadata(T plugin) {
        Class<T> pluginClass = (Class<T>) AopUtils.getTargetClass(plugin.getClass());
        PluginMetadataImpl<T> pluginMetadata = new PluginMetadataImpl<T>(
                pluginClass,
                plugin,
                getName(pluginClass),
                getDescription(pluginClass))
                .withActivateBefore(getActivateBefore(pluginClass))
                .withActivateAfter(getActivateAfter(pluginClass));
        for (Method method : pluginClass.getMethods()) {
            if (method.getParameterTypes().length == 0) {
                if (isOnStart(method))
                    pluginMetadata.addOnStart(method.getName());
                if (isOnStop(method))
                    pluginMetadata.addOnStop(method.getName());
            }
        }
        return pluginMetadata;
    }

    protected abstract boolean isOnStart(Method method);

    protected abstract boolean isOnStop(Method method);

    protected abstract String getName(Class<?> pluginClass);

    protected abstract String getDescription(Class<?> pluginClass);

    protected abstract Set<Class<?>> getActivateBefore(Class<?> pluginClass);

    protected abstract Set<Class<?>> getActivateAfter(Class<?> pluginClass);

}
