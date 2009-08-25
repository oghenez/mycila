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

import com.mycila.distribution.Distribution;
import com.mycila.math.list.IntProcedure;
import com.mycila.math.list.IntSequence;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Mathieu Carbou
 */
public final class Digits {

    private final int base;
    private final BigInteger bigBase;

    private Digits(int base) {
        this.base = base;
        this.bigBase = BigInteger.valueOf(base);
    }

    /**
     * Check wheter the digits of a number are all differents
     *
     * @param number The number
     * @return the true if all digits are different
     */
    public boolean allDifferents(long number) {
        int bitset = 0;
        for (; number > 0; number /= base) {
            int digit = (int) (number % base);
            int bit = 1 << digit;
            if ((bitset & bit) != 0) return false;
            bitset |= bit;
        }
        return true;
    }

    /**
     * Check wheter the digits of a number are all differents for a given length.
     * <p/>
     * In example, for length 5, the number 123 has two leading 0.
     *
     * @param number The number
     * @param length The length to check for.
     * @return the true if all digits are different
     */
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

    /**
     * Convert a number to this base. In example, Digits.base(2).rebase(10) gives 1010
     *
     * @param number The number
     * @return the representation of the number in this base
     */
    public long rebase(long number) {
        long num = 0;
        for (int p = 1; number > 0; number /= base, p *= 10)
            num = p * (number % base) + num;
        return num;
    }

    /**
     * Convert a number to this base. In example, Digits.base(2).rebase(10) gives 1010
     *
     * @param number The number
     * @return the representation of the number in this base
     */
    public int rebase(int number) {
        int num = 0;
        for (int p = 1; number > 0; number /= base, p *= 10)
            num = p * (number % base) + num;
        return num;
    }

    /**
     * Convert a number to this base. In example, Digits.base(2).rebase(10) gives 1010
     *
     * @param number The number
     * @return the representation of the number in this base
     */
    public BigInteger rebase(BigInteger number) {
        BigInteger num = BigInteger.ZERO;
        for (BigInteger p = BigInteger.ONE; number.signum() > 0; p = p.multiply(BigInteger.TEN)) {
            final BigInteger qr[] = number.divideAndRemainder(bigBase);
            num = num.add(p.multiply(qr[1]));
            number = qr[0];
        }
        return num;
    }

    /**
     * List all digits of a number in this base, in descending order of powers.
     * <p/>
     * I.e. 123 gives {1, 2, 3}
     *
     * @param number The number
     * @return the list of digits
     */
    public IntSequence list(long number) {
        IntSequence list = new IntSequence();
        do list.add((int) (number % base));
        while ((number /= base) > 0);
        list.reverse();
        return list;
    }

    /**
     * List all digits of a number in this base, in descending order of powers.
     * <p/>
     * I.e. 123 gives {1, 2, 3}
     *
     * @param number The number
     * @return the list of digits
     */
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

    /**
     * Returns the digit sum of a number in this base
     *
     * @param number The number
     * @return the digit sum
     */
    public int sum(long number) {
        int sum = 0;
        do sum += number % base;
        while ((number /= base) > 0);
        return sum;
    }

    /**
     * Returns the digit sum of a number in this base
     *
     * @param number The number
     * @return the digit sum
     */
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

    /**
     * Reverse the digits of a number
     *
     * @param number The number
     * @return the reversed number
     */
    public int reverse(int number) {
        int reverse = 0;
        while (number != 0) {
            reverse = reverse * base + number % base;
            number /= base;
        }
        return reverse;
    }

    /**
     * Reverse the digits of a number
     *
     * @param number The number
     * @return the reversed number
     */
    public long reverse(long number) {
        long reverse = 0;
        while (number != 0) {
            reverse = reverse * base + number % base;
            number /= base;
        }
        return reverse;
    }

    /**
     * Reverse the digits of a number
     *
     * @param number The number
     * @return the reversed number
     */
    public BigInteger reverse(BigInteger number) {
        BigInteger reverse = BigInteger.ZERO;
        while (number.signum() == 1) {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            reverse = reverse.multiply(bigBase).add(qr[1]);
            number = qr[0];
        }
        return reverse;
    }

