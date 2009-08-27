package com.mycila.math;

import com.mycila.math.number.BigInt;
import static com.mycila.math.number.BigInt.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Product {

    private Product() {
    }

    //TODO: improve perf of multiplication (Karatsuba algorithm, Toom–Cook multiplication, Schönhage–Strassen algorithm): see improved BigInteger
    
    public static BigInt productBig(int[] numbers, int offset, int length) {
        if (offset < 0 || offset + length > numbers.length)
            throw new IllegalArgumentException("Bad offset or length: " + offset + " / " + length);
        if (length < 3) {
            if (length <= 0) return one();
            if (length == 1) return big(numbers[offset]);
            return big((long) numbers[offset] * (long) numbers[offset + 1]);
        }
        BigInt product = one();
        int i = offset + length - 2;
        // Integer.MAX_VALUE * Integer.MAX_VALUE fits in a long, so we can multiply both at once
        for (; i >= offset; i -= 2)
            product = product.multiply(big((long) numbers[i] * (long) numbers[i + 1]));
        if (i + 1 == offset)
            product = product.multiply(big(numbers[offset]));
        return product;
    }

}
