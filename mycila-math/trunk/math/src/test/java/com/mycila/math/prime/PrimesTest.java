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

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Mathieu Carbou
 */
public final class PrimesTest {

    @Test
    public void test_sieveOfEratosthenes() {
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(2)), "[]");
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(3)), "[false]");
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(4)), "[false]");
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(5)), "[false]");
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(20)), "[false, false, false, false, false, false]");
        assertEquals(Arrays.toString(Primes.sieveOfEratosthenes(30)), "[false, false, false, false, false, false, false, true, false, false]");
    }

}