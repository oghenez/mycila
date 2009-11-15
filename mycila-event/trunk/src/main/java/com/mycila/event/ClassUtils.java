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

package com.mycila.event;

import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ClassUtils {

    private static final NamingPolicy NAMING_POLICY = new NamingPolicy() {
        @Override
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

    private ClassUtils() {
    }

    static Iterable<Method> filterAnnotatedMethods(final Iterable<Method> iterable, final Class<? extends Annotation>... allowedAnnotations) {
        return new Iterable<Method>() {
            @Override
            public Iterator<Method> iterator() {
                final Iterator<Method> methodIterator = iterable.iterator();
                return new FilterIterator<Method, Method>(methodIterator) {
                    @Override
                    protected Method filter(Method delegate) {
                        for (Class<? extends Annotation> annotation : allowedAnnotations)
                            if (delegate.isAnnotationPresent(annotation))
                                return delegate;
                        return null;
                    }
                };
            }
        };
    }

    static Iterable<Method> getAllDeclaredMethods(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<Class<?>>();
        while (clazz != null && clazz != Object.class) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        LinkedHashMap<MethodSignature, Method> all = new LinkedHashMap<MethodSignature, Method>();
        for (Class<?> c : hierarchy) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                MethodSignature signature = MethodSignature.of(method);
                if (!all.containsKey(signature))
                    all.put(signature, method);
            }
        }
        return new LinkedList<Method>(all.values());
    }

    static FastClass fast(Class<?> type) {
        FastClass.Generator generator = new FastClass.Generator();
        generator.setType(type);
        generator.setNamingPolicy(NAMING_POLICY);
        return generator.create();
    }

    static FastMethod fast(Method m) {
        return fast(m.getDeclaringClass()).getMethod(m);
    }

    @SuppressWarnings({"unchecked"})
    static <T> T createJDKProxy(Class<T> c, MethodInterceptor interceptor) {
        return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, toJDK(interceptor));
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T createCglibProxy(Class<T> c, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setStrategy(new DefaultGeneratorStrategy());
        enhancer.setSuperclass(c);
        enhancer.setNamingPolicy(NAMING_POLICY);
        enhancer.setCallback(toCGLIB(interceptor));
        return (T) enhancer.create();
    }

    static InvocationHandler toJDK(final MethodInterceptor interceptor) {
        return new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                return interceptor.invoke(new MethodInvocation() {
                    @Override
                    public Method getMethod() {
                        return method;
                    }

                    @Override
                    public Object[] getArguments() {
                        return args;
                    }

                    @Override
                    public Object proceed() throws Throwable {
                        return method.invoke(proxy, args);
                    }

                    @Override
                    public Object getThis() {
                        return proxy;
                    }

                    @Override
                    public AccessibleObject getStaticPart() {
                        return method;
                    }
                });
            }
        };
    }

    static net.sf.cglib.proxy.MethodInterceptor toCGLIB(final MethodInterceptor interceptor) {
        return new net.sf.cglib.proxy.MethodInterceptor() {
            @Override
            public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
                return interceptor.invoke(new MethodInvocation() {

                    public Method getMethod() {
                        return method;
                    }

                    public Object[] getArguments() {
                        return args;
                    }

                    public Object proceed() throws Throwable {
                        return proxy.invoke(obj, args);
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

}
