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
package com.mycila.math.prime.sieve;

import com.mycila.math.list.IntSequence;
import com.mycila.math.list.ReadOnlySequenceIterator;
import com.mycila.math.prime.PrimaltyTest;
import com.mycila.math.prime.Primes;
import com.mycila.math.range.IntRange;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Mathieu Carbou
 */
public final class IntSieve {

    private int[] primes;
    private final int maxPrime;
    private final IntRange sieveRange;
    private final Iterable<Integer> iterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return ReadOnlySequenceIterator.on(primes);
        }
    };

    private IntSieve(IntRange sieveRange, int[] primes) {
        this.sieveRange = sieveRange;
        this.primes = primes;
        maxPrime = primes.length > 0 ? primes[primes.length - 1] : 0;
    }

    public IntSieve growTo(int number) {
        if ((number & 1) == 0) number--;
        if (sieveRange.to >= number) return this;
        int maxNum = maxPrime * maxPrime;
        if (number < maxNum) maxNum = number;
        final int[] newPrimes = new int[Primes.getPiHighBound(number) - primes.length];
        int newPrimesPos = 0;
        int p = maxPrime + 2;
        for (; p <= maxNum; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length))
                newPrimes[newPrimesPos++] = p;
        for (; p <= number; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length)
                    && PrimaltyTest.isPrime(p, newPrimes, newPrimesPos))
                newPrimes[newPrimesPos++] = p;
        final int[] array = Arrays.copyOf(primes, primes.length + newPrimesPos);
        System.arraycopy(newPrimes, 0, array, primes.length, newPrimesPos);
        return new IntSieve(sieveRange.extendTo(number), array);
    }

    public IntSieve grow(int numberOfPrimesToAdd) {
        int pos = primes.length;
        final int max = pos + numberOfPrimesToAdd;
        final int[] newPrimes = Arrays.copyOf(primes, max);
        final int maxNum = maxPrime * maxPrime;
        int p = maxPrime + 2;
        for (; p < maxNum && pos < max; p += 2)
            if (PrimaltyTest.isPrime(p, primes, primes.length))
                newPrimes[pos++] = p;
        for (p = maxNum + 2; pos < max; p += 2)
            if (PrimaltyTest.isPrime(p, newPrimes, pos))
                newPrimes[pos++] = p;
        return new IntSieve(sieveRange.extendTo(newPrimes[pos - 1]), newPrimes);
    }

    public boolean isPrime(int number) {
        return sieveRange.contains(number) ?
                Arrays.binarySearch(primes, number) >= 0 :
                PrimaltyTest.isPrime(number, primes, primes.length);
    }

    public int size() {
        return primes.length;
    }

    public IntRange range() {
        return sieveRange;
    }

    public int get(int pos) {
        return primes[pos];
    }

    public boolean contains(int prime) {
        return Arrays.binarySearch(primes, prime) >= 0;
    }

    public int first() {
        return primes[0];
    }

    public int last() {
        return primes[primes.length - 1];
    }

    public int indexOf(int number) {
        final int pos = Arrays.binarySearch(primes, number);
        return pos >= 0 ? pos : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntSieve sieve = (IntSieve) o;
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

    public Iterable<Integer> iterable() {
        return iterable;
    }

    public Iterable<Integer> iterable(final IntRange range) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return ReadOnlySequenceIterator.on(primeIndexes(range), primes);
            }
        };
    }

    public IntSequence asSequence() {
        return new IntSequence(primes);
    }

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

    public static IntSieve to(int max) {
        IntRange range = IntRange.range(1, max);
        return new IntSieve(range, buildPrimes(range));
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
