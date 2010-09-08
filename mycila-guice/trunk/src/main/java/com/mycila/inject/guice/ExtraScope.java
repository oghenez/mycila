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

package com.mycila.inject.guice;

import com.google.inject.*;
import com.mycila.inject.annotation.*;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum ExtraScope implements Provider<ScopeWithAnnotation> {

    EXPIRING_SINGLETON(ExpiringSingleton.class) {
        @Override
        public ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation) {
            return new MycilaScope(scopeAnnotation) {
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
                            if (instance != NULL && expirationTime < System.currentTimeMillis())
                                instance = (T) NULL;
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

    RENEWABLE_SINGLETON(RenewableSingleton.class) {
        @Override
        public ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation) {
            return new MycilaScope(scopeAnnotation) {
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

    WEAK_SINGLETON(WeakSingleton.class) {
        @Override
        public ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation) {
            return new RefScope(scopeAnnotation) {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    SOFT_SINGLETON(SoftSingleton.class) {
        @Override
        public ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation) {
            return new RefScope(scopeAnnotation) {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    CONCURRENT_SINGLETON(ConcurrentSingleton.class) {
        @Override
        public ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation) {
            return new MycilaScope(scopeAnnotation) {
                private final FutureInjector futureInjector = new FutureInjector();
                private final Executor executor = new ThreadPoolExecutor(
                        0, Runtime.getRuntime().availableProcessors() * 10,
                        5, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>(),
                        new DefaultThreadFactory("@ConcurrentSingleton-Thread-"),
                        new ThreadPoolExecutor.DiscardOldestPolicy());

                // thread expiration is by default 10 seconds
                @Inject(optional = true)
                @ConcurrentSingleton.Timeout
                private long timeout = TimeUnit.SECONDS.toNanos(10);

                @Inject
                void init(final Injector injector) {
                    futureInjector.setInjector(injector);
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
                                final long expirationTime = System.nanoTime() + timeout;
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
                                                if (ignored instanceof InterruptedException)
                                                    Thread.currentThread().interrupt();
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

    private final Class<? extends Annotation> defaultAnnotation;

    ExtraScope(Class<? extends Annotation> defaultAnnotation) {
        this.defaultAnnotation = defaultAnnotation;
    }

    public final Class<? extends Annotation> defaultAnnotation() {
        return defaultAnnotation;
    }

    @Override
    public final ScopeWithAnnotation get() {
        return get(defaultAnnotation);
    }

    @Override
    public final String toString() {
        return ExtraScope.class.getSimpleName() + '.' + name();
    }

    public void install(Binder binder) {
        ScopeWithAnnotation scope = get();
        binder.bindScope(defaultAnnotation, scope);
        binder.requestInjection(scope);
    }

    public abstract ScopeWithAnnotation get(Class<? extends Annotation> scopeAnnotation);

    /* static */

    public static Expirity expirity(long value) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        properties.put("value", value);
        return AnnotationMetadata.buildAnnotation(Expirity.class, properties);
    }

    public static void installAll(Binder binder) {
        for (ExtraScope mycilaScope : ExtraScope.values())
            mycilaScope.install(binder);
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

    private static abstract class MycilaScope implements ScopeWithAnnotation {
        private final Class<? extends Annotation> annotationScope;

        private MycilaScope(Class<? extends Annotation> annotationScope) {
            this.annotationScope = annotationScope;
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
