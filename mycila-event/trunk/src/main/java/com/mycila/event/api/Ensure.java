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

    public static String notEmpty(String arg, String name) {
        notNull(arg, name);
        if (isEmpty(arg))
            throw new IllegalArgumentException(name + " cannot be empty");
        return arg;
    }

    private static boolean isEmpty(String arg) {
        for (int i = 0; i < arg.length(); i++)
            if (!Character.isWhitespace(arg.charAt(i)))
                return false;
        return true;
    }
}