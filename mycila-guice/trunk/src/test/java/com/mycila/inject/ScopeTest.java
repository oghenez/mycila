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

package com.mycila.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Jsr330;
import com.mycila.inject.scope.RenewableSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Provider;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ScopeTest {

    @Test
    public void test_expire() throws Exception {
        Provider<Object> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });
        Provider<Object> provider = ExtraScopes.expiringSingleton(500, TimeUnit.MILLISECONDS).scope(Key.get(Object.class), Jsr330.guicify(unscoped));

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

        Provider<Object> provider = ExtraScopes.weakSingleton().scope(null, Jsr330.guicify(unscoped));

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
        Provider<Object> provider = ExtraScopes.renewableSingleton(500, TimeUnit.MILLISECONDS).scope(Key.get(Object.class), Jsr330.guicify(unscoped));

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }

    @Test
    public void test_with_injector() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Object.class).toProvider(Jsr330.guicify(new Provider<Object>() {
                    @Override
                    public Object get() {
                        return new Object();
                    }
                })).in(RenewableSingleton.class);
            }
        });

        Object o1 = injector.getInstance(Key.get(Object.class));
        assertNotNull(o1);
        Object o2 = injector.getInstance(Key.get(Object.class));
        assertNotNull(o2);
        assertSame(o1, o2);
        Thread.sleep(600);
        assertNotSame(o1, injector.getInstance(Key.get(Object.class)));
    }

}