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

package com.mycila.event.spi;

import com.mycila.event.api.AbstractAnnotationProcessor;
import com.mycila.event.api.ClassUtils;
import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.Publisher;
import com.mycila.event.api.Topics;
import com.mycila.event.api.annotation.Multiple;
import com.mycila.event.api.annotation.Publish;
import com.mycila.event.api.annotation.Subscribe;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AnnotationProcessors extends AbstractAnnotationProcessor {

    private AnnotationProcessors(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static AnnotationProcessors create(final Dispatcher dispatcher) {
        return new AnnotationProcessors(dispatcher) {
            @SuppressWarnings({"unchecked"})
            @Override
            protected void processAnnotatedMethod(Dispatcher dispatcher, Object instance, Method method) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe != null)
                    dispatcher.subscribe(Topics.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
                Publish annotation = method.getAnnotation(Publish.class);
                if (annotation != null) {
                    hasOneArg(Publisher.class, method);
                    Publisher publisher = Publishers.create(dispatcher, Topics.topics(annotation.topics()));
                    method.setAccessible(true);
                    try {
                        method.invoke(instance, publisher);
                    } catch (Exception e) {
                        ExceptionUtils.reThrow(e);
                    }
                }
            }

            @Override
            protected <T> T proxyInterface(Dispatcher dispatcher, Class<T> clazz) {
                return Proxy.createJDKProxy(clazz, new PublisherInterceptor(dispatcher, clazz));
            }

            @Override
            protected <T> T proxyClass(Dispatcher dispatcher, Class<T> clazz) {
                return Proxy.createCglibProxy(clazz, new PublisherInterceptor(dispatcher, clazz));
            }
        };
    }

    private static final class PublisherInterceptor implements MethodInterceptor {
        final Map<Method, Publisher<Object>> publisherCache = new HashMap<Method, Publisher<Object>>();
        final Set<Method> requiresSplit = new HashSet<Method>();
        final Object delegate;

        PublisherInterceptor(Dispatcher dispatcher, Class<?> c) {
            Iterable<Method> allMethods = ClassUtils.getAllDeclaredMethods(c);
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Publish.class)) {
                hasSomeArgs(method);
                Publish annotation = method.getAnnotation(Publish.class);
                Publisher<Object> publisher = Publishers.create(dispatcher, Topics.topics(annotation.topics()));
                publisherCache.put(method, publisher);
                if (method.isAnnotationPresent(Multiple.class))
                    requiresSplit.add(method);
            }
            delegate = new Object() {
                @Override
                public String toString() {
                    return "MycilaEvent Generated Publisher";
                }
            };
        }

        @SuppressWarnings({"unchecked"})
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object o = publisherCache.get(invocation.getMethod());
            if (o == null) {
                try {
                    return invocation.getMethod().invoke(delegate, invocation.getArguments());
                } catch (Exception e) {
                    ExceptionUtils.reThrow(e);
                }
                throw new AssertionError("BUG - SHOULD NOT GO HERE");
            }
            Publisher<Object> publisher = (Publisher<Object>) o;
            boolean requiresSplit = this.requiresSplit.contains(invocation.getMethod());
            for (Object arg : invocation.getArguments()) {
                if (!requiresSplit)
                    publisher.publish(arg);
                else if (arg.getClass().isArray())
                    for (Object event : (Object[]) arg)
                        publisher.publish(event);
                else if (arg instanceof Iterable)
                    for (Object event : (Iterable) arg)
                        publisher.publish(event);
                else
                    publisher.publish(arg);
            }
            return null;
        }


    }
}
