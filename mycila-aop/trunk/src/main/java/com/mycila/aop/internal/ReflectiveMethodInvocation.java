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

package com.mycila.aop.internal;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class ReflectiveMethodInvocation implements ProxyMethodInvocation {

    private final Object target;
    private final Object proxy;
    private final Method method;
    private final Object[] arguments;
    private final List<MethodInterceptor> chain;
    private int next = -1;

    ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, List<MethodInterceptor> chain) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.chain = chain;
    }

    @Override
    public final Method getMethod() {
        return method;
    }

    @Override
    public final Object[] getArguments() {
        return arguments;
    }

    @Override
    public final Object proceed() throws Throwable {
        return next == chain.size() - 1 ? invokeJoinPoint() : chain.get(++next).invoke(this);
    }

    @Override
    public final Object getThis() {
        return target;
    }

    @Override
    public final AccessibleObject getStaticPart() {
        return method;
    }

    @Override
    public final Object getProxy() {
        return proxy;
    }

    Object invokeJoinPoint() throws Throwable {
        return method.invoke(target, arguments);
    }
}