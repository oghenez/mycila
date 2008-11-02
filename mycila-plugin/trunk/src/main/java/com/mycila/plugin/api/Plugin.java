package com.mycila.plugin.api;

import java.util.List;

/**
 * Defines a plugin and its dependencies / order of execution
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Plugin {

    /**
     * Get the list of plugins that should be executed before this one
     *
     * @return empty list, or a list of plugin names
     */
    List<String> getBefore();

    /**
     * Get the list of plugins that should be executed after this one
     *
     * @return empty list, or a list of plugin names
     */
    List<String> getAfter();
}
