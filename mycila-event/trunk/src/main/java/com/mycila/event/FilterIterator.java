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
