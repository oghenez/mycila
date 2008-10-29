package com.mycila.plugin;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class PluginRepositoryException extends RuntimeException {

    private static final long serialVersionUID = -35690073735188474L;

    public PluginRepositoryException(Throwable t, String message, Object... args) {
        super(String.format("%s: %s", String.format(message, args), t.getMessage()), t);
    }

    public PluginRepositoryException(String message) {
        super(message);
    }

    public PluginRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
