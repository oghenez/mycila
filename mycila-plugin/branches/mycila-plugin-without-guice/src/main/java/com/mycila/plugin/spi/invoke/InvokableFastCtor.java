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
import com.mycila.plugin.spi.aop.CglibUtils;
import com.mycila.plugin.spi.model.TypeLiteral;
import net.sf.cglib.reflect.FastConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableFastCtor<T> implements InvokableMember<T> {

    private final FastConstructor ctor;

    InvokableFastCtor(Constructor<T> ctor) {
        this.ctor = CglibUtils.getFastClass(ctor.getDeclaringClass()).getConstructor(ctor);
    }

    @Override
    public AnnotatedMember<Constructor<T>> getMember() {
        return new AnnotatedMember<Constructor<T>>(ctor.getJavaConstructor());
    }

    @Override
    public TypeLiteral<T> getType() {
        return TypeLiteral.<T>get(ctor.getJavaConstructor().getDeclaringClass());
    }

    @Override
    public String toString() {
        return ctor.getJavaConstructor().toString();
    }

    @Override
    public T invoke(Object... args) throws InvokeException {
        try {
            return (T) ctor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new InvokeException(this, e.getTargetException());
        } catch (Exception e) {
            throw new InvokeException(this, e);
        }
    }

}