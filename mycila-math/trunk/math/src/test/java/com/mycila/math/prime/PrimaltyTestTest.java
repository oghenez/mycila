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
package com.mycila.math.prime;

import com.mycila.math.number.BigInt;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PrimaltyTestTest {

    @Test
    public void test_trialDivision() {
        assertFalse(PrimaltyTest.trialDivision(0));
        assertFalse(PrimaltyTest.trialDivision(1));
        assertTrue(PrimaltyTest.trialDivision(2));
        assertTrue(PrimaltyTest.trialDivision(7));
        assertTrue(PrimaltyTest.trialDivision(179));
        assertTrue(PrimaltyTest.trialDivision(Integer.MAX_VALUE)); // 2147483647
        assertFalse(PrimaltyTest.trialDivision(21474483647546L));
    }

    @Test
    public void test_millerRabin() {
        assertFalse(PrimaltyTest.millerRabin(0));
        assertFalse(PrimaltyTest.millerRabin(1));
        assertTrue(PrimaltyTest.millerRabin(2));
        assertTrue(PrimaltyTest.millerRabin(7));
        assertTrue(PrimaltyTest.millerRabin(179));
        assertTrue(PrimaltyTest.millerRabin(Integer.MAX_VALUE)); // 2147483647
        assertTrue(PrimaltyTest.millerRabin(BigInt.big(9223372036854775783L)));
    }

    @Test
    public void test_perf() {
        Sieve sieve = Sieve.to(1000000);

        long time = System.currentTimeMillis();
        for (int prime : sieve.iterable()) assertTrue(PrimaltyTest.millerRabin(prime));
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int prime : sieve.iterable()) assertTrue(PrimaltyTest.millerRabin(prime));
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int prime : sieve.iterable()) assertTrue(PrimaltyTest.trialDivision(prime));
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int prime : sieve.iterable()) assertTrue(PrimaltyTest.trialDivision(prime));
        System.out.println(System.currentTimeMillis() - time);
    }


    @Test
    public void test_lucasLehmer() {
        assertFalse(PrimaltyTest.lucasLehmer(0));
        assertFalse(PrimaltyTest.lucasLehmer(0));
        assertFalse(PrimaltyTest.lucasLehmer(4));
        assertTrue(PrimaltyTest.lucasLehmer(2));
        assertTrue(PrimaltyTest.lucasLehmer(3));
        assertTrue(PrimaltyTest.lucasLehmer(5));
        assertTrue(PrimaltyTest.lucasLehmer(31)); // matches 2^31-1 = Integer.MAX_VALUE
    }

}