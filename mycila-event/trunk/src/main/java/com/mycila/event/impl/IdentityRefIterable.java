package com.mycila.event.impl;

import com.mycila.event.api.util.ref.Ref;
import com.mycila.event.api.util.ref.Referencable;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class IdentityRefIterable<T extends Referencable> implements Iterable<T> {

    private final ConcurrentLinkedQueue<Ref<T>> refs = new ConcurrentLinkedQueue<Ref<T>>();

    void add(T t) {
        refs.add(t.reachability().toRef(t));
    }

    void remove(T t) {
        for (Iterator<T> it = iterator(); it.hasNext();) {
            T next = it.next();
            if (next == t)
                it.remove();
        }
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<Ref<T>> it = refs.iterator();
        return new Iterator<T>() {
            T next;

            @Override
            public boolean hasNext() {
                while (it.hasNext()) {
                    next = it.next().get();
                    if (next == null) it.remove();
                    else return true;
                }
                return false;
            }

            @Override
            public T next() {
                return next;
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }
}
