package com.mycila.plugin;

import static java.lang.String.*;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginLoadException extends PluginRepositoryException {
    private static final long serialVersionUID = 2974368486006410596L;

    public PluginLoadException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType, Throwable e) {
        super(format("%s: %s\n%s", message, e.getMessage(), info(descriptor, name, clazz, pluginsType)), e);
    }

    public PluginLoadException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType) {
        super(format("%s\n%s", message, info(descriptor, name, clazz, pluginsType)));
    }

    private static String info(URL descriptor, String name, String clazz, Class<?> pluginsType) {
        return new StringBuilder()
                .append("- plugins descriptor: ").append(descriptor).append("\n")
                .append("- plugins type: ").append(pluginsType.getName()).append("\n")
                .append("- plugin name: ").append(name).append("\n")
                .append("- plugin class: ").append(clazz)
                .toString();
    }

}