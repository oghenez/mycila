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

import com.mycila.math.number.BigInt;
import static com.mycila.math.number.BigInt.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Product {

    private Product() {
    }

    //TODO: improve perf of multiplication (Karatsuba algorithm, Toom-Cook multiplication, Sch-nhage-Strassen algorithm): see improved BigInteger + see paralewll computing in jscience
    // - http://en.wikipedia.org/wiki/Karatsuba_algorithm
    // - http://en.wikipedia.org/wiki/Sch%C3%B6nhage-Strassen_algorithm
    // - http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication

    public static BigInt product(int[] numbers, int offset, int length) {
        if (offset < 0 || offset + length > numbers.length)
            throw new IllegalArgumentException("Bad offset or length: " + offset + " / " + length);
        if (length < 3) {
            if (length <= 0) return ONE;
            if (length == 1) return big(numbers[offset]);
            return big((long) numbers[offset] * (long) numbers[offset + 1]);
        }
        BigInt product = ONE;
        int i = offset + length - 2;
        // Integer.MAX_VALUE * Integer.MAX_VALUE fits in a long, so we can multiply both at once
        for (; i >= offset; i -= 2)
            product = product.multiply(big((long) numbers[i] * (long) numbers[i + 1]));
        if (i + 1 == offset)
            product = product.multiply(big(numbers[offset]));
        return product;
    }

    public static boolean isPerfectSquare(long n) {
        if (n < 0) return false;
        switch ((int) (n & 0xF)) {
            case 0:
            case 1:
            case 4:
            case 9:
                long tst = (long) Math.sqrt(n);
                return tst * tst == n;
            default:
                return false;
        }
    }

}
