package com.mycila.plugin.spi;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class PluginUtils {
    static boolean isEmpty(String name) {
        if (name != null) {
            for (char c : name.toCharArray()) {
                if (!Character.isWhitespace(c)) {
                    return false;
                }
            }
        }
        return true;
    }
}
