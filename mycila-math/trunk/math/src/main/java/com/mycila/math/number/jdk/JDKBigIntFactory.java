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
package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;
import com.mycila.math.number.BigIntFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntFactory implements BigIntFactory<BigInteger> {

    private static final int MAX = 4096;
    private static final JDKBigInt[] CACHE = new JDKBigInt[MAX];
    private static final Random RANDOM = new SecureRandom();

    static {
        for (int i = 0; i < MAX; i++)
            CACHE[i] = new JDKBigInt(BigInteger.valueOf(i), 10);
    }

    @Override
    public BigInt create(int number) {
        return (number >>> 12) == 0 ?
                CACHE[number] :
                wrap(BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(long number) {
        return (number >>> 12) == 0 ?
                CACHE[((int) number)] :
                wrap(BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(String number) {
        return wrap(new BigInteger(number, 10), 10);
    }

    @Override
    public BigInt create(String number, int radix) {
        return wrap(new BigInteger(number, radix), radix);
    }

    @Override
    public BigInt random(int length) {
        return wrap(new BigInteger(length, RANDOM), 10);
    }

    @Override
    public BigInt wrap(BigInteger internal, int radix) {
        return new JDKBigInt(internal, radix);
    }

}