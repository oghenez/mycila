package com.mycila.distribution;

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
