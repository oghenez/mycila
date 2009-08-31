package com.mycila.math.number;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface BigIntFactory<T> {
    BigInt create(int number);

    BigInt create(long number);

    BigInt create(String number);

    BigInt create(String number, int radix);

    BigInt wrap(T internal, int radix);

    BigInt random(int length);
}
