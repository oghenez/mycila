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
