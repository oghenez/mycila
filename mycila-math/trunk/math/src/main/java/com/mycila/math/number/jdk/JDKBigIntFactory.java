package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;
import com.mycila.math.number.BigIntFactory;

import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntFactory implements BigIntFactory {

    private static final int MAX = 4096;
    private static final JDKBigInt[] CACHE = new JDKBigInt[MAX];

    static {
        for (int i = 0; i < MAX; i++)
            CACHE[i] = new JDKBigInt(java.math.BigInteger.valueOf(i), 10);
    }

    @Override
    public BigInt create(int number) {
        return (number >>> 12) == 0 ?
                CACHE[number] :
                new JDKBigInt(java.math.BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(long number) {
        return new JDKBigInt(java.math.BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(String number) {
        return new JDKBigInt(new java.math.BigInteger(number, 10), 10);
    }

    @Override
    public BigInt create(String number, int radix) {
        return new JDKBigInt(new java.math.BigInteger(number, radix), radix);
    }

    @Override
    public BigInt wrap(Object internal) {
        if (!(internal instanceof java.math.BigInteger))
            throw new IllegalArgumentException("Requires " + java.math.BigInteger.class.getName());
        return new JDKBigInt((java.math.BigInteger) internal, 10);
    }

    @Override
    public BigInt random(int length) {
        return new JDKBigInt(new java.math.BigInteger(length, new Random()), 10);
    }
}