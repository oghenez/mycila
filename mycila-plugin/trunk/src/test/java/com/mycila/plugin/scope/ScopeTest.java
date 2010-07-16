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

package com.mycila.plugin.scope;

import com.mycila.plugin.Provider;
import com.mycila.plugin.annotation.scope.ExpiringSingleton;
import com.mycila.plugin.spi.Scopes;
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
    public void test_none() throws Exception {
        Provider<String> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        Provider<String> provider = new Scopes.None().getProvider(null, unscoped);

        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
    }

    @Test
    public void test_singleton() throws Exception {
        Provider<String> unscoped = mock(Provider.class);
        when(unscoped.get()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        Provider<String> provider = new Scopes.Singleton().getProvider(null, unscoped);

        assertEquals("0", provider.get());
        assertEquals("0", provider.get());
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

        Provider<Object> provider = new Scopes.WeakSingleton().getProvider(null, unscoped);

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

        ExpiringSingleton annot = mock(ExpiringSingleton.class);
        when(annot.value()).thenReturn(500l);
        Provider<Object> provider = new Scopes.ExpiringSingleton().getProvider(annot, unscoped);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }

}
