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
package com.mycila.math;

import com.mycila.math.range.IntRange;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class DigitsTest {

    @Test
    public void test_isPandigital() {
        Digits pandigital = Digits.base(10);
        assertTrue(pandigital.isPandigital(932718654, 1, 9));
        assertFalse(pandigital.isPandigital(33, 1, 9));
        assertTrue(pandigital.isPandigital(123456789, 1, 9));
        assertTrue(pandigital.isPandigital(946138257, 1, 9));
        assertTrue(pandigital.isPandigital(1234, 1, 4));
        assertFalse(pandigital.isPandigital(1244, 1, 4));
        assertFalse(pandigital.isPandigital(946108257, 1, 9));
        assertFalse(pandigital.isPandigital(1234567899, 1, 9));
        assertFalse(pandigital.isPandigital(100, 1, 2));
        assertFalse(pandigital.isPandigital(100, 0, 1));
        assertTrue(pandigital.isPandigital(120, 0, 2));
        assertTrue(pandigital.isPandigital(342, 2, 4));
    }

    @Test
    public void test_range() {
        Digits pandigital = Digits.base(10);
        assertEquals(pandigital.pandigitalRange(0), IntRange.range(0, 0));
        assertEquals(pandigital.pandigitalRange(1), IntRange.range(1, 1));
        assertEquals(pandigital.pandigitalRange(57603421), IntRange.range(0, 7));
        assertEquals(pandigital.pandigitalRange(5763421), IntRange.range(1, 7));
        assertEquals(pandigital.pandigitalRange(10), IntRange.range(0, 1));
        assertEquals(pandigital.pandigitalRange(23459), null);
        assertEquals(pandigital.pandigitalRange(9876543210L), IntRange.range(0, 9));

    }

    @Test
    public void test_isPalindromic() {
        assertTrue("isPalindromic(0)", Digits.base(10).isPalindromic(0));
        assertTrue("isPalindromic(1)", Digits.base(10).isPalindromic(1));
        assertTrue("isPalindromic(11)", Digits.base(10).isPalindromic(11));
        assertTrue("isPalindromic(121)", Digits.base(10).isPalindromic(121));
        assertTrue("isPalindromic(1221)", Digits.base(10).isPalindromic(1221));
        assertFalse("isPalindromic(1231)", Digits.base(10).isPalindromic(1231));
    }

    @Test
    public void test_differents() {
        assertTrue(Digits.base(10).allDifferents(0));
        assertTrue(Digits.base(10).allDifferents(10));
        assertTrue(Digits.base(10).allDifferents(102));
        assertFalse(Digits.base(10).allDifferents(100));
        assertTrue(Digits.base(10).allDifferents(0, 0));
        assertTrue(Digits.base(10).allDifferents(0, 1));
        assertFalse(Digits.base(10).allDifferents(0, 2));
        assertTrue(Digits.base(10).allDifferents(1, 2));
        assertFalse(Digits.base(10).allDifferents(10, 3));
        assertTrue(Digits.base(10).allDifferents(1, 1));
    }

    @Test
    public void test_reverse() {
        assertEquals("reverse(0)", Digits.base(10).reverse(0), 0);
        assertEquals("reverse(1230)", Digits.base(10).reverse(1230), 321);
        assertEquals("reverse(1000)", Digits.base(10).reverse(1000), 1);
        assertEquals("reverse(9999)", Digits.base(10).reverse(9999), 9999);
    }

    @Test
    public void test_rotate() {
        assertEquals(Digits.base(10).rotate(1234, 0), 1234);
        assertEquals(Digits.base(10).rotate(1234, 1), 4123);
        assertEquals(Digits.base(10).rotate(1234, 2), 3412);
        assertEquals(Digits.base(10).rotate(1234, 3), 2341);
        assertEquals(Digits.base(10).rotate(1234, 4), 1234);
        assertEquals(Digits.base(10).rotate(1234, 5), 4123);
        assertEquals(Digits.base(10).rotate(1234, -1), 2341);
        assertEquals(Digits.base(10).rotate(1234, -2), 3412);
        assertEquals(Digits.base(10).rotate(1234, -3), 4123);
        assertEquals(Digits.base(10).rotate(1234, -4), 1234);
        assertEquals(Digits.base(10).rotate(1234, -5), 2341);
    }

    @Test
    public void test_sum() {
        assertEquals(Digits.base(10).sum(9999999999L), 90);
        assertEquals(Digits.base(10).sum(1), 1);
        assertEquals(Digits.base(10).sum(0), 0);
    }

    @Test
    public void test_length_int() {
        assertEquals(Digits.base(10).length(999999998), 9);
        assertEquals(Digits.base(10).length(999999999), 9);
        assertEquals(Digits.base(10).length(1000000000), 10);
        assertEquals(Digits.base(10).length(1), 1);
        assertEquals(Digits.base(10).length(0), 1);
        assertEquals(Digits.base(10).length(999999), 6);
        assertEquals(Digits.base(10).length(99998), 5);
        assertEquals(Digits.base(10).length(99999), 5);
        assertEquals(Digits.base(10).length(10000), 5);
        assertEquals(Digits.base(10).length(10001), 5);
        assertEquals(Digits.base(10).length(9998), 4);
        assertEquals(Digits.base(10).length(9999), 4);
        assertEquals(Digits.base(10).length(1000), 4);
        assertEquals(Digits.base(10).length(1001), 4);
        assertEquals(Digits.base(10).length(100), 3);
        assertEquals(Digits.base(10).length(11), 2);
        assertEquals(Digits.base(10).length(10), 2);
        assertEquals(Digits.base(10).length(9), 1);
    }

    @Test
    public void test_length() {
        assertEquals(Digits.base(10).length(9999999998L), 10);
        assertEquals(Digits.base(10).length(9999999999L), 10);
        assertEquals(Digits.base(10).length(10000000000L), 11);
        assertEquals(Digits.base(10).length(999999998), 9);
        assertEquals(Digits.base(10).length(999999999), 9);
        assertEquals(Digits.base(10).length(1000000000), 10);
        assertEquals(Digits.base(10).length(1), 1);
        assertEquals(Digits.base(10).length(0), 1);
        assertEquals(Digits.base(10).length(10000), 5);
        assertEquals(Digits.base(10).length(1000), 4);
        assertEquals(Digits.base(10).length(100), 3);
        assertEquals(Digits.base(10).length(10), 2);
        assertEquals(Digits.base(10).length(10000L), 5);
        assertEquals(Digits.base(10).length(1000L), 4);
        assertEquals(Digits.base(10).length(100L), 3);
        assertEquals(Digits.base(10).length(10L), 2);
    }

    @Test
    public void test_list() {
        assertEquals(Digits.base(10).list(9999999999L).toString(), "{9, 9, 9, 9, 9, 9, 9, 9, 9, 9}");
        assertEquals(Digits.base(10).list(1234567890).toString(), "{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}");
    }

    @Test
    public void test_rebase() {
        assertEquals(Digits.base(2).rebase(0), 0);
        assertEquals(Digits.base(2).rebase(1), 1);
        assertEquals(Digits.base(2).rebase(2), 10);
        assertEquals(Digits.base(8).rebase(8), 10);
    }

    @Test
    public void test_sort() {
        assertEquals(Digits.base(10).sort(0), 0);
        assertEquals(Digits.base(10).sort(1), 1);
        assertEquals(Digits.base(10).sort(2), 2);
        assertEquals(Digits.base(10).sort(10), 1);
        assertEquals(Digits.base(10).sort(11), 11);
        assertEquals(Digits.base(10).sort(5678), 5678);
        assertEquals(Digits.base(10).sort(9883442), 2344889);
        assertEquals(Digits.base(10).sort(900002), 29);
        assertEquals(Digits.base(10).sort(9002), 29);
        assertEquals(Digits.base(10).sort(900200), 29);
    }

    @Test
    public void test_signature() {
        assertArrayEquals(Digits.base(10).signature(0).internalArray(), new int[]{0});
        assertArrayEquals(Digits.base(10).signature(1).internalArray(), new int[]{1});
        assertArrayEquals(Digits.base(10).signature(2).internalArray(), new int[]{2});
        assertArrayEquals(Digits.base(10).signature(10).internalArray(), new int[]{0, 1});
        assertArrayEquals(Digits.base(10).signature(11).internalArray(), new int[]{1, 1});
        assertArrayEquals(Digits.base(10).signature(5678).internalArray(), new int[]{5, 6, 7, 8});
        assertArrayEquals(Digits.base(10).signature(9883442).internalArray(), new int[]{2, 3, 4, 4, 8, 8, 9});
        assertArrayEquals(Digits.base(10).signature(900002).internalArray(), new int[]{0, 0, 0, 0, 2, 9});
        assertArrayEquals(Digits.base(10).signature(92).internalArray(), new int[]{2, 9});
        assertArrayEquals(Digits.base(10).signature(902).internalArray(), new int[]{0, 2, 9});
        assertArrayEquals(Digits.base(10).signature(920).internalArray(), new int[]{0, 2, 9});
        assertArrayEquals(Digits.base(10).signature(92000).internalArray(), new int[]{0, 0, 0, 2, 9});
    }

    @Test
    public void test_concat() {
        assertEquals(Digits.base(10).concatInt(0, 1, 2, 3), 123);
        assertEquals(Digits.base(10).concatInt(0, 12, 233, 677), 12233677);
        assertEquals(Digits.base(10).concatInt(0, 0, 1, 0, 0, 1, 0, 0), 100100);
    }

    @Test
    public void test_concat_perf() {
        Digits digits = Digits.base(10);
        final int MAX = 2222;
        long time = System.currentTimeMillis();
        for (int j = 0; j < MAX; j++)
            for (int i = 0; i < MAX; i++)
                digits.concatInt(i, j);
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int j = 0; j < MAX; j++)
            for (int i = 0; i < MAX; i++)
                Integer.parseInt(i + "" + j);
        System.out.println(System.currentTimeMillis() - time);
    }
}