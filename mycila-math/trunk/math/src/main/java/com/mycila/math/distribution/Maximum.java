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
package com.mycila.math.distribution;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Mathieu Carbou
 */
public final class Maximum<T> implements Iterable<Item<T>> {

    private final Set<Item<T>> items = new HashSet<Item<T>>();
    private final int value;

    private Maximum(Distribution<T> distribution) {
        int max = 0;
        for (Item<T> item : distribution) if (item.count() > max) max = item.count();
        for (Item<T> item : distribution) if (item.count() == max) items.add(item);
        value = max;
    }

    public int value() {
        return value;
    }

    public int itemCount() {
        return items.size();
    }

    public Iterator<Item<T>> iterator() {
        return Collections.unmodifiableCollection(items).iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Item<T> item : items) sb.append(", ").append(item);
        return sb.length() > 2 ? sb.delete(0, 2).toString() : sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maximum maximum = (Maximum) o;
        return value == maximum.value && items.equals(maximum.items);
    }

    @Override
    public int hashCode() {
        int result = items.hashCode();
        result = 31 * result + value;
        return result;
    }

    public static <T> Maximum<T> of(Distribution<T> distribution) {
        return new Maximum<T>(distribution);
    }

}
