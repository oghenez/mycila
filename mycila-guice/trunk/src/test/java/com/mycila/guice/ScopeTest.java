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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Stage;
import com.mycila.guice.annotation.ConcurrentSingleton;
import com.mycila.guice.annotation.Expirity;
import com.mycila.guice.annotation.RenewableSingleton;
import com.mycila.guice.annotation.SoftSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ScopeTest {

    public static void main(String... args) {
        long start = System.nanoTime();
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new AbstractModule() {
            public void configure() {
                ExtraScope.installAll(binder());
                bind(C.class);
                bind(D.class);
            }
        });
        injector.getInstance(A.class);
        long elapsed = System.nanoTime() - start;
        System.out.printf("Completed in %d seconds%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
        assertTrue(TimeUnit.NANOSECONDS.toMillis(elapsed) < 5000);
    }

    @Test
    public void test_threads_stopping_if_injector_garbadgedcollected() throws Exception {
        System.out.println("create inj.");
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new AbstractModule() {
            public void configure() {
                ExtraScope.installAll(binder());
                bind(Solo.class);
            }
        });
        System.out.println("get Solo");
        injector.getInstance(Solo.class);
        injector = null;
        System.out.println("gc");
        System.gc();
        System.gc();
        System.out.println("wait...");
        Thread.sleep(5000);
    }

    @Test
    public void test_concurrent() throws Exception {
        long start = System.nanoTime();
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new AbstractModule() {
            public void configure() {
                ExtraScope.installAll(binder());
                bind(C.class);
                bind(D.class);
            }
        });
        injector.getInstance(A.class);
        long elapsed = System.nanoTime() - start;
        System.out.printf("Completed in %d seconds%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
        assertTrue(TimeUnit.NANOSECONDS.toMillis(elapsed) < 5000);
    }

    @Test
    public void test_eq() throws Exception {
        assertEquals(ExtraScope.EXPIRING_SINGLETON.get(), ExtraScope.EXPIRING_SINGLETON.get());
        assertEquals(ExtraScope.EXPIRING_SINGLETON.get().hashCode(), ExtraScope.EXPIRING_SINGLETON.get().hashCode());

        assertEquals(ExtraScope.EXPIRING_SINGLETON.get(SoftSingleton.class), ExtraScope.EXPIRING_SINGLETON.get(SoftSingleton.class));
        assertEquals(ExtraScope.EXPIRING_SINGLETON.get(SoftSingleton.class).hashCode(), ExtraScope.EXPIRING_SINGLETON.get(SoftSingleton.class).hashCode());
    }

    @Test
    public void test_expire() throws Exception {
        Provider<Object> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });
        Expirity annot = mock(Expirity.class);
        when(annot.value()).thenReturn(500l);
        doReturn(Expirity.class).when(annot).annotationType();
        Provider<Object> provider = ExtraScope.EXPIRING_SINGLETON.get().scope(Key.get(Object.class, annot), unscoped);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNull(provider.get());
    }

    @Test
    public void test_weak() throws Exception {
        Provider<Object> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        Provider<Object> provider = ExtraScope.WEAK_SINGLETON.get().scope(null, unscoped);

        Object o = provider.get();
        int hash = o.hashCode();
        assertNotNull(o);

        System.gc();
        System.gc();

        assertSame(o, provider.get());

        o = provider.get();
        System.gc();
        System.gc();

        assertNotSame(hash, o.hashCode());
    }

    @Test
    public void test_renewable() throws Exception {
        Provider<Object> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });
        Expirity annot = mock(Expirity.class);
        when(annot.value()).thenReturn(500l);
        doReturn(Expirity.class).when(annot).annotationType();
        Provider<Object> provider = ExtraScope.RENEWABLE_SINGLETON.get().scope(Key.get(Object.class, annot), unscoped);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }

    @Test
    public void test_with_injector() throws Exception {
        Expirity expirity1 = ExtraScope.expirity(1);
        Expirity expirity2 = ExtraScope.expirity(1);
        assertEquals(expirity1, expirity2);
        assertEquals(expirity1.hashCode(), expirity2.hashCode());

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ExtraScope.installAll(binder());
                bind(Object.class).annotatedWith(ExtraScope.expirity(500)).toProvider(new Provider<Object>() {
                    @Override
                    public Object get() {
                        return new Object();
                    }
                }).in(RenewableSingleton.class);
            }
        });

        Object o1 = injector.getInstance(Key.get(Object.class, ExtraScope.expirity(500)));
        assertNotNull(o1);
        Object o2 = injector.getInstance(Key.get(Object.class, ExtraScope.expirity(500)));
        assertNotNull(o2);
        assertSame(o1, o2);
        Thread.sleep(600);
        assertNotSame(o1, injector.getInstance(Key.get(Object.class, ExtraScope.expirity(500))));
    }

    @ConcurrentSingleton
    static class A {
        @Inject
        public A(Provider<B> b, Provider<C> c) {
            try {
                System.out.printf("Starting A on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.printf("A getting B and C instances on thread %s%n", Thread.currentThread().getName());
                b.get();
                c.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started A");
            }
        }
    }

    @ConcurrentSingleton
    static class B {
        @Inject
        public B() {
            try {
                System.out.printf("Starting B on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started B");
            }
        }
    }

    @ConcurrentSingleton
    static class C {
        @Inject
        public C(Provider<D> d) {
            try {
                System.out.printf("Starting C on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
                System.out.printf("C getting D instance on thread %s%n", Thread.currentThread().getName());
                d.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started C");
            }
        }
    }

    @ConcurrentSingleton
    static class D {
        @Inject
        public D() {
            try {
                System.out.printf("Starting D on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started D");
            }
        }
    }

    @ConcurrentSingleton
    static class Solo {
        @Inject
        public Solo() {
            try {
                System.out.printf("Starting Solo on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started Solo");
            }
        }
    }

}