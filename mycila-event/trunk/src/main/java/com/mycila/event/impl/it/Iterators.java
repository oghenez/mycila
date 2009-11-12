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

package com.mycila.event.impl.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.mycila.event.api.util.Ensure.*;

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
