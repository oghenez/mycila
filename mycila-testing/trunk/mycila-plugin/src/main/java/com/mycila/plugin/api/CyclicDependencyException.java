package com.mycila.plugin.api;

import static java.lang.String.*;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CyclicDependencyException extends PluginException {
    private static final long serialVersionUID = -6644476561325060279L;

    private final List<String> pluginsNames;
    private final String pluginName;
    private final Plugin plugin;
    private final int insertionIndex;

    public CyclicDependencyException(List<String> pluginsNames, String pluginName, Plugin plugin, int insertionIndex) {
        super(format("Cyclic dependencies found when ordering loaded plugins\n%s", info(pluginsNames, pluginName, plugin, insertionIndex)));
        this.pluginsNames = pluginsNames;
        this.pluginName = pluginName;
        this.plugin = plugin;
        this.insertionIndex = insertionIndex;
    }

    public int getInsertionIndex() {
        return insertionIndex;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getPluginName() {
        return pluginName;
    }

    public List<String> getPluginsNames() {
        return pluginsNames;
    }

    private static String info(List<String> pluginsNames, String pluginName, Plugin plugin, int insertionIndex) {
        StringBuilder sb = new StringBuilder()
                .append("- currently resolved execution order: ").append(pluginsNames).append("\n")
                .append("- plugin name: ").append(pluginName).append("\n")
                .append("- plugin before dependencies: ").append(plugin.getBefore()).append("\n")
                .append("- plugin after dependencies: ").append(plugin.getAfter()).append("\n")
                .append("- insertion position: ").append(insertionIndex);
        if (insertionIndex == pluginsNames.size()) {
            sb.append(" (at the end)");
        } else {
            sb.append(" (before ").append(pluginsNames.get(insertionIndex)).append(")");
        }
        return sb.append("\n").toString();
    }
}
