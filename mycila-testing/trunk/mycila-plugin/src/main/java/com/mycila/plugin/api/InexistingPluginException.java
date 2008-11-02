package com.mycila.plugin.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InexistingPluginException extends PluginException {
    private static final long serialVersionUID = 5781552689799348255L;
    private final String plugin;

    public InexistingPluginException(String plugin) {
        super(String.format("Plugin '%s' does not exist", plugin));
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }
}
