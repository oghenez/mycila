/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.plugin.spi;

import com.mycila.plugin.Provider;
import com.mycila.plugin.Scope;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Scopes {

    public static final com.mycila.plugin.annotation.scope.None DEFAULT = new com.mycila.plugin.annotation.scope.None() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return com.mycila.plugin.annotation.scope.None.class;
        }
    };

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
            if (!(annotation instanceof com.mycila.plugin.annotation.scope.ExpiringSingleton))
                throw new IllegalStateException("Scopes." + this + " only supports @ExpiringSingleton");
            final long duration = ((com.mycila.plugin.annotation.scope.ExpiringSingleton) annotation).value();
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
