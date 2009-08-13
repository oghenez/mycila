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

import com.mycila.sequence.IntSequence;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author Mathieu Carbou
 */
public final class Sieve {

    private int[] primes;

    private Sieve(int min, int max) {
        if (max < 2) primes = new int[0];
        else {
            final int bounds = (max - 1) >> 1;
            final BitSet sieve = new BitSet(bounds + 1);
            for (int i = 1, limit = ((int) Math.sqrt(max) - 1) >> 1; i <= limit; i++)
                if (!sieve.get(i))
                    for (int j = (i * (i + 1)) << 1; j <= bounds; j += 2 * i + 1)
                        sieve.set(j);
            primes = new int[bounds + 1 - sieve.cardinality()];
            int pos = 0;
            if (min <= 2) primes[pos++] = 2;
            for (int i = 1; i <= bounds; i++) {
                if (!sieve.get(i)) {
                    final int prime = (i << 1) + 1;
                    if (prime >= min) primes[pos++] = prime;
                }
            }
            if (pos != primes.length)
                primes = Arrays.copyOf(primes, pos);
        }
    }

    private Sieve(int[] array) {
        this.primes = array;
    }

    public boolean isPrime(int number) {
        return isPrime(number, primes, primes.length);
    }

    public Sieve growTo(int number) {
        final int maxPrime = primes[primes.length - 1];
        if ((number & 1) == 0) number--;
        if (maxPrime >= number) return this;
        int maxNum = maxPrime * maxPrime;
        if (number < maxNum) maxNum = number;
        final IntSequence newPrimes = new IntSequence();
        int p = maxPrime + 2;
        for (; p <= maxNum; p += 2)
            if (isPrime(p, primes, primes.length))
                newPrimes.add(p);
        for (; p <= number; p += 2)
            if (isPrime(p, primes, primes.length)
                    && isPrime(p, newPrimes.internalArray(), newPrimes.size()))
                newPrimes.add(p);
        final int[] array = Arrays.copyOf(primes, primes.length + newPrimes.size());
        newPrimes.copyInto(array, primes.length);
        return new Sieve(array);
    }

    public Sieve grow(int numberOfPrimesToAdd) {
        int pos = primes.length;
        final int max = pos + numberOfPrimesToAdd;
        final int[] newPrimes = Arrays.copyOf(primes, max);
        final int maxPrime = primes[primes.length - 1];
        final int maxNum = maxPrime * maxPrime;
        int p = maxPrime + 2;
        for (; p < maxNum && pos < max; p += 2) {
            if (isPrime(p, primes, primes.length))
                newPrimes[pos++] = p;
        }
        for (p = maxNum + 2; pos < max; p += 2) {
            if (isPrime(p, newPrimes, pos))
                newPrimes[pos++] = p;
        }
        return new Sieve(newPrimes);
    }

    public int size() {
        return primes.length;
    }

    public int get(int pos) {
        return primes[pos];
    }

    public boolean contains(int prime) {
        return Arrays.binarySearch(primes, prime) >= 0;
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

    private static boolean isPrime(int number, int[] primes, int maxIndex) {
        final int sqrtFloor = (int) Math.sqrt(number);
        for (int i = 0; i < maxIndex; i++) {
            final int prime = primes[i];
            if (prime > sqrtFloor) return true;
            if (number % prime == 0) return false;
        }
        return true;
    }

    public static Sieve to(int max) {
        return new Sieve(2, max);
    }

    public static Sieve range(int min, int max) {
        return new Sieve(min, max);
    }

}
