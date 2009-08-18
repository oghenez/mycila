package com.mycila.sequence;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author Mathieu Carbou
 */
public final class ReadOnlySequenceIterator<T> implements Iterator<T> {

    private final Object array;
    private int current = 0;

    private ReadOnlySequenceIterator(Object array) {
        this.array = array;
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
        return current < Array.getLength(array);
    }

    public static <T> Iterator<T> on(T[] array) {
        return new ReadOnlySequenceIterator<T>(array);
    }

    public static Iterator<Integer> on(int[] array) {
        return new ReadOnlySequenceIterator<Integer>(array);
    }

    public static Iterator<Long> on(long[] array) {
        return new ReadOnlySequenceIterator<Long>(array);
    }
}
