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

import com.mycila.event.annotation.Reference;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Subscriptions {

    private Subscriptions() {
    }

    static Subscriber createSubscriber(Object instance, Method method) {
        return new MethodSubscriber(instance, method);
    }

    static Vetoer createVetoer(Object instance, Method method) {
        return new MethodVetoer(instance, method);
    }

    static <E, S> Subscription create(final TopicMatcher matcher, final Class<E> eventType, final S subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        return new Subscription<E, S>() {
            final Reachability reachability = subscriber instanceof Referencable ?
                    ((Referencable) subscriber).reachability() :
                    Reachability.of(subscriber.getClass());

            @Override
            public TopicMatcher topicMatcher() {
                return matcher;
            }

            @Override
            public Class<E> eventType() {
                return eventType;
            }

            @Override
            public S subscriber() {
                return subscriber;
            }

            @Override
            public Reachability reachability() {
                return reachability;
            }
        };
    }

    private static class ReferencableMethod implements Referencable {
        final Reachability reachability;
        final Object target;
        final FastMethod method;

        ReferencableMethod(Object target, Method method) {
            notNull(target, "Target object");
            notNull(method, "Method");
            this.method = ClassUtils.fast(method);
            this.target = target;
            this.reachability = method.isAnnotationPresent(Reference.class) ?
                    method.getAnnotation(Reference.class).value() :
                    Reachability.of(target.getClass());
            notNull(reachability, "Value of @Reference on method " + method);
        }

        @Override
        public final Reachability reachability() {
            return reachability;
        }
    }

    private static final class MethodSubscriber<E> extends ReferencableMethod implements Subscriber<E> {
        MethodSubscriber(Object target, Method method) {
            super(target, method);
            uniqueArg(Event.class, method);
            method.setAccessible(true);
        }

        @Override
        public void onEvent(Event<E> event) throws Exception {
            method.invoke(target, new Object[]{event});
        }
    }

    private static final class MethodVetoer<E> extends ReferencableMethod implements Vetoer<E> {
        MethodVetoer(Object target, Method method) {
            super(target, method);
            uniqueArg(VetoableEvent.class, method);
            method.setAccessible(true);
        }

        @Override
        public void check(VetoableEvent<E> vetoableEvent) {
            try {
                method.invoke(target, new Object[]{vetoableEvent});
            } catch (Exception t) {
                throw ExceptionUtils.toRuntime(t);
            }
        }
    }

}
