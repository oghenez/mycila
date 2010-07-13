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
import com.mycila.plugin.aop.Invokable;
import com.mycila.plugin.aop.Invokables;
import com.mycila.plugin.aop.InvokeException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ScopeProviders {

    private static final ConcurrentMap<Class<?>, Invokable<? extends ScopeProvider>> CTORS = new ConcurrentHashMap<Class<?>, Invokable<? extends ScopeProvider>>();

    private ScopeProviders() {

    }

    public static <T> ScopeProvider<T> build(Class<? extends ScopeProvider> scope) throws ScopeInstanciationException {
        try {
            Invokable<? extends ScopeProvider> ctor = CTORS.get(scope);
            if (ctor == null)
                CTORS.putIfAbsent(scope, ctor = Invokables.get(scope.getConstructor()));
            return ctor.invoke();
        } catch (InvokeException e) {
            throw new ScopeInstanciationException(scope, e.getCause());
        } catch (NoSuchMethodException e) {
            throw new ScopeInstanciationException(scope, e);
        }
    }

    public static <T> Provider<T> build(Class<? extends ScopeProvider> scope, ScopeContext context) throws ScopeInstanciationException {
        ScopeProvider<T> t = build(scope);
        t.init(context);
        return t;
    }
}
