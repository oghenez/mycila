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
import com.mycila.event.api.FilterIterator;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.topic.Topic;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SubscriptionManager<E> {

    private final SubscriptionList<E> subscriptions = new SubscriptionList<E>();
    private final ConcurrentHashMap<Topic, SubscriptionList<E>> mappedSubscriptions = new ConcurrentHashMap<Topic, SubscriptionList<E>>();

    void addSubscription(Subscription<E> subscription) {
        subscriptions.add(subscription);
        for (Map.Entry<Topic, SubscriptionList<E>> entry : mappedSubscriptions.entrySet())
            if (subscription.getTopicMatcher().matches(entry.getKey()))
                entry.getValue().add(subscription);
    }

    void removeSubscriber(Subscriber<E> subscriber) {
        for (Subscription<E> subscription : subscriptions)
            if (subscription.getSubscriber().equals(subscriber)) {
                subscriptions.remove(subscription);
                for (Map.Entry<Topic, SubscriptionList<E>> entry : mappedSubscriptions.entrySet())
                    if (subscription.getTopicMatcher().matches(entry.getKey()))
                        entry.getValue().remove(subscription);
            }
    }

    Iterator<Subscription<E>> getSubscriptions(final Event<E> event) {
        final Topic topic = event.getTopic();
        SubscriptionList<E> subscriptionList = mappedSubscriptions.get(topic);
        if (subscriptionList == null) {
            subscriptionList = new SubscriptionList<E>();
            for (Subscription<E> subscription : this.subscriptions)
                if (subscription.getTopicMatcher().matches(topic))
                    subscriptionList.add(subscription);
            final SubscriptionList<E> old = mappedSubscriptions.putIfAbsent(topic, subscriptionList);
            if (old != null) subscriptionList = old;
        }
        return new FilterIterator<Subscription<E>, Subscription<E>>(subscriptionList.iterator()) {
            final Class<?> eventType = event.getSource().getClass();

            @Override
            protected Subscription<E> filter(Subscription<E> subscription) {
                return subscription.getEventType().isAssignableFrom(eventType) ? subscription : null;
            }
        };
    }

}
