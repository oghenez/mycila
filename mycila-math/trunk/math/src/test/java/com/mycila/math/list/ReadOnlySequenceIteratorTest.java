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
