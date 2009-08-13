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
package com.mycila.distribution;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou
 */
public final class Distribution<T> implements Iterable<Item<T>> {

    private final Map<T, Item<T>> items = new HashMap<T, Item<T>>();

    private Distribution() {
    }

    public Distribution<T> add(T value) {
        Item<T> item = items.get(value);
        if (item == null) {
            item = Item.from(value);
            items.put(value, item);
        }
        item.increment();
        return this;
    }

    public Set<T> itemsHavingCount(int count) {
        final Set<T> items = new HashSet<T>();
        for (Item<T> item : this.items.values()) {
            if (item.count() == count) {
                items.add(item.value());
            }
        }
        return items;
    }

    public int itemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Iterator<Item<T>> iterator() {
        return Collections.unmodifiableCollection(items.values()).iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Item<T> item : items.values()) sb.append(", ").append(item);
        return sb.length() > 2 ? sb.delete(0, 2).toString() : sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distribution that = (Distribution) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public static <T> Distribution<T> of(Class<T> type) {
        return new Distribution<T>();
    }
}
