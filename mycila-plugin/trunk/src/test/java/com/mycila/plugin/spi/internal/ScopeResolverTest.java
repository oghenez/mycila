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

package com.mycila.plugin.spi.internal;

import com.mycila.plugin.annotation.scope.None;
import com.mycila.plugin.annotation.scope.Singleton;
import com.mycila.plugin.spi.ScopeBinding;
import com.mycila.plugin.spi.Scopes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ScopeResolverTest {

    @Test
    public void test() throws Exception {
        ScopeResolver resolver = new ScopeResolver(Scopes.DEFAULT);

        ScopeBinding scopeBinding = resolver.getScopeBinding(getClass().getMethod("method1"));
        assertEquals(Scopes.DEFAULT, scopeBinding.getAnnotation());
        assertTrue(Scopes.None.class.isInstance(scopeBinding.getScope()));

        scopeBinding = resolver.getScopeBinding(getClass().getMethod("method2"));
        assertTrue(Singleton.class.isInstance(scopeBinding.getAnnotation()));
        assertTrue(Scopes.Singleton.class.isInstance(scopeBinding.getScope()));

        try {
            resolver.getScopeBinding(getClass().getMethod("method3"));
            fail();
        } catch (Exception e) {
            assertTrue(TooManyScopeException.class.isInstance(e));
        }
    }

    @None
    public void method1() {
    }

    @Singleton
    public void method2() {
    }

    @Singleton
    @None
    public void method3() {
    }
}
