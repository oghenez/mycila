/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.inject.scope;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Destroyable;
import com.mycila.inject.util.AnnotationMetadata;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum ExtraScope implements Provider<MappedScope> {

    /*CLOSEABLE_SINGLETON {
        @Override
        public MappedScope get() {
            return new MycilaScope(CloseableSingleton.class) {
                @Override
                public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
                    return new Provider<T>() {
                        private volatile Object instance;
                        public T get() {
                            if (instance == null) {
                                synchronized (InternalInjectorCreator.class) {
                                    if (instance == null) {
                                        T provided = creator.get();
                                        if (provided instanceof CircularDependencyProxy) {
                                            return provided;
                                        }

                                        Object providedOrSentinel = (provided == null) ? NULL : provided;
                                        if (instance != null && instance != providedOrSentinel) {
                                            throw new ProvisionException(
                                                    "Provider was reentrant while creating a singleton");
                                        }

                                        instance = providedOrSentinel;
                                    }
                                }
                            }

                            Object localInstance = instance;
                            // This is safe because instance has type T or is equal to NULL
                            @SuppressWarnings("unchecked")
                            T returnedInstance = (localInstance != NULL) ? (T) localInstance : null;
                            return returnedInstance;
                        }

                        public String toString() {
                            return String.format("%s[%s]", creator, CLOSEABLE_SINGLETON);
                        }
                    };
                }
            };
        }
    },*/

    EXPIRING_SINGLETON {
        @Override
        public MappedScope get() {
            return new MycilaScope(ExpiringSingleton.class) {
                boolean hasJSR250Module;

                @Inject
                void init(final Injector injector) {
                    hasJSR250Module = Jsr250.hasJSR250Module(injector);
                }

                @Override
                public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
                    Annotation annotation = key.getAnnotation();
                    if (!Expirity.class.isInstance(annotation))
                        throw new IllegalStateException("Binding " + key + " in scope EXPIRING_SINGLETON must be binded with @Expirity");
                    final long expirationDelay = ((Expirity) annotation).value();
                    return new Provider<T>() {
                        private volatile T instance;
                        private volatile long expirationTime;

                        public T get() {
                            if (instance == null) {
                                synchronized (this) {
                                    if (instance == null) {
                                        instance = creator.get();
                                        expirationTime = System.currentTimeMillis() + expirationDelay;
                                    }
                                }
                            }
                            if (instance != NULL && expirationTime < System.currentTimeMillis()) {
                                T old = instance;
                                instance = (T) NULL;
                                if (hasJSR250Module)
                                    Jsr250.preDestroy(old);
                            }
                            return instance == NULL ? null : instance;
                        }

                        public String toString() {
                            return String.format("%s[%s]", creator, EXPIRING_SINGLETON);
                        }
                    };
                }
            };
        }
    },

    RENEWABLE_SINGLETON {
        @Override
        public MappedScope get() {
            return new MycilaScope(RenewableSingleton.class) {
                boolean hasJSR250Module;

                @Inject
                void init(final Injector injector) {
                    hasJSR250Module = Jsr250.hasJSR250Module(injector);
                }

                @Override
                public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
                    Annotation annotation = key.getAnnotation();
                    if (!Expirity.class.isInstance(annotation))
                        throw new IllegalStateException("Binding " + key + " in scope RENEWABLE_SINGLETON must be binded with @Expirity");
                    final long expirationDelay = ((Expirity) annotation).value();
                    return new Provider<T>() {
                        private volatile T instance;
                        private volatile long expirationTime;

                        public T get() {
                            if (expirationTime < System.currentTimeMillis()) {
                                synchronized (this) {
                                    if (expirationTime < System.currentTimeMillis()) {
                                        T old = instance;
                                        if (hasJSR250Module)
                                            Jsr250.preDestroy(old);
                                        instance = creator.get();
                                        expirationTime = System.currentTimeMillis() + expirationDelay;
                                    }
                                }
                            }
                            return instance;
                        }

                        public String toString() {
                            return String.format("%s[%s]", creator, RENEWABLE_SINGLETON);
                        }
                    };
                }
            };
        }
    },

    WEAK_SINGLETON {
        @Override
        public MappedScope get() {
            return new RefScope(WeakSingleton.class) {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    SOFT_SINGLETON {
        @Override
        public MappedScope get() {
            return new RefScope(SoftSingleton.class) {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    CONCURRENT_SINGLETON {
        @Override
        public MappedScope get() {
            return new MycilaScope(ConcurrentSingleton.class) {
                private final FutureInjector futureInjector = new FutureInjector();
                private final ExecutorService executor = new ThreadPoolExecutor(
                        0, Runtime.getRuntime().availableProcessors() * 10,
                        5, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>(),
                        new DefaultThreadFactory("@" + ConcurrentSingleton.class.getSimpleName() + "-Thread-"),
                        new ThreadPoolExecutor.DiscardOldestPolicy());

                // thread expiration is by default 10 seconds
                private long expirity = TimeUnit.SECONDS.toNanos(10);

                @Inject
                void init(final Injector injector) {
                    futureInjector.setInjector(injector);
                    Binding<Long> b = (Binding<Long>) injector.getBindings().get(Key.get(long.class, ConcurrentSingleton.ThreadExpiration.class));
                    if (b != null)
                        expirity = TimeUnit.MILLISECONDS.toNanos(b.getProvider().get());
                }

                @PreDestroy
                public void shutdown() {
                    executor.shutdown();
                }

                @Override
                public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
                    // Create a monitoring thread for this singleton.
                    // This thread will wait for the key to be available and call the provider to get the singleton.
                    // This thread must also be non bloquant so that it can be interrupted if the program finishes
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // this thread will expire if not ended within the given timeout
                                final long expirationTime = System.nanoTime() + expirity;
                                while (!Thread.currentThread().isInterrupted() && System.nanoTime() < expirationTime) {
                                    final Injector injector = futureInjector.waitAndGet(500, TimeUnit.MILLISECONDS).get();
                                    if (injector == null) {
                                        // May not be ready now. Retry later.
                                        Thread.sleep(500);
                                    } else {
                                        final Binding<?> binding = injector.getExistingBinding(key);
                                        if (binding == null) {
                                            // wait: perhaps it is not yet available to be constructed
                                            Thread.sleep(500);
                                        } else {
                                            try {
                                                // call the provider it load the singleton in this thread
                                                binding.getProvider().get();
                                            } catch (Throwable ignored) {
                                                // completely ignore the exception: since this provider calls the FutureProvider,
                                                // the FutureTask will memoize the exception and rethrow it for subsequent calls
                                                // not within this loader thread
                                            }
                                            return;
                                        }
                                    }
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                    // Future task that will memoize the provided singleton.
                    // The task will be run either in the caller thread, or by the monitoring thread
                    return new FutureProvider<T>(unscoped);
                }
            };
        }
    };

    /* scope class */

    @Override
    public final String toString() {
        return ExtraScope.class.getSimpleName() + '.' + name();
    }

    /* static */

    /**
     * Expiration time, in milliseconds
     */
    public static Expirity expirity(long value) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        properties.put("value", value);
        return AnnotationMetadata.buildAnnotation(Expirity.class, properties);
    }

    /* private */

    private static final Object NULL = new Object();

    private static class DefaultThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicLong threadNumber = new AtomicLong();
        private final String namePrefix;

        private DefaultThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    private static abstract class RefScope extends MycilaScope {
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

    private static abstract class MycilaScope implements MappedScope {
        private final Class<? extends Annotation> annotationScope;
        private final boolean isSingleton;

        private MycilaScope(Class<? extends Annotation> annotationScope) {
            this.annotationScope = annotationScope;
            isSingleton = annotationScope.isAnnotationPresent(Jsr250Destroyable.class);
        }

        @Override
        public final Class<? extends Annotation> getScopeAnnotation() {
            return annotationScope;
        }

        @Override
        public final String toString() {
            return ExtraScope.class.getSimpleName() + "." + annotationScope.getSimpleName();
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MycilaScope that = (MycilaScope) o;
            return annotationScope.equals(that.annotationScope);
        }

        @Override
        public final int hashCode() {
            return annotationScope.hashCode() + 31 * getClass().hashCode();
        }

        @Override
        public final boolean isSingleton() {
            return isSingleton;
        }
    }

    private static final class FutureInjector {
        private volatile WeakReference<Injector> injector = new WeakReference<Injector>(null);
        private final CountDownLatch injectorAvailable = new CountDownLatch(1);

        public void setInjector(Injector injector) {
            if (this.injector.get() != null) return;
            this.injector = new WeakReference<Injector>(injector);
            injectorAvailable.countDown();
        }

        public Reference<Injector> waitAndGet(long timeout, TimeUnit unit) throws InterruptedException {
            // We need to apply a timeout in case the user forgot to request injection on the scope to not block the threads.
            // If the injector is not ready within this timeout, we consider it as inexisting
            injectorAvailable.await(timeout, unit);
            return injector;
        }
    }

    private static final class FutureProvider<T> extends FutureTask<T> implements Provider<T> {
        private FutureProvider(final Provider<T> unscoped) {
            super(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return unscoped.get();
                }
            });
        }

        @Override
        public T get() {
            try {
                if (!isDone()) run();
                return super.get();
            } catch (ExecutionException e) {
                throw (RuntimeException) e.getCause();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ProvisionException("Interrupted during provision");
            }
        }
    }
}
