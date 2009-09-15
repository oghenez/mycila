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

import static com.mycila.math.number.BigInt.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PrimesTest {

    @Test
    public void test_product() {
        assertEquals(Primes.product(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 0, 0), big(1));
        assertEquals(Primes.product(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 0, 1), big(1));
        assertEquals(Primes.product(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 4, 1), big(5));
        assertEquals(Primes.product(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 4, 2), big(30));
        assertEquals(Primes.product(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 0, 10), big(3628800));
        assertEquals(Primes.product(new int[]{1, 2, Integer.MAX_VALUE, 4, 5, 6, Integer.MAX_VALUE, 8, 9, 10}, 1, 7), big("8854437147134247569280"));
    }

    @Test
    public void test_sieveOfEratosthenes() {
        assertEquals(Primes.sieveOfEratosthenes(2).toString(), "{}");
        assertEquals(Primes.sieveOfEratosthenes(3).toString(), "{7, 10, 15, 17, 20, 24, 27, 29, 30, 37, 38, 39, 40, 43, 46, 47, 50, 52, 55, 57, 60, 61}");
        assertEquals(Primes.sieveOfEratosthenes(4).toString(), "{7, 10, 15, 17, 20, 24, 27, 29, 30, 37, 38, 39, 40, 43, 46, 47, 50, 52, 55, 57, 60, 61}");
        assertEquals(Primes.sieveOfEratosthenes(5).toString(), "{7, 10, 15, 17, 20, 24, 27, 29, 30, 37, 38, 39, 40, 43, 46, 47, 50, 52, 55, 57, 60, 61}");
        assertEquals(Primes.sieveOfEratosthenes(20).toString(), "{7, 10, 15, 17, 20, 24, 27, 29, 30, 37, 38, 39, 40, 43, 46, 47, 50, 52, 55, 57, 60, 61}");
        assertEquals(Primes.sieveOfEratosthenes(30).toString(), "{7, 10, 15, 17, 20, 24, 27, 29, 30, 37, 38, 39, 40, 43, 46, 47, 50, 52, 55, 57, 60, 61}");
    }

}