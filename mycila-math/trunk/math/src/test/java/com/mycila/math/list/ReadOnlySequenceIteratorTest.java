package com.mycila.math.list;

import com.mycila.math.range.IntRange;
import org.junit.Test;

import java.util.Iterator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ReadOnlySequenceIteratorTest {

    @Test
    public void test() throws Exception {
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(1, 2, 3);
            }
        })
            System.out.print(i + " ");
        System.out.println();
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(IntRange.range(1, 3), 2, 4, 6, 8, 10, 12);
            }
        })
            System.out.print(i + " ");
        System.out.println();
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(IntRange.range(0, 0), 2, 4, 6, 8, 10, 12);
            }
        })
            System.out.print(i + " ");
        System.out.println();
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(IntRange.range(5, 5), 2, 4, 6, 8, 10, 12);
            }
        })
            System.out.print(i + " ");
        System.out.println();
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(IntRange.range(5, 4), 2, 4, 6, 8, 10, 12);
            }
        })
            System.out.print(i + " ");
        System.out.println();
        for (Integer i : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(IntRange.range(3, 4), 2, 4, 6, 8, 10, 12);
            }
        })
            System.out.print(i + " ");
        System.out.println();
    }

}
