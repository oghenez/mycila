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

package com.mycila.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.mycila.guice.annotation.scope.Expirity;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Scopes {

    /**
     * A sentinel value representing null.
     */
    private static final Object NULL = new Object();

    public static final ScopeWithAnnotation EXPIRING_SINGLETON = expiringSingleton(com.mycila.guice.annotation.scope.ExpiringSingleton.class);
    public static final ScopeWithAnnotation WEAK_SINGLETON = weakSingleton(com.mycila.guice.annotation.scope.WeakSingleton.class);
    public static final ScopeWithAnnotation SOFT_SINGLETON = softSingleton(com.mycila.guice.annotation.scope.SoftSingleton.class);

    private Scopes() {
    }

    public static void bind(Binder binder) {
        binder.bindScope(com.mycila.guice.annotation.scope.ExpiringSingleton.class, EXPIRING_SINGLETON);
        binder.bindScope(com.mycila.guice.annotation.scope.WeakSingleton.class, WEAK_SINGLETON);
        binder.bindScope(com.mycila.guice.annotation.scope.SoftSingleton.class, SOFT_SINGLETON);
    }

    public static ScopeWithAnnotation expiringSingleton(Class<? extends Annotation> scopeAnnotation) {
        return new ExpiringSingleton(scopeAnnotation);
    }

    public static ScopeWithAnnotation weakSingleton(Class<? extends Annotation> scopeAnnotation) {
        return new WeakSingleton(scopeAnnotation);
    }

    public static ScopeWithAnnotation softSingleton(Class<? extends Annotation> scopeAnnotation) {
        return new SoftSingleton(scopeAnnotation);
    }

    private static final class ExpiringSingleton extends AbstractScopeWithAnnotation {
        private ExpiringSingleton(Class<? extends Annotation> scopeAnnotation) {
            super(scopeAnnotation);
        }

        @Override
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
            Annotation annotation = key.getAnnotation();
            if (!Expirity.class.isInstance(annotation))
                throw new IllegalStateException("Binding " + key + " in scope ExpiringSingleton must be binded with @Expirity");
            final long expirationDelay = ((Expirity) annotation).value();
            return new Provider<T>() {
                private volatile T instance;
                private volatile long expirationTime;

                public T get() {
                    if (expirationTime < System.currentTimeMillis()) {
                        synchronized (this) {
                            if (expirationTime < System.currentTimeMillis()) {
                                instance = creator.get();
                                expirationTime = System.currentTimeMillis() + expirationDelay;
                            }
                        }
                    }
                    return instance;
                }

                public String toString() {
                    return String.format("%s[%s]", creator, ExpiringSingleton.this);
                }
            };
        }
    }

    private static final class WeakSingleton extends RefScope {
        private WeakSingleton(Class<? extends Annotation> scopeAnnotation) {
            super(scopeAnnotation);
        }

        @Override
        protected <T> Reference<T> build(T instance) {
            return new WeakReference<T>(instance);
        }
    }

    private static final class SoftSingleton extends RefScope {
        private SoftSingleton(Class<? extends Annotation> scopeAnnotation) {
            super(scopeAnnotation);
        }

        @Override
        protected <T> Reference<T> build(T instance) {
            return new SoftReference<T>(instance);
        }
    }

    private static abstract class RefScope extends AbstractScopeWithAnnotation {
        RefScope(Class<? extends Annotation> scopeAnnotation) {
            super(scopeAnnotation);
        }

        @Override
        public final <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {
                private volatile Reference<T> ref;

                public T get() {
                    return ref == null ? newRef(creator) : fromRef(creator);
                }

                private T fromRef(Provider<T> unscoped) {
                    T instance = ref.get();
                    if (instance == NULL) return null;
                    if (instance != null) return instance;
                    return newRef(unscoped);
                }

                private T newRef(Provider<T> unscoped) {
                    T instance = unscoped.get();
                    ref = (Reference<T>) build(instance == null ? NULL : instance);
                    return instance;
                }

                public String toString() {
                    return String.format("%s[%s]", creator, RefScope.this);
                }
            };
        }

        protected abstract <T> Reference<T> build(T instance);
    }

    private static abstract class AbstractScopeWithAnnotation implements ScopeWithAnnotation {
        private final Class<? extends Annotation> scopeAnnotation;

        AbstractScopeWithAnnotation(Class<? extends Annotation> scopeAnnotation) {
            this.scopeAnnotation = scopeAnnotation;
        }

        @Override
        public final Class<? extends Annotation> getScopeAnnotation() {
            return null;
        }

        @Override
        public final String toString() {
            return "Scopes." + getClass().getSimpleName();
        }
    }
}
