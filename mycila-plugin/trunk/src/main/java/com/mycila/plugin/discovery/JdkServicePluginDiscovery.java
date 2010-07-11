package com.mycila.plugin.discovery;

import com.mycila.plugin.discovery.support.ServiceClassLoader;
import com.mycila.plugin.util.ClassUtils;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JdkServicePluginDiscovery implements PluginDiscovery {

    private final Class<?> markerClass;
    private final ClassLoader classLoader;

    public JdkServicePluginDiscovery(Class<?> markerClass) {
        this(markerClass, ClassUtils.getDefaultClassLoader());
    }

    public JdkServicePluginDiscovery(Class<?> markerClass, ClassLoader classLoader) {
        this.markerClass = markerClass;
        this.classLoader = classLoader;
    }

    @Override
    public Iterable<? extends Class<?>> scan() {
        return ServiceClassLoader.load(markerClass, classLoader);
    }
}
