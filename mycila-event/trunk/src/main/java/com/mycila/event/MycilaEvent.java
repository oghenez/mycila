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

import com.mycila.event.annotation.Answers;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.internal.EventQueue;
import com.mycila.event.internal.Proxy;
import com.mycila.event.internal.PublisherInterceptor;
import com.mycila.event.internal.Subscriptions;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import static com.google.common.collect.Iterables.*;
import static com.mycila.event.internal.Ensure.*;
import static com.mycila.event.internal.Reflect.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaEvent {

    private final Dispatcher dispatcher;

    private MycilaEvent(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public <T> BlockingQueue<T> createSynchronousQueue(Topics topics, Class<T> eventType) {
        EventQueue<T> queue = new EventQueue<T>(new SynchronousQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createBoundedQueue(Topics topics, Class<T> eventType, int capacity) {
        EventQueue<T> queue = new EventQueue<T>(new ArrayBlockingQueue<T>(capacity));
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createUnboundedQueue(Topics topics, Class<T> eventType) {
        EventQueue<T> queue = new EventQueue<T>(new LinkedBlockingQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createPriorityQueue(Topics topics, Class<T> eventType) {
        EventQueue<T> queue = new EventQueue<T>(new PriorityBlockingQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> T instanciate(Class<T> abstractClassOrInterface) {
        notNull(abstractClassOrInterface, "Abstract class or interface");
        T t = Proxy.proxy(abstractClassOrInterface, new PublisherInterceptor(dispatcher, abstractClassOrInterface));
        register(t);
        return t;
    }

    public void register(Object instance) {
        notNull(instance, "Instance");
        Proxy.isMycilaProxy(instance.getClass()) ?
                instance :
                registerInternal(instance);
        final Iterable<Method> methods = findMethods(getTargetClass(instance.getClass()));
        for (Method method : filter(methods, annotatedBy(Subscribe.class))) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            dispatcher.subscribe(Topic.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
        }
        for (Method method : filter(methods, annotatedBy(Answers.class))) {
            Answers answers = method.getAnnotation(Answers.class);
            dispatcher.subscribe(Topic.anyOf(answers.topics()), EventMessage.class, Subscriptions.createResponder(instance, method));
        }
    }

    /* STATIC CTOR */

    public static MycilaEvent with(Dispatcher dispatcher) {
        return new MycilaEvent(dispatcher);
    }

}
