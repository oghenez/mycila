package com.mycila.sandbox.collection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Wrapper {

    private Wrapper() {
    }

    @SuppressWarnings({"unchecked"})
    public static <K, V> Map<K, V> wrap(final Map<K, V> map, final Class<?> type) {
        return (Map<K, V>) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{Map.class},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (args != null && args.length == 1 && method.getName().equals("get")) {
                            K key = (K) args[0];
                            V v = map.get(key);
                            if (v == null) map.put(key, v = (V) type.newInstance());
                            return v;
                        }
                        return method.invoke(map, args);
                    }
                });
    }

}
