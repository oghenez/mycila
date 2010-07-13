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

package com.mycila.plugin.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableMethod<T> implements InvokableMember<T> {

    private final Method method;
    private final Object target;

    InvokableMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public Member getMember() {
        return method;
    }

    @Override
    public Class<T> getType() {
        return (Class<T>) method.getReturnType();
    }

    @Override
    public String toString() {
        return method.toString();
    }

    @Override
    public T invoke(Object... args) throws InvokeException {
        try {
            return (T) method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw new InvokeException(this, e.getTargetException());
        } catch (Exception e) {
            throw new InvokeException(this, e);
        }
    }

}