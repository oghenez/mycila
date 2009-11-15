package com.mycila.event;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
abstract class FilterIterator<T, D> implements Iterator<T> {

    private final Iterator<D> delegate;
    private T next;
    private boolean hasNext = true;

    FilterIterator(Iterator<D> delegate) {
        notNull(delegate, "Iterator");
        this.delegate = delegate;
    }

    @Override
    public final boolean hasNext() {
        while (delegate.hasNext()) {
            D d = delegate.next();
            next = filter(d);
            if (next != null)
                return hasNext = true;
        }
        return hasNext = false;
    }

    @Override
    public final T next() {
        if (!hasNext)
            throw new NoSuchElementException();
        return next;
    }

    @Override
    public final void remove() {
        delegate.remove();
    }

    abstract T filter(D value);
}
