package com.mycila.event.impl;

import com.mycila.event.api.Ref;
import com.mycila.event.api.Referencable;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
        return refs.add(t.reachability().wrap(t));
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
        return new Iterator<T>() {
            final Iterator<Ref<T>> it = refs.iterator();
            private T next;
            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                while (it.hasNext()) {
                    next = it.next().get();
                    if (next == null)
                        it.remove();
                    else
                        return hasNext = true;
                }
                return hasNext = false;
            }

            @Override
            public T next() {
                if (!hasNext)
                    throw new NoSuchElementException();
                return next;
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }
}
