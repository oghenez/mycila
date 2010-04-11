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
import com.mycila.event.api.Event;
import com.mycila.event.api.Reachability;
import com.mycila.event.api.Referencable;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.ext.EventQueueManager;
import com.mycila.event.api.topic.TopicMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EventQueueManagers {

    private EventQueueManagers() {
    }

    public static EventQueueManager create(final Dispatcher dispatcher) {
        return new EventQueueManager() {
            public <T> BlockingQueue<T> createSynchronousQueue(TopicMatcher topics, Class<T> eventType) {
                MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new SynchronousQueue<T>());
                dispatcher.subscribe(topics, eventType, queue);
                return queue;
            }

            public <T> BlockingQueue<T> createBoundedQueue(TopicMatcher topics, Class<T> eventType, int capacity) {
                MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new ArrayBlockingQueue<T>(capacity));
                dispatcher.subscribe(topics, eventType, queue);
                return queue;
            }

            public <T> BlockingQueue<T> createUnboundedQueue(TopicMatcher topics, Class<T> eventType) {
                MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new LinkedBlockingQueue<T>());
                dispatcher.subscribe(topics, eventType, queue);
                return queue;
            }

            public <T> BlockingQueue<T> createPriorityQueue(TopicMatcher topics, Class<T> eventType) {
                MycilaEventQueue<T> queue = new MycilaEventQueue<T>(new PriorityBlockingQueue<T>());
                dispatcher.subscribe(topics, eventType, queue);
                return queue;
            }
        };
    }


    private static final class MycilaEventQueue<T> implements BlockingQueue<T>, Subscriber<T>, Referencable {

        private final BlockingQueue<T> delegate;

        private MycilaEventQueue(BlockingQueue<T> delegate) {
            this.delegate = delegate;
        }

        public Reachability getReachability() {
            return Reachability.WEAK;
        }

        public void onEvent(Event<T> e) throws Exception {
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

        public boolean add(T t) {
            return delegate.add(t);
        }

        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        public int drainTo(Collection<? super T> c) {
            return delegate.drainTo(c);
        }

        public int drainTo(Collection<? super T> c, int maxElements) {
            return delegate.drainTo(c, maxElements);
        }

        public boolean offer(T t) {
            return delegate.offer(t);
        }

        public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.offer(t, timeout, unit);
        }

        public T poll(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.poll(timeout, unit);
        }

        public void put(T t) throws InterruptedException {
            delegate.put(t);
        }

        public int remainingCapacity() {
            return delegate.remainingCapacity();
        }

        public boolean remove(Object o) {
            return delegate.remove(o);
        }

        public T take() throws InterruptedException {
            return delegate.take();
        }

        public T element() {
            return delegate.element();
        }

        public T peek() {
            return delegate.peek();
        }

        public T poll() {
            return delegate.poll();
        }

        public T remove() {
            return delegate.remove();
        }

        public boolean addAll(Collection<? extends T> c) {
            return delegate.addAll(c);
        }

        public void clear() {
            delegate.clear();
        }

        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        public Iterator<T> iterator() {
            return delegate.iterator();
        }

        public boolean removeAll(Collection<?> c) {
            return delegate.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return delegate.retainAll(c);
        }

        public int size() {
            return delegate.size();
        }

        public Object[] toArray() {
            return delegate.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return delegate.toArray(a);
        }

    }
}
