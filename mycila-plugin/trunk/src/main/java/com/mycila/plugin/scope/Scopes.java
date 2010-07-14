package com.mycila.plugin.scope;

import com.mycila.plugin.Provider;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Scopes {

    private static final Object NULL = new Object();

    private Scopes() {
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T nullT() {
        return (T) NULL;
    }

    public static final class None extends AbstractScope {
        @Override
        public <T> Provider<T> getProvider(Annotation annotation, Provider<T> unscoped) {
            return unscoped;
        }
    }

    public static final class Singleton extends AbstractScope {
        @Override
        public <T> Provider<T> getProvider(Annotation annotation, final Provider<T> unscoped) {
            return new Provider<T>() {
                private volatile T instance;

                @Override
                public T get() {
                    if (instance == null)
                        synchronized (this) {
                            if (instance == null)
                                instance = unscoped.get();
                        }
                    return instance;
                }
            };
        }
    }

    public static final class WeakSingleton extends RefSingleton {
        @Override
        protected <T> Reference<T> build(T instance) {
            return new WeakReference<T>(instance);
        }
    }

    public static final class SoftSingleton extends RefSingleton {
        @Override
        protected <T> Reference<T> build(T instance) {
            return new WeakReference<T>(instance);
        }
    }

    public static final class ExpiringSingleton extends AbstractScope {
        @Override
        public <T> Provider<T> getProvider(Annotation annotation, final Provider<T> unscoped) {
            if (!(annotation instanceof com.mycila.plugin.scope.annotation.ExpiringSingleton))
                throw new IllegalStateException("Scopes." + this + " only supports @ExpiringSingleton");
            final long duration = ((com.mycila.plugin.scope.annotation.ExpiringSingleton) annotation).value();
            return new Provider<T>() {
                private volatile T instance;
                private volatile long expirationTime;

                @Override
                public T get() {
                    if (expirationTime < System.currentTimeMillis()) {
                        synchronized (this) {
                            if (expirationTime < System.currentTimeMillis()) {
                                instance = unscoped.get();
                                expirationTime = System.currentTimeMillis() + duration;
                            }
                        }
                    }
                    return instance;
                }
            };
        }
    }

    private static abstract class RefSingleton extends AbstractScope {
        @Override
        public final <T> Provider<T> getProvider(Annotation annotation, final Provider<T> unscoped) {
            return new Provider<T>() {
                private volatile Reference<T> ref;

                @Override
                public T get() {
                    return ref == null ? newRef(unscoped) : fromRef(unscoped);
                }

                private T fromRef(Provider<T> unscoped) {
                    T instance = ref.get();
                    if (instance == NULL) return null;
                    if (instance != null) return instance;
                    return newRef(unscoped);
                }

                private T newRef(Provider<T> unscoped) {
                    T instance = unscoped.get();
                    ref = build(instance == null ? Scopes.<T>nullT() : instance);
                    return instance;
                }
            };
        }

        protected abstract <T> Reference<T> build(T instance);
    }

    private static abstract class AbstractScope implements Scope {
        @Override
        public final String toString() {
            return "@" + getClass().getSimpleName();
        }
    }
}
