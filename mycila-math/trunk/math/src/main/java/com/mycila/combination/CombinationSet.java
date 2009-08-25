package com.mycila.combination;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
public final class CombinationSet implements Iterable<int[]> {

    final int n;
    final int r;

    CombinationSet(int n, int r) {
        this.n = n;
        this.r = r;
    }

    public long size() {
        return Combinations.binomialLong(n, r);
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
        if (n == 0 || r == 0)
            return new Iterator<int[]>() {
                boolean hasNext = true;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public int[] next() {
                    hasNext = false;
                    return new int[]{0};
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Remove not supported");
                }
            };
        return new Iterator<int[]>() {
            boolean first = true;
            final int[] pos = new int[r];
            final int end = r - 1;
            final int offset = n - r;
            long count = size();

            @Override
            public boolean hasNext() {
                return count > 0;
            }

            @Override
            public int[] next() {
                if (first) {
                    for (int i = 0; i < pos.length; i++) pos[i] = i;
                    first = false;
                } else {
                    int i = end;
                    while (pos[i] == offset + i)
                        i--;
                    pos[i]++;
                    for (int j = i + 1; j < r; j++)
                        pos[j] = pos[i] + j - i;

                }
                count--;
                return pos;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }
        };
    }
}
