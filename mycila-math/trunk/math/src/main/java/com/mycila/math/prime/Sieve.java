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
package com.mycila.math.prime;

import com.mycila.math.list.IntSequence;
import com.mycila.math.list.ReadOnlySequenceIterator;
import com.mycila.math.range.IntRange;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Mathieu Carbou
 */
public final class Sieve {

    private int[] primes;
    private final IntRange sieveRange;
    private final Iterable<Integer> iterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return ReadOnlySequenceIterator.on(primes);
        }
    };

    private Sieve(IntRange sieveRange, int[] primes) {
        this.sieveRange = sieveRange;
        this.primes = primes;
    }

    /**
     * Grow the sieve up to given number
     *
     * @param number The number to extend the sieve to
     * @return A new extended sieve
     */
    public Sieve growTo(int number) {
        if ((number & 1) == 0) number--;
        if (sieveRange.to >= number) return this;
        int maxNum = limit();
        if (number < maxNum) maxNum = number;
        final int[] newPrimes = new int[Primes.getPiHighBound(number) - primes.length];
        int newPrimesPos = 0;
        int p = primes[primes.length - 1] + 2;
        for (; p <= maxNum; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length))
                newPrimes[newPrimesPos++] = p;
        for (; p <= number; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length)
                    && PrimaltyTest.isPrime(p, newPrimes, newPrimesPos))
                newPrimes[newPrimesPos++] = p;
        final int[] array = Arrays.copyOf(primes, primes.length + newPrimesPos);
        System.arraycopy(newPrimes, 0, array, primes.length, newPrimesPos);
        return new Sieve(sieveRange.extendTo(number), array);
    }

    /**
     * Extend this Sieve to add more primes into.
     *
     * @param numberOfPrimesToAdd Number of primes to add to this sieve
     * @return The new extended sieve.
     */
    public Sieve grow(int numberOfPrimesToAdd) {
        int pos = primes.length;
        final int max = pos + numberOfPrimesToAdd;
        final int[] newPrimes = Arrays.copyOf(primes, max);
        final int maxNum = limit();
        int p = primes[primes.length - 1] + 2;
        for (; p < maxNum && pos < max; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length))
                newPrimes[pos++] = p;
        for (p = maxNum + 2; pos < max; p += 2)
            if (PrimaltyTest.isPrime(p, newPrimes, pos))
                newPrimes[pos++] = p;
        return new Sieve(sieveRange.extendTo(newPrimes[pos - 1]), newPrimes);
    }

    private int limit() {
        if (primes.length == 0) throw new IllegalStateException("Sieve is empty !");
        final long tmp = (long) primes[primes.length - 1] * (long) primes[primes.length - 1];
        return tmp > Integer.MAX_VALUE || tmp < 0 ? Integer.MAX_VALUE : (int) tmp;
    }

    /**
     * Check wheter the number is prime.
     * <p/>
     * Implementation note:<br>
     * - if the number is contained in the sieve range, check if the sieve contains the number.<br>
     * - If number <= last()^2, check is some primes of this sieve divides the number
     * - Otherwise, if the number is greater than last()^2, the Miller-Rabin test is executed
     *
     * @param number Numberto check
     * @return True if it is prime
     */
    public boolean isPrime(int number) {
        if (sieveRange.contains(number))
            return Arrays.binarySearch(primes, number) >= 0;
        if (number <= limit())
            return PrimaltyTest.isPrime(number, primes, primes.length);
        return PrimaltyTest.millerRabin(number);
    }

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Primorial">Primorial</a> for all primes in this sieve
     *
     * @return The product of all primes in this Sieve
     */
    public BigInteger primorial() {
        return primorial(sieveRange);
    }

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Primorial">Primorial</a> for all primes from this Sieve contained in the provided range.
     *
     * @param range Th range of prime to include
     * @return The product of all primes in this Sieve matching given range
     */
    public BigInteger primorial(IntRange range) {
        range = primeIndexes(range);
        BigInteger prd = BigInteger.ONE;
        if (range.isEmpty()) return prd;
        for (int i = range.from; i <= range.to; i++)
            prd = prd.multiply(BigInteger.valueOf(primes[i]));
        return prd;
    }

    /**
     * The number of prime in the sieve
     *
     * @return The number of prime in the sieve
     */
    public int size() {
        return primes.length;
    }

    /**
     * The range of numbers used to create the sieve
     *
     * @return A range
     */
    public IntRange range() {
        return sieveRange;
    }

    /**
     * Get a prime
     *
     * @param pos Prime index in the sieve
     * @return the prime
     */
    public int get(int pos) {
        return primes[pos];
    }

    /**
     * Check wheter the Sieve contains a prime
     *
     * @param prime The prime
     * @return True if the Sieve contains the prime
     */
    public boolean contains(int prime) {
        return Arrays.binarySearch(primes, prime) >= 0;
    }

    /**
     * Get the first prime of this sieve
     *
     * @return the first prime of this sieve
     */
    public int first() {
        return primes[0];
    }

    /**
     * Get the last prime of this sieve
     *
     * @return the last prime of this sieve
     */
    public int last() {
        return primes[primes.length - 1];
    }

    /**
     * Get the index of a prime
     *
     * @param number Prime number
     * @return the position of the prime in the sieve, starting from 0, or -1 if not found
     */
    public int indexOf(int number) {
        final int pos = Arrays.binarySearch(primes, number);
        return pos >= 0 ? pos : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sieve sieve = (Sieve) o;
        return Arrays.equals(primes, sieve.primes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(primes);
    }

    @Override
    public String toString() {
        return Arrays.toString(primes);
    }

    /**
     * Returns an iterable over this prime to be used in for loops
     *
     * @return This Sieve's iterable
     */
    public Iterable<Integer> iterable() {
        return iterable;
    }

    /**
     * Returns an iterable over the primes of this Sieve which are included in the given range
     *
     * @param range The range
     * @return An iterable to be used in for loops
     */
    public Iterable<Integer> iterable(final IntRange range) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(primeIndexes(range), primes);
            }
        };
    }

    /**
     * Create an {@link com.mycila.math.list.IntSequence} from the prime numbers of this Sieve
     *
     * @return an {@link com.mycila.math.list.IntSequence} containing all primes in order
     */
    public IntSequence asSequence() {
        return new IntSequence(primes);
    }

    /**
     * Create an {@link com.mycila.math.list.IntSequence} from the prime numbers of this Sieve which are included in given range
     *
     * @param range The range of prime to select
     * @return an {@link com.mycila.math.list.IntSequence} containing all primes in order which are contained in the range
     */
    public IntSequence asSequence(IntRange range) {
        range = primeIndexes(range);
        if (range.isEmpty()) return new IntSequence(0);
        IntSequence seq = new IntSequence(range.length());
        seq.appendFrom(primes, range.from, range.length());
        return seq;
    }

    private IntRange primeIndexes(IntRange range) {
        if (primes.length == 0 || range.isEmpty()) return IntRange.empty();
        int start = Arrays.binarySearch(primes, range.from);
        if (start < 0) start = -start - 1;
        if (start >= primes.length) return IntRange.range(primes.length - 1, primes.length - 1);
        int end = Arrays.binarySearch(primes, range.to);
        if (end < 0) end = -end - 2;
        return end >= primes.length ?
                IntRange.range(start, primes.length - 1) :
                IntRange.range(start, end);
    }

    /**
     * Build a Sieve up to this number.
     *
     * @param max The maxmimu range for this sieve
     * @return The sieve
     */
    public static Sieve to(int max) {
        IntRange range = IntRange.range(1, max);
        return new Sieve(range, buildPrimes(range));
    }

    private static int[] buildPrimes(IntRange sieveRange) {
        if (sieveRange.to < 2) return new int[0];
        final boolean[] composite = Primes.sieveOfEratosthenes(sieveRange.to);
        int[] primes = new int[Primes.getPiHighBound(sieveRange.to)];
        boolean toggle = false;
        int p = 5, i = 0, j = 0;
        if (sieveRange.contains(2)) primes[j++] = 2;
        if (sieveRange.contains(3)) primes[j++] = 3;
        while (p <= sieveRange.to) {
            if (!composite[i++] && sieveRange.contains(p))
                primes[j++] = p;
            p += (toggle = !toggle) ? 2 : 4;
        }
        return j != primes.length ? Arrays.copyOf(primes, j) : primes;
    }

}
