package com.mycila.plugin.spi;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Ensure {
    private Ensure() {
    }

    static void notNull(String argName, Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }

    static void notEmpty(String message, String argName, String arg) {
        notNull(argName, arg);
        if (isEmpty(arg)) {
            throw new IllegalArgumentException(argName + " cannot be empty: " + message);
        }
    }

    static void notEmpty(String argName, String arg) {
        notNull(argName, arg);
        for (int i = 0; i < arg.length(); i++) {
            if (!Character.isWhitespace(arg.charAt(i))) {
                return;
            }
        }
        throw new IllegalArgumentException(argName + " cannot be empty");
    }

    static boolean isEmpty(String arg) {
        for (int i = 0; i < arg.length(); i++) {
            if (!Character.isWhitespace(arg.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}