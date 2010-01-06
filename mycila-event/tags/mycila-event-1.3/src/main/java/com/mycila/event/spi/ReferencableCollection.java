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

import com.mycila.event.api.FilterIterator;
import com.mycila.event.api.Ref;
import com.mycila.event.api.Referencable;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ReferencableCollection<T extends Referencable> extends AbstractCollection<T> {

    private final ConcurrentLinkedQueue<Ref<T>> refs = new ConcurrentLinkedQueue<Ref<T>>();

    @Override
    public boolean add(T t) {
        notNull(t, "Referencable");
        return refs.add(t.getReachability().wrap(t));
    }

    @Override
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    @Override
    public int size() {
        int count = 0;
        for (Iterator<T> it = iterator(); it.hasNext(); it.next())
            count++;
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilterIterator<T, Ref<T>>(refs.iterator()) {
            @Override
            protected T filter(Ref<T> delegate) {
                T next = delegate.get();
                if (next == null)
                    remove();
                return next;
            }
        };
    }
}