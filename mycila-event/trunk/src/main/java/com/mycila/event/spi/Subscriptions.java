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

    static <E> Subscriber<E> createSubscriber(Object instance, Method method) {
        return new MethodSubscriber<E>(instance, method);
    }

    static <E> Subscription create(final TopicMatcher matcher, final Class<?> eventType, final Subscriber<E> subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        return new Subscription<E>() {
            final Reachability reachability = subscriber instanceof Referencable ?
                    ((Referencable) subscriber).getReachability() :
                    Reachability.of(subscriber.getClass());

            @Override
            public TopicMatcher getTopicMatcher() {
                return matcher;
            }

            @Override
            public Class<?> getEventType() {
                return eventType;
            }

            @Override
            public Subscriber<E> getSubscriber() {
                return subscriber;
            }

            @Override
            public Reachability getReachability() {
                return reachability;
            }
        };
    }

    private static class ReferencableMethod implements Referencable {
        final Reachability reachability;
        final Object target;
        final Invokable invokable;

        ReferencableMethod(Object target, final Method method) {
            notNull(target, "Target object");
            notNull(method, "Method");
            this.target = target;
            this.reachability = method.isAnnotationPresent(Reference.class) ?
                    method.getAnnotation(Reference.class).value() :
                    Reachability.of(target.getClass());
            notNull(reachability, "Value of @Reference on method " + method);
            this.invokable = Modifier.isPrivate(method.getModifiers()) ?
                    new Invokable() {
                        {
                            if (!method.isAccessible())
                                method.setAccessible(true);
                        }

                        @Override
                        public void invoke(Object target, Object... args) throws Exception {
                            try {
                                method.invoke(target, args);
                            } catch (Exception e) {
                                ExceptionUtils.reThrow(e);
                            }
                        }
                    } :
                    new Invokable() {
                        final FastMethod m = Proxy.fastMethod(method);

                        @Override
                        public void invoke(Object target, Object... args) throws Exception {
                            try {
                                m.invoke(target, args);
                            } catch (Exception e) {
                                ExceptionUtils.reThrow(e);
                            }
                        }
                    };
        }

        @Override
        public final Reachability getReachability() {
            return reachability;
        }

        protected final void invoke(Object... args) throws Exception {
            invokable.invoke(target, args);
        }
    }

    private static final class MethodSubscriber<E> extends ReferencableMethod implements Subscriber<E> {
        MethodSubscriber(Object target, Method method) {
            super(target, method);
            hasOneArg(Event.class, method);
        }

        @Override
        public void onEvent(Event<E> event) throws Exception {
            invoke(event);
        }
    }

    private static interface Invokable {
        void invoke(Object target, Object... args) throws Exception;
    }
}
