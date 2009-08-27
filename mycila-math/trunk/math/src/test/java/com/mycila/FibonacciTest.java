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

import com.mycila.math.number.BigInt;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class FibonacciTest {

    private static final long[] expected = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025};

    @Test
    public void test_isFibonaci() {
        for (int i = 0, max = expected.length; i < max; i++) {
            assertTrue(Fibonacci.isFibonaci(expected[i]));
        }
    }

    @Test
    public void test_gen() {
        for (int i = 0, max = expected.length; i < max; i++) {
            assertEquals(Fibonacci.binet(i), expected[i]);
            assertEquals(Fibonacci.iterative(i), BigInt.big(expected[i]));
            assertEquals(Fibonacci.logarithmic(i), BigInt.big(expected[i]));
        }
    }

}