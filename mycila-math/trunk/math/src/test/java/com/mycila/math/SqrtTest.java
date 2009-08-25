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
import static java.math.BigInteger.*;

/**
 * @author Mathieu Carbou
 */
public final class SqrtTest {

    @Test
    public void test_sqrtInt_big() {
        assertEquals(Sqrt.sqrtInt(valueOf(0))[0], valueOf(0));
        assertEquals(Sqrt.sqrtInt(valueOf(1))[0], valueOf(1));
        assertEquals(Sqrt.sqrtInt(valueOf(2))[0], valueOf(1));
        assertEquals(Sqrt.sqrtInt(valueOf(3))[0], valueOf(1));
        assertEquals(Sqrt.sqrtInt(valueOf(4))[0], valueOf(2));
        assertEquals(Sqrt.sqrtInt(valueOf(5))[0], valueOf(2));
        assertEquals(Sqrt.sqrtInt(valueOf(Integer.MAX_VALUE))[0], valueOf(46340));
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190521"))[0], new BigInteger("123456789"));
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190521"))[1], ZERO);
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190530"))[0], new BigInteger("123456789"));
        assertEquals(Sqrt.sqrtInt(new BigInteger("15241578750190530"))[1], new BigInteger("9"));
        for (int i = 0; i < 1000000; i++)
            assertEquals("" + i, Sqrt.sqrtInt(BigInteger.valueOf(i))[0], BigInteger.valueOf((int) Math.sqrt(i)));
        for (int i = Integer.MAX_VALUE - 1000000; i < Integer.MAX_VALUE; i++)
            assertEquals("" + i, Sqrt.sqrtInt(BigInteger.valueOf(i))[0], BigInteger.valueOf((int) Math.sqrt(i)));
        for (long i = Long.MAX_VALUE - 1000000; i < Long.MAX_VALUE; i++)
            assertEquals("" + i, Sqrt.sqrtInt(BigInteger.valueOf(i))[0], BigInteger.valueOf((long) Math.sqrt(i)));
    }

}