package com.mycila.event.util;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Providers {

    private Providers() {
    }

    public static <T> Provider<T> cache(final Provider<? extends T> p) {
        return new Provider<T>() {
            private final T t = p.get();

            @Override
            public T get() {
                return t;
            }
        };
    }

    public static <T> Provider<T> cache(final T val) {
        return new Provider<T>() {
            private final T t = val;

            @Override
            public T get() {
                return t;
            }
        };
    }

}
