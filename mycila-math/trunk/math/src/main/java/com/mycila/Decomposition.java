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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
public final class Decomposition {

    private final int number;
    private final List<Factor> decomp = new ArrayList<Factor>();
    private int divisorCount = -1;
    private String toString;

    private Decomposition(int number, Sieve sieve) {
        this.number = number;
        int i = 0;
        for (int max = sieve.size(); i < max && number > 1; i++) {
            final int prime = sieve.get(i);
            if (number < prime) break;
            if (number % prime == 0) {
                final Factor factor = Factor.valueOf(prime);
                decomp.add(factor);
                for (number /= prime; number % prime == 0; number /= prime)
                    factor.incrementExponent();
            }
        }
        // if the number is greater than one, it means that the sieve is not large enough.
        // We try to increase it
        if (number > 1) {
            sieve = sieve.growTo(number);
            for (int max = sieve.size(); i < max && number > 1; i++) {
                final int prime = sieve.get(i);
                if (number < prime) break;
                if (number % prime == 0) {
                    final Factor factor = Factor.valueOf(prime);
                    decomp.add(factor);
                    for (number /= prime; number % prime == 0; number /= prime)
                        factor.incrementExponent();
                }
            }
        }
    }

    public int factorCount() {
        return decomp.size();
    }

    public int divisorCount() {
        if (divisorCount != -1) return divisorCount;
        if (number == 0) return divisorCount = 0;
        divisorCount = 1;
        for (Factor factor : decomp)
            divisorCount *= factor.exponent() + 1;
        return divisorCount;
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

    public static Decomposition of(int number) {
        return new Decomposition(number, Sieve.to(number));
    }

    public static Decomposition of(int number, Sieve sieve) {
        return new Decomposition(number, sieve);
    }

}
