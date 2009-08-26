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
