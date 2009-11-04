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

import com.mycila.math.prime.Sieve;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
//TODO: PRIME - from ECM: totient, moebius, factors, sum divisor, divisor count, ...
//TODO: PRIME - create class to decompose BigInts
public final class Decomposition {

    private final int number;
    private final List<Factor> decomp;
    private int divisorCount = -1;
    private String toString;

    private Decomposition(int number, List<Factor> decomp) {
        this.number = number;
        this.decomp = decomp;
    }

    public int factorCount() {
        return decomp.size();
    }

    public List<Factor> decomp() {
        return decomp;
    }

    public int divisorCount() {
        if (divisorCount != -1) return divisorCount;
        if (number == 0) return divisorCount = 0;
        divisorCount = 1;
        for (Factor factor : decomp)
            divisorCount *= factor.exponent() + 1;
        return divisorCount;
    }

    public List<Integer> factors() {
        List<Integer> factors = new ArrayList<Integer>();
        for (Factor factor : decomp)
            for (int i = 0; i < factor.exponent(); i++)
                factors.add(factor.prime());
        return factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decomposition decomposition = (Decomposition) o;
        return number == decomposition.number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        if (toString != null) return toString;
        if (decomp.isEmpty()) return toString = "" + number;
        StringBuilder sb = new StringBuilder().append(number).append("=");
        for (Factor factor : decomp) sb.append(factor).append("*");
        return toString = sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static Decomposition of(final int number) {
        final List<Factor> decomp = new ArrayList<Factor>();
        Sieve sieve = Sieve.to(number);
        int n = number;
        for (int prime : sieve.asArray()) {
            if (n % prime == 0) {
                Factor factor = Factor.valueOf(prime);
                decomp.add(factor);
                while ((n /= prime) % prime == 0)
                    factor.incrementExponent();
            }
        }
        return new Decomposition(number, decomp);
    }

}