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

package com.mycila.guice;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.mycila.guice.annotation.ConcurrentSingleton;
import com.mycila.guice.annotation.ExpiringSingleton;
import com.mycila.guice.annotation.Expirity;
import com.mycila.guice.annotation.RenewableSingleton;
import com.mycila.guice.annotation.SoftSingleton;
import com.mycila.guice.annotation.WeakSingleton;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum ExtraScope implements Provider<Scope> {

    EXPIRING_SINGLETON(ExpiringSingleton.class) {
        @Override
        public Scope get() {
            return new Scope() {
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

                @Override
                public String toString() {
                    return ExtraScope.class.getSimpleName() + ".EXPIRING_SINGLETON";
                }
            };
        }
    },

    RENEWABLE_SINGLETON(RenewableSingleton.class) {
        @Override
        public Scope get() {
            return new Scope() {
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

                @Override
                public String toString() {
                    return ExtraScope.class.getSimpleName() + ".RENEWABLE_SINGLETON";
                }
            };
        }
    },

    WEAK_SINGLETON(WeakSingleton.class) {
        @Override
        public Scope get() {
            return new RefScope("WEAK_SINGLETON") {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    SOFT_SINGLETON(SoftSingleton.class) {
        @Override
        public Scope get() {
            return new RefScope("SOFT_SINGLETON") {
                @Override
                protected <T> Reference<T> build(T instance) {
                    return new WeakReference<T>(instance);
                }
            };
        }
    },

    CONCURRENT_SINGLETON(ConcurrentSingleton.class) {
        @Override
        public Scope get() {
            return new Scope() {
                private final BlockingQueue<Key<?>> executionQueue = new LinkedBlockingQueue<Key<?>>();

                @Inject
                void init(final Injector injector) {
                    EXECUTOR.execute(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                try {
                                    Key<?> key = executionQueue.take();
                                    final Binding<?> binding = injector.getExistingBinding(key);
                                    if (binding == null) {
                                        System.out.println("=> take " + key + "\n=> b : " + binding);
                                        Thread.sleep(100);
                                        executionQueue.add(key);
                                    } else {
                                        EXECUTOR.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.getProvider().get();
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    });

                    /*final Scope thisScope = this;
                    for (final Binding<?> binding : injector.getBindings().values()) {
                        binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Void>() {
                            @Override
                            public Void visitScope(Scope scope) {
                                if (scope == thisScope)
                                    EXECUTOR.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.getProvider().get();
                                        }
                                    });
                                return null;
                            }
                        });
                    }*/
                }

                @Override
                public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
                    return new FutureProvider<T>(key, unscoped, executionQueue);
                }

                @Override
                public String toString() {
                    return ExtraScope.class.getSimpleName() + ".CONCURRENT_SINGLETON";
                }
            };
        }
    };

    private final Class<? extends Annotation> defaultAnnotation;

    ExtraScope(Class<? extends Annotation> defaultAnnotation) {
        this.defaultAnnotation = defaultAnnotation;
    }

    public Class<? extends Annotation> defaultAnnotation() {
        return defaultAnnotation;
    }

    @Override
    public String toString() {
        return ExtraScope.class.getSimpleName() + '.' + name();
    }

    public static Expirity expirity(long value) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        properties.put("value", value);
        return AnnotationMetadata.buildAnnotation(Expirity.class, properties);
    }

    public static void install(Binder binder) {
        for (ExtraScope mycilaScope : ExtraScope.values()) {
            Scope scope = mycilaScope.get();
            binder.requestInjection(scope);
            binder.bindScope(mycilaScope.defaultAnnotation(), scope);
        }
    }

    private static final Object NULL = new Object();

    static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            0, Runtime.getRuntime().availableProcessors() * 2,
            10, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new DefaultThreadFactory("@ConcurrentSingleton-Thread-"),
            new ThreadPoolExecutor.CallerRunsPolicy());

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

    private static abstract class RefScope implements Scope {
        private final String name;

        RefScope(String name) {
            this.name = name;
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

        @Override
        public String toString() {
            return ExtraScope.class.getSimpleName() + '.' + name;
        }
    }

}
