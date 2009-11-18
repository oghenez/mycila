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

import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AnnotationProcessor {

    public abstract <T> T process(T instance);

    public abstract <T> T createPublisher(Class<T> abstractClassOrInterface);

    public static AnnotationProcessor create(final Dispatcher dispatcher) {
        return new AnnotationProcessor() {
            @Override
            public <T> T createPublisher(Class<T> abstractClassOrInterface) {
                notNull(abstractClassOrInterface, "Abstract class or interface");
                if (abstractClassOrInterface.isInterface())
                    return ClassUtils.createJDKProxy(abstractClassOrInterface, new PublisherInterceptor(dispatcher, abstractClassOrInterface));
                if (Modifier.isAbstract(abstractClassOrInterface.getModifiers()))
                    return ClassUtils.createCglibProxy(abstractClassOrInterface, new PublisherInterceptor(dispatcher, abstractClassOrInterface));
                throw new IllegalArgumentException("You cannot proxy an existing instance. Use instead AnnotationProcessor.process(instance).");
            }

            @SuppressWarnings({"unchecked"})
            @Override
            public <T> T process(T instance) {
                notNull(instance, "Instance");
                final Iterable<Method> methods = ClassUtils.getAllDeclaredMethods(instance.getClass());
                for (Method method : ClassUtils.filterAnnotatedMethods(methods, Veto.class)) {
                    Veto veto = method.getAnnotation(Veto.class);
                    dispatcher.subscribe(Topics.anyOf(veto.topics()), veto.eventType(), Subscriptions.createVetoer(instance, method));
                }
                for (Method method : ClassUtils.filterAnnotatedMethods(methods, Subscribe.class)) {
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    dispatcher.subscribe(Topics.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
                }
                for (Method method : ClassUtils.filterAnnotatedMethods(methods, Publish.class)) {
                    Publish annotation = method.getAnnotation(Publish.class);
                    uniqueArg(Publisher.class, method);
                    Publisher publisher = Publishers.create(dispatcher, Topics.topics(annotation.topics()));
                    method.setAccessible(true);
                    try {
                        method.invoke(instance, publisher);
                    } catch (Exception e) {
                        throw ExceptionUtils.toRuntime(e);
                    }
                }
                return instance;
            }
        };
    }

    private static final class PublisherInterceptor implements MethodInterceptor {
        final Map<Method, Publisher<Object>> cache = new HashMap<Method, Publisher<Object>>();

        PublisherInterceptor(Dispatcher dispatcher, Class<?> c) {
            Iterable<Method> allMethods = ClassUtils.getAllDeclaredMethods(c);
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Publish.class)) {
                hasArgs(method);
                Publish annotation = method.getAnnotation(Publish.class);
                Publisher<Object> publisher = Publishers.create(dispatcher, Topics.topics(annotation.topics()));
                cache.put(method, publisher);
            }
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Publisher<Object> p = cache.get(invocation.getMethod());
            if (p == null)
                return invocation.proceed();
            for (Object event : invocation.getArguments())
                p.publish(event);
            return null;
        }
    }
}
