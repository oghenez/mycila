package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Ensure {
    private Ensure() {
    }

    public static <T> T notNull(T arg, String name) {
        if (arg == null)
            throw new IllegalArgumentException(name + " cannot be null");
        return arg;
    }
}