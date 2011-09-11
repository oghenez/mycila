package com.mycila.sandbox.override;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassUtils {
    public static Iterable<Method> getAllDeclaredMethods(Class<?> clazz) {
        LinkedHashMap<MethodSignature, Method> all = new LinkedHashMap<MethodSignature, Method>();
        while (clazz != null && clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                MethodSignature signature = MethodSignature.of(method);
                if (!all.containsKey(signature))
                    all.put(signature, method);
            }
            clazz = clazz.getSuperclass();
        }
        return new LinkedList<Method>(all.values());
    }
}
