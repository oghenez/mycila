/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.internal;

import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Proxy {

    private Proxy() {
    }

    private static final WeakCache<Method, MethodInvoker> INVOKER_CACHE = new WeakCache<Method, MethodInvoker>(new WeakCache.Provider<Method, MethodInvoker>() {
        @Override
        public MethodInvoker get(final Method method) {
            int modifiers = method.getModifiers();
            if (!Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers)) {
                try {
                    final net.sf.cglib.reflect.FastMethod fastMethod = BytecodeGen.newFastClass(method.getDeclaringClass(), BytecodeGen.Visibility.forMember(method)).getMethod(method);
                    return new MethodInvoker() {
                        public Object invoke(Object target, Object... parameters) throws IllegalAccessException, InvocationTargetException {
                            return fastMethod.invoke(target, parameters);
                        }
                    };
                } catch (net.sf.cglib.core.CodeGenerationException e) {/* fall-through */}
            }
            if (!Modifier.isPublic(modifiers) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
                method.setAccessible(true);
            }
            return new MethodInvoker() {
                public Object invoke(Object target, Object... parameters) throws IllegalAccessException, InvocationTargetException {
                    return method.invoke(target, parameters);
                }
            };
        }
    });

    public static MethodInvoker invoker(final Method method) {
        return INVOKER_CACHE.get(method);
    }

    public static <T> T proxy(Class<T> c, MethodInterceptor interceptor) {
        return c.isInterface() ?
                createJDKProxy(c, interceptor) :
                createCglibProxy(c, interceptor);
    }

    @SuppressWarnings({"unchecked"})
    static <T> T createJDKProxy(Class<T> c, MethodInterceptor interceptor) {
        List<Class<?>> interfaces = new LinkedList<Class<?>>();
        interfaces.add(ProxyMarker.class);
        interfaces.add(c);
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                c.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                toJDK(interceptor));
    }

    @SuppressWarnings({"unchecked"})
    static <T> T createCglibProxy(Class<T> c, MethodInterceptor interceptor) {
        BytecodeGen.newEnhancer()

        Enhancer enhancer = new Enhancer();
        enhancer.setStrategy(new DefaultGeneratorStrategy());
        List<Class<?>> interfaces = new LinkedList<Class<?>>();
        interfaces.add(ProxyMarker.class);
        if (c.isInterface()) interfaces.add(c);
        else enhancer.setSuperclass(c);
        enhancer.setInterfaces(interfaces.toArray(new Class[interfaces.size()]));
        enhancer.setNamingPolicy(NAMING_POLICY);
        enhancer.setCallback(toCGLIB(interceptor));
        enhancer.setUseFactory(true);
        return (T) enhancer.create();
    }

    static InvocationHandler toJDK(final MethodInterceptor interceptor) {
        return new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, Object[] args) throws Throwable {
                final Object[] arguments = args == null ? new Object[0] : args;
                return interceptor.invoke(new MethodInvocation() {
                    public Method getMethod() {
                        return method;
                    }

                    public Object[] getArguments() {
                        return arguments;
                    }

                    public Object proceed() throws Throwable {
                        return method.invoke(proxy, arguments);
                    }

                    public Object getThis() {
                        return proxy;
                    }

                    public AccessibleObject getStaticPart() {
                        return method;
                    }
                });
            }
        };
    }

    static net.sf.cglib.proxy.MethodInterceptor toCGLIB(final MethodInterceptor interceptor) {
        return new net.sf.cglib.proxy.MethodInterceptor() {
            public Object intercept(final Object obj, final Method method, Object[] args, final MethodProxy proxy) throws Throwable {
                final Object[] arguments = args == null ? new Object[0] : args;
                return interceptor.invoke(new MethodInvocation() {
                    public Method getMethod() {
                        return method;
                    }

                    public Object[] getArguments() {
                        return arguments;
                    }

                    public Object proceed() throws Throwable {
                        return proxy.invokeSuper(obj, arguments);
                    }

                    public Object getThis() {
                        return obj;
                    }

                    public AccessibleObject getStaticPart() {
                        return method;
                    }
                });
            }
        };
    }

    static boolean isProxy(Class<?> c) {
        return c != null
                && !c.isInterface()
                && !c.equals(Object.class)
                && (java.lang.reflect.Proxy.isProxyClass(c)
                || Factory.class.isAssignableFrom(c)
                || net.sf.cglib.proxy.Proxy.isProxyClass(c)
                || isMycilaProxy(c));
    }

    public static boolean isMycilaProxy(Class<?> c) {
        return ProxyMarker.class.isAssignableFrom(c);
    }

    public static interface ProxyMarker {
    }
}
