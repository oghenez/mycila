package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInteger;
import com.mycila.math.number.BigIntegerFactory;

import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntegerFactory implements BigIntegerFactory {

    private static final int MAX = 4096;
    private static final JDKBigInteger[] CACHE = new JDKBigInteger[MAX];

    static {
        for (int i = 0; i < MAX; i++)
            CACHE[i] = new JDKBigInteger(java.math.BigInteger.valueOf(i), 10);
    }

    @Override
    public BigInteger create(int number) {
        return (number >>> 12) == 0 ?
                CACHE[number] :
                new JDKBigInteger(java.math.BigInteger.valueOf(number), 10);
    }

    @Override
    public com.mycila.math.number.BigInteger create(long number) {
        return new JDKBigInteger(java.math.BigInteger.valueOf(number), 10);
    }

    @Override
    public com.mycila.math.number.BigInteger create(String number) {
        return new JDKBigInteger(new java.math.BigInteger(number, 10), 10);
    }

    @Override
    public com.mycila.math.number.BigInteger create(String number, int radix) {
        return new JDKBigInteger(new java.math.BigInteger(number, radix), radix);
    }

    @Override
    public BigInteger wrap(Object internal) {
        if (!(internal instanceof java.math.BigInteger))
            throw new IllegalArgumentException("Requires " + java.math.BigInteger.class.getName());
        return new JDKBigInteger((java.math.BigInteger) internal, 10);
    }

    @Override
    public BigInteger random(int length) {
        return new JDKBigInteger(new java.math.BigInteger(length, new Random()), 10);
    }
}