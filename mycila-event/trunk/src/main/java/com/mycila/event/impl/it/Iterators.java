package com.mycila.event.impl.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Iterators {

    private Iterators() {
    }

    public static <S, D> Iterator<D> transform(final Iterator<S> it, final IteratorTransformer<S, D> transformer) {
        notNull(it, "Iterator");
        notNull(transformer, "IteratorTransformer");
        return new Iterator<D>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public D next() {
                return transformer.transform(it.next());
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    public static <T> Iterator<T> filter(final Iterator<T> it, final IteratorFilter<T> filter) {
        notNull(it, "Iterator");
        notNull(filter, "IteratorFilter");
        return new Iterator<T>() {
            private T next;
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                while (it.hasNext()) {
                    next = it.next();
                    if (filter.accept(next))
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
