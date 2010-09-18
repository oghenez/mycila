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
import com.mycila.event.annotation.Multiple;
import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Request;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.internal.ClassUtils;
import com.mycila.event.internal.MethodSignature;
import com.mycila.event.internal.Proxy;
import com.mycila.event.internal.Subscriptions;
import com.mycila.event.spi.Publisher;
import com.mycila.event.spi.Publishers;
import com.mycila.event.spi.Requestor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

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

    public <T> T register(T instance) {
        notNull(instance, "Instance");
        return Proxy.isMycilaProxy(instance.getClass()) ?
                instance :
                registerInternal(instance);
    }

    public <T> T instanciate(Class<T> abstractClassOrInterface) {
        notNull(abstractClassOrInterface, "Abstract class or interface");
        return registerInternal(Proxy.proxy(abstractClassOrInterface, new PublisherInterceptor(dispatcher, abstractClassOrInterface)));
    }

    public <T> BlockingQueue<T> createSynchronousQueue(Topics topics, Class<T> eventType) {
        MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new SynchronousQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createBoundedQueue(Topics topics, Class<T> eventType, int capacity) {
        MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new ArrayBlockingQueue<T>(capacity));
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createUnboundedQueue(Topics topics, Class<T> eventType) {
        MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new LinkedBlockingQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public <T> BlockingQueue<T> createPriorityQueue(Topics topics, Class<T> eventType) {
        MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new PriorityBlockingQueue<T>());
        dispatcher.subscribe(topics, eventType, queue);
        return queue;
    }

    public static MycilaEvent with(Dispatcher dispatcher) {
        return new MycilaEvent(dispatcher);
    }

    /* PRIVATE */


    private <T> T registerInternal(T instance) {
        final Iterable<Method> methods = findMethods(getTargetClass(instance.getClass()));
        for (Method method : filter(methods, annotatedBy(Subscribe.class))) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            dispatcher.subscribe(Topic.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
        }
        for (Method method : filter(methods, annotatedBy(Answers.class))) {
            Answers answers = method.getAnnotation(Answers.class);
            dispatcher.subscribe(Topic.anyOf(answers.topics()), EventMessage.class, Subscriptions.createResponder(instance, method));
        }
        return instance;
    }

    private static final class PublisherInterceptor implements MethodInterceptor {
        private final Map<MethodSignature, Publisher<Object>> publisherCache = new HashMap<MethodSignature, Publisher<Object>>();
        private final Map<MethodSignature, Requestor<Object[], Object>> requestorCache = new HashMap<MethodSignature, Requestor<Object[], Object>>();
        private final Object delegate;

        private PublisherInterceptor(Dispatcher dispatcher, final Class<?> c) {
            Iterable<Method> allMethods = ClassUtils.getAllDeclaredMethods(c, false);
            // find publishers
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Publish.class)) {
                hasSomeArgs(method);
                Publish annotation = method.getAnnotation(Publish.class);
                Publisher<Object> publisher = Publishers.createPublisher(dispatcher, Topic.topics(annotation.topics()));
                publisherCache.put(MethodSignature.of(method), publisher);
            }
            // find requestors
            for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Request.class)) {
                Request annotation = method.getAnnotation(Request.class);
                Requestor<Object[], Object> requestor = Publishers.createRequestor(dispatcher, Topic.topic(annotation.topic()), annotation.timeout(), annotation.unit());
                requestorCache.put(MethodSignature.of(method), requestor);
            }
            delegate = !c.isInterface() ? null : new Object() {
                @Override
                public String toString() {
                    return c.getName() + "$$EnhancerByMycilaEvent@" + Integer.toHexString(hashCode());
                }
            };
        }

        @SuppressWarnings({"unchecked"})
        public Object invoke(MethodInvocation invocation) throws Throwable {
            MethodSignature methodSignature = MethodSignature.of(invocation.getMethod());
            Publisher<Object> publisher = publisherCache.get(methodSignature);
            if (publisher != null)
                return handlePublishing(publisher, invocation);
            Requestor<Object[], Object> requestor = requestorCache.get(methodSignature);
            if (requestor != null)
                return requestor.request(invocation.getArguments());
            return delegate == null ?
                    invocation.proceed() :
                    invocation.getMethod().invoke(delegate, invocation.getArguments());
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

    private static final class MycilaEventQueue<T> implements BlockingQueue<T>, Subscriber<T>, Referencable {

        private final BlockingQueue<T> delegate;

        private MycilaEventQueue(BlockingQueue<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Reachability getReachability() {
            return Reachability.WEAK;
        }

        @Override
        public void onEvent(Event<? extends T> e) throws Exception {
            offer(e.getSource());
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MycilaEventQueue that = (MycilaEventQueue) o;
            return delegate.equals(that.delegate);
        }

        /* AUTO GENERATED DELEGATES */

        @Override
        public boolean add(T t) {
            return delegate.add(t);
        }

        @Override
        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        @Override
        public int drainTo(Collection<? super T> c) {
            return delegate.drainTo(c);
        }

        @Override
        public int drainTo(Collection<? super T> c, int maxElements) {
            return delegate.drainTo(c, maxElements);
        }

        @Override
        public boolean offer(T t) {
            return delegate.offer(t);
        }

        @Override
        public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.offer(t, timeout, unit);
        }

        @Override
        public T poll(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.poll(timeout, unit);
        }

        @Override
        public void put(T t) throws InterruptedException {
            delegate.put(t);
        }

        @Override
        public int remainingCapacity() {
            return delegate.remainingCapacity();
        }

        @Override
        public boolean remove(Object o) {
            return delegate.remove(o);
        }

        @Override
        public T take() throws InterruptedException {
            return delegate.take();
        }

        @Override
        public T element() {
            return delegate.element();
        }

        @Override
        public T peek() {
            return delegate.peek();
        }

        @Override
        public T poll() {
            return delegate.poll();
        }

        @Override
        public T remove() {
            return delegate.remove();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return delegate.addAll(c);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return delegate.iterator();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return delegate.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return delegate.retainAll(c);
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public Object[] toArray() {
            return delegate.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return delegate.toArray(a);
        }

    }
}
