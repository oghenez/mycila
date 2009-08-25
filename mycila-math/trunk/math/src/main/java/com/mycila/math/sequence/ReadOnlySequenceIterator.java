package com.mycila.math.sequence;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * @author Mathieu Carbou
 */
public final class ReadOnlySequenceIterator<T> implements Iterator<T> {

    private final Object array;
    private int current = 0;
    private final int size;

    private ReadOnlySequenceIterator(Object array, int size) {
        this.array = array;
        this.size = size;
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
        return current < size;
    }

    public static <T> Iterator<T> on(T... array) {
        return new ReadOnlySequenceIterator<T>(array, array.length);
    }

    public static <T> Iterator<T> on(T[] array, int size) {
        return new ReadOnlySequenceIterator<T>(array, size);
    }

    public static Iterator<Integer> on(int... array) {
        return new ReadOnlySequenceIterator<Integer>(array, array.length);
    }

    public static Iterator<Integer> on(int[] array, int size) {
        return new ReadOnlySequenceIterator<Integer>(array, size);
    }

    public static Iterator<Long> on(long... array) {
        return new ReadOnlySequenceIterator<Long>(array, array.length);
    }

    public static Iterator<Long> on(long[] array, int size) {
        return new ReadOnlySequenceIterator<Long>(array, size);
    }

}
