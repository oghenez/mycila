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

package com.mycila.plugin.invoke;

import net.sf.cglib.reflect.FastConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableFastCtor<T> implements InvokableMember<T> {

    private final FastConstructor ctor;

    InvokableFastCtor(Constructor<T> ctor) {
        this.ctor = CglibCache.getFastClass(ctor.getDeclaringClass()).getConstructor(ctor);
    }

    @Override
    public Member getMember() {
        return ctor.getJavaConstructor();
    }

    @Override
    public Class<T> getType() {
        return ctor.getJavaConstructor().getDeclaringClass();
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