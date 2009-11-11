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

package com.mycila.event.impl;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.Event;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Topic;
import com.mycila.event.api.TopicMatcher;
import com.mycila.event.api.Vetoer;

import java.util.Iterator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class DispatcherDelegate implements Dispatcher {

    final DispatcherSkeleton delegate;

    DispatcherDelegate(DispatcherSkeleton dispatcherSkeleton) {
        this.delegate = dispatcherSkeleton;
    }

    <E, S> Iterator<S> getSubscribers(Event<E> event) {
        return delegate.getSubscribers(event);
    }

    <E, S> Iterator<S> getVetoers(Event<E> event) {
        return delegate.getVetoers(event);
    }

    <E> boolean isVetoed(Event<E> event) {
        return delegate.isVetoed(event);
    }

    @Override
    public <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
        delegate.register(matcher, eventType, vetoer);
    }

    @Override
    public <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
        delegate.subscribe(matcher, eventType, subscriber);
    }

    @Override
    public <E> void unregister(Vetoer<E> vetoer) {
        delegate.unregister(vetoer);
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
        delegate.unsubscribe(subscriber);
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        delegate.publish(topic, source);
    }
}