package com.mycila.math.concurrent;

import com.mycila.math.number.BigInt;

import java.util.concurrent.Future;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MultiplyOperation extends Operation<BigInt> {
    Future<BigInt> multiply(BigInt a, BigInt b);

    int size();
}
