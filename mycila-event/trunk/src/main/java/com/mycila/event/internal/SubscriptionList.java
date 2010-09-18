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

package com.mycila.event.internal;

import com.mycila.event.Ref;
import com.mycila.event.Subscription;
import com.mycila.event.internal.FilterIterator;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SubscriptionList<E> implements Iterable<Subscription<E>> {

    private final CopyOnWriteArrayList<Ref<Subscription<E>>> subscriptions = new CopyOnWriteArrayList<Ref<Subscription<E>>>();

    public boolean add(Subscription<E> subscription) {
        return subscriptions.add(subscription.getReachability().wrap(subscription));
    }

    public boolean isEmpty() {
        return subscriptions.isEmpty();
    }

    public int size() {
        return subscriptions.size();
    }

    public Iterator<Subscription<E>> iterator() {
        return new FilterIterator<Subscription<E>, Ref<Subscription<E>>>(subscriptions.iterator()) {
            @Override
            protected Subscription<E> filter(Ref<Subscription<E>> ref) {
                Subscription<E> next = ref.get();
                if (next == null) subscriptions.remove(ref);
                return next;
            }
        };
    }

    public void remove(Subscription<E> subscription) {
        for (Ref<Subscription<E>> ref : subscriptions)
            if (subscription.equals(ref.get()))
                subscriptions.remove(ref);
    }
}
