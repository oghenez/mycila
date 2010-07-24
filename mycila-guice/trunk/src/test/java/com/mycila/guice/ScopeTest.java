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
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.mycila.guice.annotation.ExpiringSingleton;
import com.mycila.guice.annotation.Expirity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ScopeTest {

    @Test
    public void test_weak() throws Exception {
        Provider<Object> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        Provider<Object> provider = Scopes.WEAK_SINGLETON.scope(null, unscoped);

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
        Provider<Object> provider = Scopes.EXPIRING_SINGLETON.scope(Key.get(Object.class, annot), unscoped);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }

    @Test
    public void test_with_injector() throws Exception {
        Expirity expirity1 = Scopes.expirity(1);
        Expirity expirity2 = Scopes.expirity(1);
        assertEquals(expirity1, expirity2);
        assertEquals(expirity1.hashCode(), expirity2.hashCode());

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                MycilaBinder mycilaGuice = MycilaBinder.on(binder());
                mycilaGuice.bindScopes();
                bind(Object.class).annotatedWith(Scopes.expirity(500)).toProvider(new Provider<Object>() {
                    @Override
                    public Object get() {
                        return new Object();
                    }
                }).in(ExpiringSingleton.class);
            }
        });

        Object o1 = injector.getInstance(Key.get(Object.class, Scopes.expirity(500)));
        assertNotNull(o1);
        Object o2 = injector.getInstance(Key.get(Object.class, Scopes.expirity(500)));
        assertNotNull(o2);
        assertSame(o1, o2);
        Thread.sleep(600);
        assertNotSame(o1, injector.getInstance(Key.get(Object.class, Scopes.expirity(500))));
    }

}