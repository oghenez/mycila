package com.mycila.inject.util;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Aop {

    private Aop() {
    }

    public static Class<?> getTargetClass(Class<?> proxy) {
        if (proxy.getName().contains("$$")) {
            do {
                proxy = proxy.getSuperclass();
            } while (proxy.getName().contains("$$"));
            return proxy;
        }
        return proxy;
    }

}
