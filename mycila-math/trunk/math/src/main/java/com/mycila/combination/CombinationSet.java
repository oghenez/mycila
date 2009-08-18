package com.mycila.combination;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mathieu Carbou
 */
public final class CombinationSet<T> implements Iterable<int[]> {

    final int n;
    final int r;

    CombinationSet(int n, int r) {
        this.n = n;
        this.r = r;
    }

    public long size() {
        return Combinations.binomial(n, r);
    }

    public List<int[]> asList() {
        final List<int[]> list = new LinkedList<int[]>();
        for (int[] ints : this) {
            list.add(ints);
        }
        return list;
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {
            long index = 0;
            final int[] a = new int[r];
            final int end = r - 1;
            final int total = n - r;
            final long max = size();

            @Override
            public boolean hasNext() {
                return index < max;
            }

            @Override
            public int[] next() {
                if (index == max) throw new NoSuchElementException();
                if (index > 0) {
                    int i = end;
                    while (a[i] == total + i)
                        i--;
                    a[i]++;
                    for (int j = i + 1; j < r; j++)
                        a[j] = a[i] + j - i;

                } else {
                    for (int i = 0; i < a.length; i++) a[i] = i;
                }
                index++;
                return Arrays.copyOf(a, a.length);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }
        };
    }
}