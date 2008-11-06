package com.mycila.plugin.spi;

import com.mycila.plugin.api.Plugin;
import com.mycila.plugin.api.PluginBinding;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Binding<T extends Plugin> implements PluginBinding<T> {

    private final String name;
    private T plugin;

    Binding(String name) {
        this.name = name;
    }

    Binding<T> withPlugin(T plugin) {
        this.plugin = plugin;
        return this;
    }

    public T getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public int compareTo(PluginBinding<T> o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && (o instanceof String || o instanceof Binding) && toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
