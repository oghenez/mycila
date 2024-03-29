/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.ujd.api;

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