    /**
     * Rotate digits of a number.<br>
     * - The rotation direction is specified by the sign of offset<br>
     * - The rotation length is determined by the value of the offset
     * <p/>
     * I.e., rotate(1234, 3) and rotate(1234, -1) will both give 2341
     *
     * @param number Number to rotate
     * @param offset The direction and length of the rotation
     * @return The rotated number
     */
    public int rotate(int number, int offset) {
        final int len = length(number);
        offset %= len;
        if (offset == 0) return number;
        if (offset < 0) offset = len + offset;
        final int mask = (int) Math.pow(base, offset);
        return number / mask + (number % mask) * (int) Math.pow(base, len - offset);
    }

    /**
     * Rotate digits of a number.<br>
     * - The rotation direction is specified by the sign of offset<br>
     * - The rotation length is determined by the value of the offset
     * <p/>
     * I.e., rotate(1234, 3) and rotate(1234, -1) will both give 2341
     *
     * @param number Number to rotate
     * @param offset The direction and length of the rotation
     * @return The rotated number
     */
    public long rotate(long number, int offset) {
        final int len = length(number);
        offset %= len;
        if (offset == 0) return number;
        if (offset < 0) offset = len + offset;
        final long mask = (long) Math.pow(base, offset);
        return number / mask + (number % mask) * (long) Math.pow(base, len - offset);
    }

    /**
     * Rotate digits of a number.<br>
     * - The rotation direction is specified by the sign of offset<br>
     * - The rotation length is determined by the value of the offset
     * <p/>
     * I.e., rotate(1234, 3) and rotate(1234, -1) will both give 2341
     *
     * @param number Number to rotate
     * @param offset The direction and length of the rotation
     * @return The rotated number
     */
    public BigInteger rotate(BigInteger number, int offset) {
        final int len = length(number);
        offset %= len;
        if (offset == 0) return number;
        if (offset < 0) offset = len + offset;
        final BigInteger mask = bigBase.pow(offset);
        return number.divide(mask).add((number.mod(mask).multiply(bigBase.pow(len - offset))));
    }

    /**
     * Executes a callback for each digit of the number
     *
     * @param number    The number
     * @param procedure The callback to run for each digit
     * @return True if all digits have been processed.
     *         The callback can return false at any time to stop processing.
     */
    public boolean each(long number, IntProcedure procedure) {
        do if (!procedure.execute((int) (number % base))) return false;
        while ((number /= base) > 0);
        return true;
    }

    /**
     * Executes a callback for each digit of the number
     *
     * @param number    The number
     * @param procedure The callback to run for each digit
     * @return True if all digits have been processed.
     *         The callback can return false at any time to stop processing.
     */
    public boolean each(BigInteger number, IntProcedure procedure) {
        do {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            if (!procedure.execute(qr[1].intValue())) return false;
            number = qr[0];
        }
        while (number.signum() > 0);
        return true;
    }

    /**
     * Returns the distribution (frequencies) of the digits on this number
     *
     * @param number The number
     * @return a {@link com.mycila.distribution.Distribution} containing all digit frequencies for this number
     */
    public Distribution<Integer> map(long number) {
        final Distribution<Integer> distribution = Distribution.of(Integer.class);
        do {
            distribution.add((int) (number % base));
            number /= base;
        } while (number > 0);
        return distribution;
    }

    /**
     * Returns the distribution (frequencies) of the digits on this number
     *
     * @param number The number
     * @return a {@link com.mycila.distribution.Distribution} containing all digit frequencies for this number
     */
    public Distribution<Integer> map(BigInteger number) {
        final Distribution<Integer> distribution = Distribution.of(Integer.class);
        do {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            distribution.add(qr[1].intValue());
            number = qr[0];
        } while (number.signum() > 0);
        return distribution;
    }

    /**
     * Get the number length. Note: 0 as a length of 1.
     *
     * @param number The number
     * @return Its length
     */
    public int length(long number) {
        if (number == 0) return 1;
        int count = 0;
        for (; number > 0; number /= base) count++;
        return count;
    }

    /**
     * Get the number length. Note: 0 as a length of 1.
     *
     * @param number A positive number
     * @return Its length
     */
    public int length(BigInteger number) {
        return number.toString(base).length();
    }

