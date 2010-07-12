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

package com.mycila.plugin.metadata;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.InvokeException;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InvokableMethod implements Invokable {

    private final FastMethod method;
    private final Object target;

    private InvokableMethod(Object target, FastMethod method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public Object invoke(Object... args) throws InvokeException {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw new InvokeException(method.getJavaMethod(), e.getTargetException());
        } catch (Exception e) {
            throw new InvokeException(method.getJavaMethod(), e);
        }
    }

    @Override
    public String toString() {
        return "InvokableMethod " + method.getJavaMethod();
    }

    public static InvokableMethod create(Object target, FastMethod method) {
        return new InvokableMethod(target, method);
    }
}
