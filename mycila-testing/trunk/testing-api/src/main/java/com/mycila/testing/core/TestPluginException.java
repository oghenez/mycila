package com.mycila.testing.core;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class TestPluginException extends RuntimeException {
    private static final long serialVersionUID = -7772706327921003956L;

    public TestPluginException(String message, Object... args) {
        super(String.format(message, args));
    }

    public TestPluginException(String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
    }

    public TestPluginException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
