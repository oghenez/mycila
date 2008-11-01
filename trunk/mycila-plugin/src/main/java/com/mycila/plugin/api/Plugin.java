package com.mycila.plugin.api;

import java.util.List;

/**
 * Defines a plugin and its dependencies / order of execution
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Plugin {

    /**
     * Get the execution order needed for this plugin to work.
     * This order is needed by the {@link com.mycila.plugin.api.PluginResolver} to be able to provides
     * a sorted list of plugins, that can be processed in order to meet their dependencies.
     * <p/>
     * I.E.:<br/>
     * If a plugin A must be executed after a plugin B, the execution order will be B, A<br/>
     * If a plugin A depends on another plugin B, the execution order will be B, A<br/>
     *
     * @return empty string, or a list of plugin names representing the dependencies or execution order needed for this plugin
     */
    List<String> getExecutionOrder();
}
