package com.mycila.testing.plugin.powermock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Static {
    static int firstStaticMethod(String string) {
        return string.length();
    }

    static int secondStaticMethod() {
        throw new UnsupportedOperationException();
    }

    static void thirdStaticMethod() {
    }
}
