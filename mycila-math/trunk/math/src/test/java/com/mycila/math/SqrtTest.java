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

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class SqrtTest {

    @Test
    public void test_sqrtInt() {
        assertEquals(Sqrt.sqrtInt(0), 0);
        assertEquals(Sqrt.sqrtInt(0L), 0L);
        assertEquals(Sqrt.sqrtInt(1L), 1L);
        assertEquals(Sqrt.sqrtInt(1), 1);
        assertEquals(Sqrt.sqrtInt(3), 1);
        assertEquals(Sqrt.sqrtInt(16), 4);
        assertEquals(Sqrt.sqrtInt(36), 6);
        assertEquals(Sqrt.sqrtInt(10000), 100);
        assertEquals(Sqrt.sqrtInt(Integer.MAX_VALUE), 46340); // 2147483647
        assertEquals(Sqrt.sqrtInt(Long.MAX_VALUE), 3037000499L); // 9223372036854775807

        for (int i = 0; i < 10000000; i++) {
            assertEquals(Sqrt.sqrtInt(i), (int) (Math.sqrt(i)));
        }
        for (long i = 10000000000L; i < 1000999999L; i++) {
            assertEquals(Sqrt.sqrtInt(i), (long) (Math.sqrt(i)));
        }
    }

    @Test
    public void test_sqrtInt_big() {
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(0)), BigInteger.valueOf(0));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(1)), BigInteger.valueOf(1));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(2)), BigInteger.valueOf(1));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(3)), BigInteger.valueOf(1));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(4)), BigInteger.valueOf(2));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(5)), BigInteger.valueOf(2));
        assertEquals(Sqrt.sqrtInt(BigInteger.valueOf(Integer.MAX_VALUE)), BigInteger.valueOf(46340));
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190521")), new BigInteger("123456789"));
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190530")), new BigInteger("123456789"));
    }

    @Test
    public void test_sqrtInt_perf() {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            int a = Sqrt.sqrtInt(i);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (long i = 10000000000L; i < 10000999999L; i++) {
            long a = Sqrt.sqrtInt(i);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            int sqrt = (int) (Math.sqrt(i));
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (long i = 10000000000L; i < 10000999999L; i++) {
            long sqrt = (long) (Math.sqrt(i));
        }
        System.out.println(System.currentTimeMillis() - time);
    }

}