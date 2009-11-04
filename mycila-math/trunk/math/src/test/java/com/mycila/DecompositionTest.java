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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou
 */
public final class DecompositionTest {

    @Test
    public void test_decomposition() {
        assertEquals("Decomposition.of(0)", Decomposition.of(0).toString(), "0");
        assertEquals("Decomposition.of(1)", Decomposition.of(1).toString(), "1");
        assertEquals("Decomposition.of(2)", Decomposition.of(2).toString(), "2=2^1");
        assertEquals("Decomposition.of(6)", Decomposition.of(6).toString(), "6=2^1*3^1");
        assertEquals("Decomposition.of(18)", Decomposition.of(18).toString(), "18=2^1*3^2");
        assertEquals("Decomposition.of(16)", Decomposition.of(16).toString(), "16=2^4");
        assertEquals("Decomposition.of(28)", Decomposition.of(28).toString(), "28=2^2*7^1");
        assertEquals("Decomposition.of(3580)", Decomposition.of(3580).toString(), "3580=2^2*5^1*179^1");
        assertEquals("Decomposition.of(10000)", Decomposition.of(10000).toString(), "10000=2^4*5^4");
        assertEquals("Decomposition.of(289)", Decomposition.of(289).toString(), "289=17^2");
    }

    @Test
    public void test_factors() {
        assertEquals(Decomposition.of(10000).factors().toString(), "[2, 2, 2, 2, 5, 5, 5, 5]");
    }

    @Test
    public void test_sum() {
        int sum = 0;
        for (int i = 2; i <= 100; i++) {
            System.out.println(Decomposition.of(i));
            for (int prime : Decomposition.of(i).factors())
                if (prime < 20) sum += prime;
        }
        assertEquals(1037, sum);
    }

    @Test
    public void test_decomposition_perf() {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) Decomposition.of(i);
        System.out.println(System.currentTimeMillis() - time);
    }

    @Test
    public void test_divisorCount() {
        assertEquals("divisorCount(0)", Decomposition.of(0).divisorCount(), 0);
        assertEquals("divisorCount(1)", Decomposition.of(1).divisorCount(), 1);
        assertEquals("divisorCount(2)", Decomposition.of(2).divisorCount(), 2);
        assertEquals("divisorCount(289)", Decomposition.of(289).divisorCount(), 3);
        assertEquals("divisorCount(6)", Decomposition.of(6).divisorCount(), 4);
        assertEquals("divisorCount(18)", Decomposition.of(18).divisorCount(), 6);
        assertEquals("divisorCount(16)", Decomposition.of(16).divisorCount(), 5);
        assertEquals("divisorCount(28)", Decomposition.of(28).divisorCount(), 6);
        assertEquals("divisorCount(3580)", Decomposition.of(3580).divisorCount(), 12);
        assertEquals("divisorCount(10000)", Decomposition.of(10000).divisorCount(), 25);
    }

}