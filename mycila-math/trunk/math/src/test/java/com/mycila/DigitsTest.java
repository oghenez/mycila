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

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class DigitsTest {

    @Test
    public void test_differents() {
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
    public void test_sum() {
        assertEquals(Digits.base(10).sum(9999999999L), 90);
        assertEquals(Digits.base(10).sum(1), 1);
        assertEquals(Digits.base(10).sum(0), 0);
        assertEquals(Digits.base(10).sum(BigInteger.ZERO), 0);
        assertEquals(Digits.base(10).sum(BigInteger.ONE), 1);
        assertEquals(Digits.base(10).sum(BigInteger.valueOf(9999999999L)), 90);
    }

    @Test
    public void test_length() {
        assertEquals(Digits.base(10).length(9999999999L), 10);
        assertEquals(Digits.base(10).length(1), 1);
        assertEquals(Digits.base(10).length(0), 1);
        assertEquals(Digits.base(10).length(BigInteger.ZERO), 1);
        assertEquals(Digits.base(10).length(BigInteger.ONE), 1);
        assertEquals(Digits.base(10).length(BigInteger.valueOf(9999999999L)), 10);
    }

    @Test
    public void test_list() {
        assertEquals(Digits.base(10).list(9999999999L).toString(), "{9, 9, 9, 9, 9, 9, 9, 9, 9, 9}");
        assertEquals(Digits.base(10).list(1234567890).toString(), "{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}");
        assertEquals(Digits.base(10).list(BigInteger.valueOf(9999999999L)).toString(), "{9, 9, 9, 9, 9, 9, 9, 9, 9, 9}");
        assertEquals(Digits.base(10).list(BigInteger.valueOf(1234567890)).toString(), "{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}");
    }

    @Test
    public void test_rebase() {
        assertEquals(Digits.base(2).rebase(0), 0);
        assertEquals(Digits.base(2).rebase(1), 1);
        assertEquals(Digits.base(2).rebase(2), 10);
        assertEquals(Digits.base(8).rebase(8), 10);
    }

}