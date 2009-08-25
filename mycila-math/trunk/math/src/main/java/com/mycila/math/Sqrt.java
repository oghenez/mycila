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

import java.math.BigInteger;
import static java.math.BigInteger.*;

/**
 * @author Mathieu Carbou
 */
public final class Sqrt {

    private Sqrt() {
    }

    public static int sqrtInt(int number) {
        return (int) Math.sqrt(number);
    }

    public static long sqrtInt(long number) {
        return (long) Math.sqrt(number);
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     *
     * @param number A positive number
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>2</sup> + r = number</code>.
     */
    public static BigInteger[] sqrtInt(BigInteger number) {
        if (number.signum() == 0 || number.equals(ONE)) return new BigInteger[]{number, ZERO};
        BigInteger lastGuess = ZERO;
        BigInteger guess = ONE.shiftLeft(number.bitLength() >> 1);
        BigInteger test = lastGuess.subtract(guess);
        BigInteger reminder = number.subtract(guess.pow(2));
        while (test.signum() != 0 && !test.equals(ONE) || reminder.signum() < 0) {
            lastGuess = guess;
            guess = number.divide(guess).add(lastGuess).shiftRight(1);
            test = lastGuess.subtract(guess);
            reminder = number.subtract(guess.pow(2));
        }
        return new BigInteger[]{guess, reminder};
    }

}
