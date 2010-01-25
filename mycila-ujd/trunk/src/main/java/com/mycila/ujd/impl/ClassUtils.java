package com.mycila.ujd.impl;

import java.lang.reflect.Proxy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ClassUtils {
    static String getPath(Class<?> aClass) {
        return aClass.getName().replace('.', '/') + ".class";
    }

    static boolean isGeneratedClass(Class<?> aClass) {
        return Proxy.isProxyClass(aClass)
                || (aClass.getClassLoader() != null
                && aClass.getClassLoader().getResource(ClassUtils.getPath(aClass)) == null);
    }
}
