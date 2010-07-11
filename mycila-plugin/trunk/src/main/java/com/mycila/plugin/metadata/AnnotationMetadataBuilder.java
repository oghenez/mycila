package com.mycila.plugin.metadata;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Plugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationMetadataBuilder extends AbstractMetadataBuilder {

    @Override
    protected String getName(Class<?> pluginClass) {
        Plugin plugin = pluginClass.getAnnotation(Plugin.class);
        if (plugin == null || plugin.name().length() == 0)
            return pluginClass.getSimpleName();
        return plugin.name();
    }

    @Override
    protected String getDescription(Class<?> pluginClass) {
        Plugin plugin = pluginClass.getAnnotation(Plugin.class);
        if (plugin == null || plugin.description().length() == 0)
            return pluginClass.getSimpleName();
        return plugin.description();
    }

    @Override
    protected Set<Class<?>> getActivateBefore(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateBefore activateBefore = pluginClass.getAnnotation(ActivateBefore.class);
        if (activateBefore != null && activateBefore.value().length > 0)
            plugins.addAll(Arrays.asList(activateBefore.value()));
        return plugins;
    }

    @Override
    protected Set<Class<?>> getActivateAfter(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateAfter activateAfter = pluginClass.getAnnotation(ActivateAfter.class);
        if (activateAfter != null && activateAfter.value().length > 0)
            plugins.addAll(Arrays.asList(activateAfter.value()));
        return plugins;
    }

    @Override
    protected boolean isOnStart(Method method) {
        return method.isAnnotationPresent(OnStart.class);
    }

    @Override
    protected boolean isOnStop(Method method) {
        return method.isAnnotationPresent(OnStop.class);
    }

}
