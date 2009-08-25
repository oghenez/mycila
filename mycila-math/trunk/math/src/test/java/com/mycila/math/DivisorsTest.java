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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class DivisorsTest {

    @Test
    public void test_list() {
        assertEquals(Divisors.list(0).toString(), "{}");
        assertEquals(Divisors.list(1).toString(), "{1}");
        assertEquals(Divisors.list(100).toString(), "{1, 2, 4, 5, 10, 20, 25, 50, 100}");
        assertEquals(Divisors.list(49).toString(), "{1, 7, 49}");
        assertEquals(Divisors.list(60668796879L).toString(), "{1, 3, 9, 27, 43, 129, 387, 1161, 52255639, 156766917, 470300751, 1410902253, 2246992477, 6740977431, 20222932293, 60668796879}");
    }

    @Test
    public void test_sum() {
        assertEquals(Divisors.sum(0), 0);
        assertEquals(Divisors.sum(1), 1);
        assertEquals(Divisors.sum(11), 12);
        assertEquals(Divisors.sum(1221), 1824);
        assertEquals(Divisors.sum(3580), 7560);
        assertEquals(Divisors.sum(70), 144);
        assertEquals(Divisors.sum(8), 15);
    }

    @Test
    public void test_states() {
        assertTrue(Divisors.isPerfect(6));
        assertTrue(Divisors.isAbundant(18));
        assertTrue(Divisors.isDeficient(16));
    }

    @Test
    public void test_gcd() {
        assertEquals(Divisors.gcd(644, 645), 1);
        assertEquals(Divisors.gcd(1000, 100), 100);
        assertEquals(Divisors.gcd(15, 17), 1);
        assertEquals(Divisors.gcd(15, 18), 3);
        assertEquals(Divisors.gcdInt(15, 18, 27, 36, 20), 1);
        assertEquals(Divisors.gcdInt(15, 18, 27, 36), 3);
        assertEquals(Divisors.gcdInt(15, 18, 27), 3);
    }

    @Test
    public void test_lcm() {
        assertEquals(Divisors.lcm(14, 15), 210);
        assertEquals(Divisors.lcmInt(15, 18, 27), 270);
        assertEquals(Divisors.lcm(644, 645), 415380);
        assertEquals(Divisors.lcm(644, 646), 208012);
    }

    @Test
    public void test_findDivisor() {
        assertEquals(Divisors.findDivisor(0), 0);
        assertEquals(Divisors.findDivisor(1), 1);
        assertEquals(Divisors.findDivisor(2), 2);
        assertEquals(Divisors.findDivisor(4), 2);
        assertEquals(Divisors.findDivisor(3), 3);
        assertEquals(Divisors.findDivisor(17), 17);
        assertEquals(Divisors.findDivisor(9), 3);
        assertEquals(Divisors.findDivisor(361), 19);
    }

}
