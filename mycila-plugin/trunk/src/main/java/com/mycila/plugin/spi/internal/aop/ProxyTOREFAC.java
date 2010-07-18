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

package com.mycila.plugin.spi.internal.aop;

import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ProxyTOREFAC {

    private ProxyTOREFAC() {
    }

    private static final NamingPolicy NAMING_POLICY = new NamingPolicy() {
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix != null ?
                    prefix.startsWith("java") ?
                            "$" + prefix :
                            prefix :
                    "net.sf.cglib.empty.Object");
            sb.append("$$");
            sb.append(source.substring(source.lastIndexOf('.') + 1));
            sb.append("ByMycilaEvent$$");
            sb.append(Integer.toHexString(key.hashCode()));
            String base = sb.toString();
            String attempt = base;
            int index = 2;
            while (names.evaluate(attempt))
                attempt = base + "_" + index++;
            return attempt;
        }
    };

    static <T> T proxy(Class<T> c, MethodInterceptor interceptor) {
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

    static boolean isMycilaProxy(Class<?> c) {
        return ProxyMarker.class.isAssignableFrom(c);
    }

    public static interface ProxyMarker {
    }
}