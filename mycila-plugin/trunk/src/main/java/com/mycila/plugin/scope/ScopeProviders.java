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
import com.mycila.plugin.util.ClassUtils;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ScopeProviders {

    private static final ConcurrentMap<Class<?>, FastConstructor> CTORS = new ConcurrentHashMap<Class<?>, FastConstructor>();

    private ScopeProviders() {

    }

    public static <T> ScopeProvider<T> build(Class<? extends ScopeProvider> scope) throws ScopeInstanciationException {
        FastConstructor ctor = CTORS.get(scope);
        try {
            if (ctor == null) {
                FastClass fastClass = FastClass.create(ClassUtils.getDefaultClassLoader(), scope);
                CTORS.putIfAbsent(scope, ctor = fastClass.getConstructor(new Class[0]));
            }
            return (ScopeProvider<T>) ctor.newInstance();
        } catch (NoSuchMethodError e) {
            throw new ScopeInstanciationException(scope, e);
        } catch (InvocationTargetException e) {
            throw new ScopeInstanciationException(scope, e.getTargetException());
        }
    }

    public static <T> Provider<T> build(Class<? extends ScopeProvider> scope, ScopeContext<T> context) throws ScopeInstanciationException {
        ScopeProvider<T> t = build(scope);
        t.init(context);
        return t;
    }
}
