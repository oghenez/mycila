package com.mycila.plugin.api;

import static java.lang.String.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginReadException extends PluginException {
    private static final long serialVersionUID = 5781552689799348255L;

    public PluginReadException(Throwable t, String message, Object... args) {
        super(format("%s: %s", format(message, args), t.getMessage()), t);
    }
}