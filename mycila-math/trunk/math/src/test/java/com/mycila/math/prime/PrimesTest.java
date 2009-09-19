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
import static com.mycila.math.number.BigInt.*;
import com.mycila.math.number.jdk7.BigInteger;
import static org.junit.Assert.*;
import org.junit.Test;

import java.security.SecureRandom;

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
        assertEquals(Primes.product(new int[]{1, 2, Integer.MAX_VALUE, 4, 5, 6, Integer.MAX_VALUE, 8, 9, 10}, 1, 8), big("79689934324208228123520"));
        assertEquals(Primes.product(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 0, 4), big("21267647892944572736998860269687930881"));
        assertEquals(Primes.product(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 0, 9), INT_MAX.pow(9));
    }

    @Test
    public void test_product_perf() {
        SecureRandom random = new SecureRandom();
        int length = 30000;
        int[] numbers = new int[length];
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = random.nextInt() & 0x7FFFFFFF;

        long time = System.currentTimeMillis();
        BigInteger product = BigInteger.ONE;
        for (int number : numbers)
            product = product.multiply(BigInteger.valueOf(number));
        time = System.currentTimeMillis() - time;
        System.out.println(" in " + time);

        time = System.currentTimeMillis();
        BigInt p = Primes.product(numbers, 0, length);
        time = System.currentTimeMillis() - time;
        System.out.println(" in " + time);

        if (product.bitLength() != p.bitLength()) {
            System.out.println("expected: " + product.toString(16));
            System.out.println("current: " + p.toString(16));
            assertEquals(product.bitLength(), p.bitLength());
        }
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