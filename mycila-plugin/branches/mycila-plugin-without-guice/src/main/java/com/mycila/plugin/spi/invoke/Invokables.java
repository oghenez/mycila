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

package com.mycila.plugin.spi.invoke;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Invokables {

    private static final boolean hasCGLIB;

    static {
        hasCGLIB = hasCGLIB(Invokables.class.getClassLoader());
    }

    private static boolean hasCGLIB(ClassLoader classLoader) {
        try {
            classLoader.loadClass("net.sf.cglib.proxy.Callback");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private Invokables() {
    }

    public static <T> InvokableMember<T> get(Constructor<T> ctor) {
        return hasCGLIB ? new InvokableFastCtor<T>(ctor) : new InvokableCtor<T>(ctor);
    }

    public static <T> InvokableMember<T> get(Method method, Object target) {
        return hasCGLIB ? new InvokableFastMethod<T>(target, method) : new InvokableMethod<T>(target, method);
    }

    public static <T> InvokableMember<T> get(Field field, Object target) {
        return new InvokableField<T>(target, field);
    }

    public static <T> InvokableComposite<T> composite() {
        return new InvokableCompositeImpl<T>();
    }

}
