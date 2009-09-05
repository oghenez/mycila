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
package com.mycila.combination;

import com.mycila.math.Factorial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mathieu Carbou
 */
public final class PermutationSet<T> implements Iterable<List<T>> {

    final Factoradic factoradic;
    final List<T> objects;
    final long max;

    PermutationSet(Factoradic factoradic, List<T> objects) {
        this.factoradic = factoradic;
        this.objects = objects;
        this.max = Factorial.lookup(objects.size());
    }

    public List<T> get(int index) {
        final List<T> objs = new ArrayList<T>(objects);
        final List<T> elements = new ArrayList<T>(objects.size());
        int[] digits = factoradic.digits(index);
        for (int i = 0, max = digits.length; i < max; i++)
            elements.add(objs.remove(digits[i]));
        return elements;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < max;
            }

            @Override
            public List<T> next() {
                if (index == max) throw new NoSuchElementException();
                return get(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }
        };
    }
}
