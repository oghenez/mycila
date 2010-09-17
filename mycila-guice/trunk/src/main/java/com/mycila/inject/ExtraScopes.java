/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.inject;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.mycila.inject.annotation.Jsr250Singleton;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.scope.ResetScope;

import javax.annotation.PreDestroy;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ExtraScopes {

    private ExtraScopes() {
    }

    public static Scope expiringSingleton(final long expirity, final TimeUnit unit) {
        return new ExpiringSingleton(unit.toMillis(expirity));
    }

    public static Scope renewableSingleton(final long expirity, final TimeUnit unit) {
        return new RenewableSingleton(unit.toMillis(expirity));
    }

    public static Scope weakSingleton() {
        return new WeakSingleton();
    }

    public static Scope softSingleton() {
        return new SoftSingleton();
    }

    public static Scope concurrentSingleton() {
        return concurrentSingleton(10, TimeUnit.SECONDS);
    }

    public static Scope concurrentSingleton(final long expirity, final TimeUnit unit) {
        return new ConcurrentSingleton(unit.toMillis(expirity));
    }

    public static ResetScope resetSingleton() {
        return new ResetSingleton();
    }

    /* private */

    private static final Object NULL = new Object();

    @Jsr250Singleton
    private static final class ConcurrentSingleton extends MycilaScope {
        final long expirationDelay;

        private ConcurrentSingleton(long expirationDelay) {
            this.expirationDelay = expirationDelay;
        }

        private final FutureInjector futureInjector = new FutureInjector();
        private final ExecutorService executor = new ThreadPoolExecutor(
                0, Runtime.getRuntime().availableProcessors() * 10,
                5, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new DefaultThreadFactory("@" + ConcurrentSingleton.class.getSimpleName() + "-Thread-"),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        @Inject
        void initFuture(Injector injector) {
            futureInjector.setInjector(injector);
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
                        final long expirationTime = System.currentTimeMillis() + expirationDelay;
                        while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < expirationTime) {
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
            return new FutureProvider<T>(key, unscoped);
        }
    }

    @Jsr250Singleton
    private static final class WeakSingleton extends RefScope {
        @Override
        protected <T> Reference<T> build(T instance) {
            return new WeakReference<T>(instance);
        }
    }

    @Jsr250Singleton
    private static final class SoftSingleton extends RefScope {
        @Override
        protected <T> Reference<T> build(T instance) {
            return new SoftReference<T>(instance);
        }
    }

    @Jsr250Singleton
    private static final class RenewableSingleton extends MycilaScope {
        final long expirationDelay;

        private RenewableSingleton(long expirationDelay) {
            this.expirationDelay = expirationDelay;
        }

        @Override
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {
                private volatile T instance;
                private volatile long expirationTime;

                @Override
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

                @Override
                public String toString() {
                    return String.format("%s[%s]", creator, RenewableSingleton.this);
                }
            };
        }
    }

    @Jsr250Singleton
    private static final class ExpiringSingleton extends MycilaScope {
        final long expirationDelay;

        private ExpiringSingleton(long expirationDelay) {
            this.expirationDelay = expirationDelay;
        }

        @Override
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {
                private volatile T instance;
                private volatile long expirationTime;

                @Override
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

                @Override
                public String toString() {
                    return String.format("%s[%s]", creator, ExpiringSingleton.this);
                }
            };
        }
    }

    @Jsr250Singleton
    private static final class ResetSingleton extends MycilaScope implements ResetScope {
        private final Map<Key<?>, Object> singletons = new HashMap<Key<?>, Object>();
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock r = lock.readLock();
        private final Lock w = lock.writeLock();

        @Override
        public void reset() {
            Map<Key<?>, Object> map = new HashMap<Key<?>, Object>();
            try {
                w.lock();
                if (hasJSR250Module) {
                    map.putAll(singletons);
                }
                singletons.clear();
            } finally {
                w.unlock();
            }
            for (Map.Entry<Key<?>, Object> entry : map.entrySet())
                Jsr250.preDestroy(entry.getValue());
        }

        @PreDestroy
        public void shutdown() {
            singletons.clear();
        }

        @Override
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {
                public T get() {
                    Object t;
                    try {
                        r.lock();
                        t = singletons.get(key);
                    } finally {
                        r.unlock();
                    }
                    if (t == null) {
                        try {
                            w.lock();
                            t = creator.get();
                            t = t == null ? NULL : t;
                            singletons.put(key, t);
                        } finally {
                            w.unlock();
                        }
                    }
                    return t == NULL ? null : (T) t;
                }

                @Override
                public String toString() {
                    return String.format("%s[%s]", creator, ResetSingleton.this);
                }
            };
        }
    }

    private static abstract class RefScope extends MycilaScope {
        @Override
        public final <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {
                private volatile Reference<T> ref;

                @Override
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

                @Override
                public String toString() {
                    return String.format("%s[%s]", creator, RefScope.this);
                }
            };
        }

        protected abstract <T> Reference<T> build(T instance);
    }

    private static abstract class MycilaScope implements Scope {
        boolean hasJSR250Module;

        @Inject
        private void init(final Injector injector) {
            hasJSR250Module = Jsr250.hasJSR250Module(injector);
        }

        @Override
        public final String toString() {
            return ExtraScopes.class.getSimpleName() + "." + getClass().getSimpleName();
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
        private final Key<T> key;

        private FutureProvider(Key<T> key, final Provider<T> unscoped) {
            super(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return unscoped.get();
                }
            });
            this.key = key;
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

        @Override
        public String toString() {
            return "FutureProvider[" + key + "]";
        }
    }

    private static final class DefaultThreadFactory implements ThreadFactory {
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

}
