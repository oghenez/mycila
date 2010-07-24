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

package com.mycila.guice.tmp;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.spi.DefaultBindingScopingVisitor;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * A special form of singleton scope that eagerly starts building its
 * singletons concurrently in background using a thread pool at injector
 * creation.
 */
public class ConcurrentSingletonScope implements Scope {

    /**
     * Install this scope given a Binder. Call only once per injector.
     */
    public static void install(Binder binder) {
        Scope scope = new ConcurrentSingletonScope();
        binder.bindScope(ConcurrentSingleton.class, scope);
        binder.requestInjection(scope);
    }


    // Scope method

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {

        // Memoize call to unscoped.get() under key.

        return new Provider<T>() {
            public T get() {
                ConcurrentMap<Key<T>, Future<T>> futures = futures();
                Future<T> f = futures.get(key);
                if (f == null) {
                    FutureTask<T> creator = new FutureTask<T>(new Callable<T>() {
                        public T call() {
                            return unscoped.get();
                        }
                    });
                    f = futures.putIfAbsent(key, creator);
                    if (f == null) {
                        f = creator;
                        creator.run();
                    }
                }
                try {
                    return f.get();
                } catch (ExecutionException e) {
                    throw (RuntimeException) e.getCause();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ProvisionException("interrupted during provision");
                }
            }
        };
    }

    //
    // Implementation
    //

    private ConcurrentSingletonScope() {
        // Prevent instantiation by anyone else.
    }

    @Inject private void initializeBindingsInThisScope(Injector injector) {
        final Scope thisScope = this;
        final ExecutorService pool = Executors.newCachedThreadPool();
        for (final Binding<?> binding : injector.getBindings().values()) {
            binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Void>() {
                @Override public Void visitScope(Scope scope) {
                    if (scope == thisScope) {
                        pool.execute(new Runnable() {
                            public void run() {
                                binding.getProvider().get();
                            }
                        });
                    }
                    return null;
                }
            });
        }
        pool.shutdown();
    }

    @SuppressWarnings("unchecked")
    <T> ConcurrentMap<Key<T>, Future<T>> futures() {
        return (ConcurrentMap<Key<T>, Future<T>>) futures;
    }

    private final ConcurrentMap futures = new ConcurrentHashMap();
}
