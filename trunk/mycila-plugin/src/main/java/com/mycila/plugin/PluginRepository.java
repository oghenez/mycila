package com.mycila.plugin;

import java.util.Collection;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginRepository<T> {

    Map<String, T> loadPlugins() throws PluginRepositoryException;

    void setExclusions(String... exclusions);

    void setExclusions(Collection<String> exclusions);

    void setLoader(ClassLoader loader);
}
