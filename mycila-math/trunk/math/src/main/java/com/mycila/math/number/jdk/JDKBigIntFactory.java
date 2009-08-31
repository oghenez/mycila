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
                new JDKBigInt(BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(long number) {
        return (number >>> 12) == 0 ?
                CACHE[((int) number)] :
                new JDKBigInt(BigInteger.valueOf(number), 10);
    }

    @Override
    public BigInt create(String number) {
        return new JDKBigInt(new BigInteger(number, 10), 10);
    }

    @Override
    public BigInt create(String number, int radix) {
        return new JDKBigInt(new BigInteger(number, radix), radix);
    }

    @Override
    public BigInt wrap(BigInteger internal, int radix) {
        return new JDKBigInt(internal, radix);
    }

    @Override
    public BigInt random(int length) {
        return new JDKBigInt(new BigInteger(length, RANDOM), 10);
    }
}