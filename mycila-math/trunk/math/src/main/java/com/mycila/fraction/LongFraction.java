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
package com.mycila.fraction;

import com.mycila.Divisors;

/**
 * @author Mathieu Carbou
 */
public final class LongFraction {

    private final long numerator;
    private final long denominator;

    private LongFraction(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public long numerator() {
        return numerator;
    }

    public long denominator() {
        return denominator;
    }

    public long toLong() {
        return numerator / denominator;
    }

    public double toDouble() {
        return (double) numerator / (double) denominator;
    }

    public LongFraction simplify() {
        final long gcd = Divisors.gcd(numerator, denominator);
        return gcd == 1 ? this : new LongFraction(numerator / gcd, denominator / gcd);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public static LongFraction valueOf(long number) {
        return new LongFraction(number, 1);
    }

    public static LongFraction valueOf(long numerator, long denominator) {
        return new LongFraction(numerator, denominator);
    }

    public LongFraction multiply(long factor) {
        return LongFraction.valueOf(numerator * factor, denominator).simplify();
    }

    public LongFraction add(LongFraction fraction) {
        return LongFraction.valueOf(numerator * fraction.denominator + denominator * fraction.numerator, denominator * fraction.denominator).simplify();
    }
}