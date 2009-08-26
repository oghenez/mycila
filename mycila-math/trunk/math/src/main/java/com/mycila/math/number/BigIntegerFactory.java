package com.mycila.math.number;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface BigIntegerFactory {
    BigInteger create(long number);
    BigInteger create(String number);
    BigInteger create(String number, int radix);
    BigInteger get(int val);
}
