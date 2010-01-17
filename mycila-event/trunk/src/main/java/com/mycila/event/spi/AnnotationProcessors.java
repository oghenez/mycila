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

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.annotation.AnnotationProcessor;
import com.mycila.event.api.annotation.Answers;
import com.mycila.event.api.annotation.Multiple;
import com.mycila.event.api.annotation.Publish;
import com.mycila.event.api.annotation.Request;
import com.mycila.event.api.annotation.Subscribe;
import com.mycila.event.api.message.MessageResponse;
import com.mycila.event.api.topic.Topics;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AnnotationProcessors {

    private AnnotationProcessors() {
    }

    public static AnnotationProcessor create(final Dispatcher dispatcher) {
        return new AnnotationProcessor() {
            public <T> T process(T instance) {
                notNull(instance, "Instance");
                return Proxy.isMycilaProxy(instance.getClass()) ?
                        instance :
                        processInternal(instance);
            }

            public <T> T proxy(Class<T> abstractClassOrInterface) {
                notNull(abstractClassOrInterface, "Abstract class or interface");
                return processInternal(Proxy.proxy(abstractClassOrInterface, new PublisherInterceptor(dispatcher, abstractClassOrInterface)));
            }

            private <T> T processInternal(T instance) {
                final Iterable<Method> methods = ClassUtils.getAllDeclaredMethods(instance.getClass(), false);
                for (Method method : ClassUtils.filterAnnotatedMethods(methods, Subscribe.class)) {
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    dispatcher.subscribe(Topics.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
                }
                for (Method method : ClassUtils.filterAnnotatedMethods(methods, Answers.class)) {
                    Answers answers = method.getAnnotation(Answers.class);
                    dispatcher.subscribe(Topics.anyOf(answers.topics()), MessageResponse.class, Subscriptions.createResponder(instance, method));
                }
                return instance;
            }
        };
    }

    private static final class PublisherInterceptor implements MethodInterceptor {
        private final Map<MethodSignature, Publisher> publisherCache = new HashMap<MethodSignature, Publisher>();
        private final Map<MethodSignature, Requestor> requestorCache = new HashMap<MethodSignature, Requestor>();
        private final Object delegate;

        private PublisherInterceptor(Dispatcher dispatcher, final Class<?> c) {
            Iterable<Method> allMethods = ClassUtils.getAllDeclaredMethods(c, false);
            // find publishers
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Publish.class)) {
                hasSomeArgs(method);
                Publish annotation = method.getAnnotation(Publish.class);
                Publisher publisher = Publishers.createPublisher(dispatcher, Topics.topics(annotation.topics()));
                publisherCache.put(MethodSignature.of(method), publisher);
            }
            // find requestors
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Request.class)) {
                Request annotation = method.getAnnotation(Request.class);
                Requestor requestor = Publishers.createRequestor(dispatcher, Topics.topic(annotation.topic()), annotation.timeout(), annotation.unit());
                requestorCache.put(MethodSignature.of(method), requestor);
            }
            delegate = !c.isInterface() ? null : new Object() {
                @Override
                public String toString() {
                    // com.mycila.event.spi.ComTest$DU$$EnhancerByMycilaEvent$$acd8e1af@1971afc
                    return c.getName() + "$$EnhancerByMycilaEvent@" + Integer.toHexString(hashCode());
                }
            };
        }

        @SuppressWarnings({"unchecked"})
        public Object invoke(MethodInvocation invocation) throws Throwable {
            MethodSignature methodSignature = MethodSignature.of(invocation.getMethod());
            Publisher publisher = publisherCache.get(methodSignature);
            if (publisher != null)
                return handlePublishing(publisher, invocation);
            Requestor requestor = requestorCache.get(methodSignature);
            if (requestor != null)
                return handleRequest(requestor, invocation);
            try {
                return delegate == null ?
                        invocation.proceed() :
                        invocation.getMethod().invoke(delegate, invocation.getArguments());
            } catch (Exception e) {
                throw ExceptionUtils.handle(e);
            }
        }

        private static Object handleRequest(Requestor<Object, Object> requestor, MethodInvocation invocation) throws Exception {
            switch (invocation.getArguments().length) {
                case 0:
                    return requestor.request(null);
                case 1:
                    return requestor.request(invocation.getArguments()[0]);
                default:
                    return requestor.request(invocation.getArguments());
            }
        }

        private static Object handlePublishing(Publisher<Object> publisher, MethodInvocation invocation) {
            boolean requiresSplit = invocation.getMethod().isAnnotationPresent(Multiple.class);
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