    /**
     * Concatenate positive numbers
     *
     * @param number  The starting number
     * @param numbers Other numbers to concatenate
     * @return The concatenated number, on 32 bits
     */
    public int concatInt(int number, int... numbers) {
        final StringBuilder sb = new StringBuilder().append(number);
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return Integer.parseInt(sb.toString());
    }

    /**
     * Concatenate positive numbers
     *
     * @param number  The starting number
     * @param numbers Other numbers to concatenate
     * @return The concatenated number, on 64 bits
     */
    public long concatLong(long number, long... numbers) {
        final StringBuilder sb = new StringBuilder().append(number);
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return Long.parseLong(sb.toString());
    }

    /**
     * Concatenate positive numbers
     *
     * @param number  The starting number
     * @param numbers Other numbers to concatenate
     * @return The concatenated number
     */
    public BigInteger concat(BigInteger number, BigInteger... numbers) {
        final StringBuilder sb = new StringBuilder().append(number);
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return new BigInteger(sb.toString());
    }

    /**
     * Sort the digits of a number
     *
     * @param number The number
     * @return Another number with the same digits, sorted
     */
    public int sort(int number) {
        final int[] digits = new int[length(number)];
        int i = 0;
        do digits[i++] = number % base;
        while ((number /= base) > 0);
        Arrays.sort(digits);
        number = 0;
        for (int digit : digits)
            number = base * number + digit;
        return number;
    }

    /**
     * Sort the digits of a number
     *
     * @param number The number
     * @return Another number with the same digits, sorted
     */
    public long sort(long number) {
        final int[] digits = new int[length(number)];
        int i = 0;
        do digits[i++] = (int) (number % base);
        while ((number /= base) > 0);
        Arrays.sort(digits);
        number = 0;
        for (int digit : digits)
            number = base * number + digit;
        return number;
    }

    /**
     * Sort the digits of a number
     *
     * @param number The number
     * @return Another number with the same digits, sorted
     */
    public BigInteger sort(BigInteger number) {
        final char c[] = number.toString(base).toCharArray();
        Arrays.sort(c);
        return new BigInteger(String.valueOf(c), 10);
    }

    /**
     * Returns the signature of a number. The signature is composed of all the digit of the number, sorted.
     *
     * @param number The number
     * @return Its digit list
     */
    public IntSequence signature(long number) {
        final IntSequence digits = new IntSequence(length(number));
        do digits.addQuick((int) (number % base));
        while ((number /= base) > 0);
        return digits.sort();
    }

    /**
     * Returns the signature of a number. The signature is composed of all the digit of the number, sorted.
     *
     * @param number The number
     * @return Its digit list
     */
    public IntSequence signature(BigInteger number) {
        final char chars[] = number.toString(base).toCharArray();
        final int len = chars.length;
        Arrays.sort(chars);
        final int[] digits = new int[len];
        for (int i = 0; i < len; i++)
            digits[i] = chars[i] - 48;
        return new IntSequence(digits);
    }

    /**
     * Check wheter the given numbers are permutations of tehir digits
     *
     * @param number  The first number
     * @param numbers Other number to be checked against
     * @return True if all numbers are digit permutations of number
     */
    public boolean arePermutations(final long number, final long... numbers) {
        final IntSequence sign = signature(number);
        for (long n : numbers)
            if (!sign.equals(signature(n)))
                return false;
        return true;
    }

    /**
     * Check wheter the given numbers are permutations of tehir digits
     *
     * @param number  The first number
     * @param numbers Other number to be checked against
     * @return True if all numbers are digit permutations of number
     */
    public boolean arePermutations(final BigInteger number, final BigInteger... numbers) {
        final IntSequence sign = signature(number);
        for (BigInteger n : numbers)
            if (!sign.equals(signature(n)))
                return false;
        return true;
    }

    /**
     * Check if the number is a palyndrom
     *
     * @param number The number
     * @return tru if it is
     */
    public boolean isPalindromic(long number) {
        return number == reverse(number);
    }

    /**
     * Check if the number is a palyndrom
     *
     * @param number The number
     * @return tru if it is
     */
    public boolean isPalindromic(BigInteger number) {
        return number.equals(reverse(number));
    }

    public static Digits base(int base) {
        return new Digits(base);
    }
}
