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
package com.mycila;

import com.mycila.distribution.Distribution;
import com.mycila.sequence.IntProcedure;
import com.mycila.sequence.IntSequence;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class Digits {

    private final int base;
    private final BigInteger bigBase;
    private final double logBase;

    private Digits(int base) {
        this.base = base;
        this.bigBase = BigInteger.valueOf(base);
        this.logBase = Math.log(base);
    }

    public boolean allDifferents(long number, int length) {
        int bitset = 0;
        for (; length > 0; number /= base, length--) {
            int digit = (int) (number % base);
            int bit = 1 << digit;
            if ((bitset & bit) != 0) return false;
            bitset |= bit;
        }
        return true;
    }

    public long rebase(long number) {
        long num = 0;
        for (int p = 1; number > 0; number /= base, p *= 10)
            num = p * (number % base) + num;
        return num;
    }

    public int rebase(int number) {
        int num = 0;
        for (int p = 1; number > 0; number /= base, p *= 10)
            num = p * (number % base) + num;
        return num;
    }

    public IntSequence list(long number) {
        IntSequence list = new IntSequence();
        do list.add((int) (number % base));
        while ((number /= base) > 0);
        list.reverse();
        return list;
    }

    public IntSequence list(BigInteger number) {
        IntSequence list = new IntSequence();
        BigInteger qr[];
        do {
            qr = number.divideAndRemainder(bigBase);
            list.add(qr[1].intValue());
        }
        while ((number = qr[0]).signum() == 1);
        list.reverse();
        return list;
    }

    public int sum(BigInteger number) {
        int sum = 0;
        do {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            sum += qr[1].intValue();
            number = qr[0];
        }
        while (number.signum() == 1);
        return sum;
    }

    public int sum(long number) {
        int sum = 0;
        do sum += number % base;
        while ((number /= base) > 0);
        return sum;
    }

    public int reverse(int number) {
        int reverse = 0;
        while (number != 0) {
            reverse = reverse * base + number % base;
            number /= base;
        }
        return reverse;
    }

    public BigInteger reverse(BigInteger number) {
        BigInteger reverse = BigInteger.ZERO;
        while (number.signum() == 1) {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            reverse = reverse.multiply(bigBase).add(qr[1]);
            number = qr[0];
        }
        return reverse;
    }

    public int rotate(int number) {
        int n = number, p = 1;
        while ((n /= base) > 0) p *= base;
        return number / base + p * (number % base);
    }

    public boolean each(int number, IntProcedure procedure) {
        do if (!procedure.execute(number % base)) return false;
        while ((number /= base) > 0);
        return true;
    }

    public Distribution<Integer> map(int number) {
        Distribution<Integer> distribution = Distribution.of(Integer.class);
        do {
            distribution.add(number % base);
            number /= base;
        } while (number > 0);
        return distribution;
    }

    public int length(BigInteger number) {
        int length = 1;
        while ((number = number.divide(bigBase)).signum() == 1) length++;
        return length;
    }

    public int length(long number) {
        return number == 0 ? 1 : (int) (Math.floor(Math.log(number) / logBase)) + 1;
    }

    public int length(int number) {
        return number == 0 ? 1 : (int) (Math.floor(Math.log(number) / logBase)) + 1;
    }

    public int concatInt(int number, int... numbers) {
        final StringBuilder sb = new StringBuilder().append(number);
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return Integer.parseInt(sb.toString());
    }

    public long concatLong(long number, long... numbers) {
        final StringBuilder sb = new StringBuilder().append(number);
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return Long.parseLong(sb.toString());
    }

    public static Digits base(int base) {
        return new Digits(base);
    }
}
