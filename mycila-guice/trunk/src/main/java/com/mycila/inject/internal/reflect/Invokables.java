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

package com.mycila.inject.internal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Invokables {

    private Invokables() {
    }

    public static <T> InvokableMember<T> get(Constructor<T> ctor) {
        return new InvokableFastCtor<T>(ctor);
    }

    public static <T> InvokableMember<T> get(Method method) {
        return get(null, method);
    }

    public static <T> InvokableMember<T> get(Object target, Method method) {
        return new InvokableFastMethod<T>(target, method);
    }

    public static <T> InvokableMember<T> get(Field field) {
        return get(null, field);
    }

    public static <T> InvokableMember<T> get(Object target, Field field) {
        return new InvokableField<T>(target, field);
    }

    public static <T> Invokable<T> compose(final Invokable<T>... invokables) {
        return new Invokable<T>() {
            @Override
            public T invoke(Object... args) throws InvokeException {
                T res = null;
                for (Invokable<? extends T> invokable : invokables)
                    res = invokable.invoke(args);
                return res;
            }
        };
    }

}
