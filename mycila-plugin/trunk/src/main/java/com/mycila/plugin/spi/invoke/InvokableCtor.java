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

import com.mycila.plugin.InvokeException;
import com.mycila.plugin.spi.model.TypeLiteral;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableCtor<T> implements InvokableMember<T> {

    private final Constructor<T> ctor;

    InvokableCtor(Constructor<T> ctor) {
        this.ctor = ctor;
    }

    @Override
    public AnnotatedMember<Constructor<T>> getMember() {
        return new AnnotatedMember<Constructor<T>>(ctor);
    }

    @Override
    public TypeLiteral<T> getType() {
        return TypeLiteral.get(ctor.getDeclaringClass());
    }

    @Override
    public String toString() {
        return ctor.toString();
    }

    @Override
    public T invoke(Object... args) throws InvokeException {
        try {
            if (!ctor.isAccessible())
                ctor.setAccessible(true);
            return ctor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new InvokeException(this, e.getTargetException());
        } catch (Exception e) {
            throw new InvokeException(this, e);
        }
    }

}