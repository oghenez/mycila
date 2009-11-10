package com.mycila.event.impl;

import com.mycila.event.api.Ref;
import com.mycila.event.impl.Referencable;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class IdentityRefIterable<T extends Referencable> extends AbstractCollection<T> {

    private final ConcurrentLinkedQueue<Ref<T>> refs = new ConcurrentLinkedQueue<Ref<T>>();

    @Override
    public boolean add(T t) {
        return refs.add(t.reachability().toRef(t));
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
