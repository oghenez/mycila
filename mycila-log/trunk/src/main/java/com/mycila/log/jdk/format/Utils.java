package com.mycila.log.jdk.format;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Utils {
    private Utils() {
    }

    @SuppressWarnings({"unchecked"})
    public static final String EOL = java.security.AccessController.<String>doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    @SuppressWarnings({"unchecked"})
    public static <T> T instantiateByClassName(String className, Class superClass, T defaultValue) {
        if (className != null) {
            try {
                Class classObj = Thread.currentThread().getContextClassLoader().loadClass(className);
                if (!superClass.isAssignableFrom(classObj)) {
                    return defaultValue;
                }
                return (T) classObj.newInstance();
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }
}