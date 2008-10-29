package com.mycila.plugin;

import static java.lang.String.*;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DuplicatePluginException extends PluginRepositoryException {
    private static final long serialVersionUID = -2413558322415176455L;

    public DuplicatePluginException(URL descriptor, String name) {
        super(format("Duplicate plugin found ! The plugin in this descriptor has already been loaded\n%s", info(descriptor, name)));
    }

    private static String info(URL descriptor, String name) {
        return new StringBuilder()
                .append("- plugins descriptor: ").append(descriptor).append("\n")
                .append("- plugin name: ").append(name).append("\n")
                .toString();
    }
}