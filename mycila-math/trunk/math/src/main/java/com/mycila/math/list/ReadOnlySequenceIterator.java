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
package com.mycila.math.list;

import com.mycila.math.range.IntRange;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author Mathieu Carbou
 */
public final class ReadOnlySequenceIterator<T> implements Iterator<T> {

    private final Object array;
    private int current;
    private final IntRange range;

    private ReadOnlySequenceIterator(Object array, IntRange range) {
        this.array = array;
        this.range = range;
        current = range.from;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public T next() {
        return (T) Array.get(array, current++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }

    @Override
    public boolean hasNext() {
        return current <= range.to;
    }

    public static <T> Iterator<T> on(T... array) {
        return on(array.length == 0 ? IntRange.empty() : IntRange.range(0, array.length - 1), array);
    }

    public static <T> Iterator<T> on(IntRange range, T... array) {
        return new ReadOnlySequenceIterator<T>(array, range);
    }

    public static Iterator<Integer> on(int[] array) {
        return on(array.length == 0 ? IntRange.empty() : IntRange.range(0, array.length - 1), array);
    }

    public static Iterator<Integer> on(IntRange range, int[] array) {
        return new ReadOnlySequenceIterator<Integer>(array, range);
    }

    public static Iterator<Long> on(long[] array) {
        return on(array.length == 0 ? IntRange.empty() : IntRange.range(0, array.length - 1), array);
    }

    public static Iterator<Long> on(IntRange range, long[] array) {
        return new ReadOnlySequenceIterator<Long>(array, range);
    }

}
