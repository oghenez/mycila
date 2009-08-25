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

import com.mycila.combination.Combinations;
import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Mathieu Carbou
 */
public final class CombinationsTest {

    @Test
    public void test_get() {
        assertEquals(Combinations.catalan(0), 1);
        assertEquals(Combinations.catalan(1), 1);
        assertEquals(Combinations.catalan(2), 2);
        assertEquals(Combinations.catalan(3), 5);
        assertEquals(Combinations.catalan(4), 14);
        assertEquals(Combinations.catalan(13), 742900);
    }

    @Test
    public void test_binomial() {
        assertEquals(Combinations.binomial(1, 1), 1);
        assertEquals(Combinations.binomial(4, 1), 4);
        assertEquals(Combinations.binomial(4, 4), 1);
        assertEquals(Combinations.binomial(4, 3), 4);
        assertEquals(Combinations.binomial(23, 2), 253);
        assertEquals(Combinations.binomial(BigInteger.valueOf(1), BigInteger.valueOf(1)), new BigInteger("1"));
        assertEquals(Combinations.binomial(BigInteger.valueOf(4), BigInteger.valueOf(1)), new BigInteger("4"));
        assertEquals(Combinations.binomial(BigInteger.valueOf(4), BigInteger.valueOf(4)), new BigInteger("1"));
        assertEquals(Combinations.binomial(BigInteger.valueOf(4), BigInteger.valueOf(3)), new BigInteger("4"));
        assertEquals(Combinations.binomial(BigInteger.valueOf(23), BigInteger.valueOf(2)), new BigInteger("253"));
    }

    @Test
    public void test_iterate() {
        int count = 0;
        System.out.println("C(0, 0)");
        for (int[] positions : Combinations.combinations(0, 0)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(0, 1)");
        count = 0;
        for (int[] positions : Combinations.combinations(0, 1)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(1, 0)");
        count = 0;
        for (int[] positions : Combinations.combinations(1, 0)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(1, 1)");
        count = 0;
        for (int[] positions : Combinations.combinations(1, 1)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(2, 0)");
        count = 0;
        for (int[] positions : Combinations.combinations(2, 0)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(2, 1)");
        count = 0;
        for (int[] positions : Combinations.combinations(2, 1)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(2, 2)");
        count = 0;
        for (int[] positions : Combinations.combinations(2, 2)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(4, 2)");
        count = 0;
        for (int[] positions : Combinations.combinations(4, 2)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(5, 3)");
        count = 0;
        for (int[] positions : Combinations.combinations(5, 3)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }

        System.out.println("C(5, 1)");
        count = 0;
        for (int[] positions : Combinations.combinations(5, 1)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }
        System.out.println("C(5, 4)");
        count = 0;
        for (int[] positions : Combinations.combinations(5, 4)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }
    }

}