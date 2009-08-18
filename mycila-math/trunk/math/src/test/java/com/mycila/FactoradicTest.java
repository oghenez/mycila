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
package com.mycila;

import com.mycila.combination.Factoradic;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
public final class FactoradicTest {

    @Test
    public void test_digits() {
        assertEquals(Factoradic.base(10).toString(), "Factoradic base 10: [362880, 40320, 5040, 720, 120, 24, 6, 2, 1, 1]");
        assertArrayEquals(Factoradic.base(10).digits(999999), new int[]{2, 6, 6, 2, 5, 1, 2, 1, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(145), new int[]{0, 0, 0, 0, 1, 1, 0, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(154), new int[]{0, 0, 0, 0, 1, 1, 1, 2, 0, 0});
        assertArrayEquals(Factoradic.base(10).digits(541), new int[]{0, 0, 0, 0, 4, 2, 2, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(121), new int[]{0, 0, 0, 0, 1, 0, 0, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(123), new int[]{0, 0, 0, 0, 1, 0, 0, 1, 1, 0});
    }

    @Test
    public void test_permutation() {
        assertEquals(Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(999999), Arrays.asList(2, 7, 8, 3, 9, 1, 5, 4, 6, 0));
        assertEquals(Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(0), Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(3628799), Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_permutation2() {
        Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(3628800);
    }

    @Test
    public void test_iterate() {
        int count = 0;
        for (List<String> list : Factoradic.base(4).permutations("A", "B", "C", "D")) {
            System.out.println(count++ + ": " + list);
        }
    }

}
