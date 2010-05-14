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

import com.mycila.event.api.Event;
import com.mycila.event.api.Reachability;
import com.mycila.event.api.Referencable;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.message.MessageResponse;
import com.mycila.event.api.topic.TopicMatcher;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Subscriptions {

    private Subscriptions() {
    }

    static <E> Subscription create(final TopicMatcher matcher, final Class<?> eventType, final Subscriber<E> subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        return new Subscription<E>() {
            final Reachability reachability = subscriber instanceof Referencable ?
                    ((Referencable) subscriber).getReachability() :
                    Reachability.of(subscriber.getClass());

            public TopicMatcher getTopicMatcher() {
                return matcher;
            }

            public Class<?> getEventType() {
                return eventType;
            }

            public Subscriber<E> getSubscriber() {
                return subscriber;
            }

            public Reachability getReachability() {
                return reachability;
            }
        };
    }

    static <E> Subscriber<E> createSubscriber(Object instance, Method method) {
        return new MethodSubscriber<E>(instance, method);
    }

    static <R> Subscriber<MessageResponse<R>> createResponder(Object instance, Method method) {
        return new MethodResponder<R>(instance, method);
    }

    private static class ReferencableMethod<T> implements Referencable {
        private final Reachability reachability;
        private final Object target;
        private final Invokable<T> invokable;

        ReferencableMethod(Object target, final Method method) {
            notNull(target, "Target object");
            notNull(method, "Method");
            this.target = target;
            this.reachability = method.isAnnotationPresent(Reference.class) ?
                    method.getAnnotation(Reference.class).value() :
                    Reachability.of(target.getClass());
            notNull(reachability, "Value of @Reference on method " + method);
            this.invokable = Modifier.isPrivate(method.getModifiers()) ?
                    new Invokable<T>() {
                        {
                            if (!method.isAccessible())
                                method.setAccessible(true);
                        }

                        public T invoke(Object target, Object... args) throws Exception {
                            return (T) method.invoke(target, args);
                        }
                    } :
                    new Invokable<T>() {
                        final FastMethod m = Proxy.fastMethod(method);

                        public T invoke(Object target, Object... args) throws Exception {
                            return (T) m.invoke(target, args);
                        }
                    };
        }


        public final Reachability getReachability() {
            return reachability;
        }

        protected final T invoke(Object... args) throws Exception {
            return invokable.invoke(target, args);
        }
    }

    private static final class MethodSubscriber<E> extends ReferencableMethod implements Subscriber<E> {
        MethodSubscriber(Object target, Method method) {
            super(target, method);
            hasOneArg(Event.class, method);
        }

        public void onEvent(Event<E> event) throws Exception {
            invoke(event);
        }
    }

    private static final class MethodResponder<R> extends ReferencableMethod<R> implements Subscriber<MessageResponse<R>> {

        private final int len;

        MethodResponder(Object target, Method method) {
            super(target, method);
            this.len = method.getParameterTypes().length;
        }

        public void onEvent(Event<MessageResponse<R>> event) throws Exception {
            if (len == 0) event.getSource().reply(invoke());
                else event.getSource().reply(invoke(event.getSource().getParameters()));
        }
    }

    private static interface Invokable<T> {
        T invoke(Object target, Object... args) throws Exception;
    }
}
