package com.mycila.ujd.impl;

import com.google.common.collect.AbstractIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class MemoizingIterator<T> extends AbstractIterator<T> {
    private final Iterator<? extends T> it;
    private final Set<T> cache = new HashSet<T>();

    MemoizingIterator(Iterator<? extends T> it) {
        this.it = it;
    }

    @Override
    protected T computeNext() {
        while (it.hasNext()) {
            T t = it.next();
            if (cache.add(t))
                return t;
        }
        return endOfData();
    }
}
