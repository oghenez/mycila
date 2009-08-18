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
        System.out.println("3, 7, 109, 673");
        int count = 0;
        for (int[] positions : Combinations.combinations(4, 2)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }
        count = 0;
        for (int[] positions : Combinations.combinations(4, 3)) {
            System.out.println(count++ + ": " + Arrays.toString(positions));
        }
    }

}