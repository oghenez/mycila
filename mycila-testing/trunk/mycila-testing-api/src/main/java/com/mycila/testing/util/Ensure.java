package com.mycila.testing.util;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Ensure {
    private Ensure() {}

    public static void notNull(String argName, Object arg) {
        if(arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }
}
