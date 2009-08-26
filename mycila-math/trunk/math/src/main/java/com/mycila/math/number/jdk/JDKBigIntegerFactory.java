package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInteger;
import com.mycila.math.number.BigIntegerFactory;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntegerFactory implements BigIntegerFactory {

    private static final JDKBigInteger ZERO = new JDKBigInteger(java.math.BigInteger.valueOf(0));
    private static final JDKBigInteger ONE = new JDKBigInteger(java.math.BigInteger.valueOf(1));
    private static final JDKBigInteger TWO = new JDKBigInteger(java.math.BigInteger.valueOf(2));
    private static final JDKBigInteger TEN = new JDKBigInteger(java.math.BigInteger.valueOf(10));

    @Override
    public com.mycila.math.number.BigInteger create(long number) {
        return new JDKBigInteger(java.math.BigInteger.valueOf(number));
    }

    @Override
    public com.mycila.math.number.BigInteger create(String number) {
        return new JDKBigInteger(new java.math.BigInteger(number));
    }

    @Override
    public com.mycila.math.number.BigInteger create(String number, int radix) {
        return new JDKBigInteger(new java.math.BigInteger(number, radix));
    }

    @Override
    public BigInteger get(int val) {
        switch (val) {
            case 0:
                return ZERO;
            case 1:
                return ONE;
            case 10:
                return TEN;
            case 2:
                return TWO;
        }
        return create(val);
    }
}