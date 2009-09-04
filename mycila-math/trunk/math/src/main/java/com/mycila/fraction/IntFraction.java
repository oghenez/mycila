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

import com.mycila.math.Divisors;

/**
 * @author Mathieu Carbou
 */
//TODO: BigFranction
//TODO: continued BigFrac http://www.luschny.de/math/factorial/approx/continuedfraction.html + (math.mtu.edu)
public final class IntFraction {

    private final int numerator;
    private final int denominator;

    private IntFraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int numerator() {
        return numerator;
    }

    public int denominator() {
        return denominator;
    }

    public int toInt() {
        return numerator / denominator;
    }

    public float toFloat() {
        return (float) numerator / (float) denominator;
    }

    public IntFraction simplify() {
        final int gcd = Divisors.gcd(numerator, denominator);
        return gcd == 1 ? this : new IntFraction(numerator / gcd, denominator / gcd);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public static IntFraction valueOf(int number) {
        return new IntFraction(number, 1);
    }

    public static IntFraction valueOf(int numerator, int denominator) {
        return new IntFraction(numerator, denominator);
    }

    public IntFraction multiply(int factor) {
        return IntFraction.valueOf(numerator * factor, denominator).simplify();
    }

    public IntFraction add(IntFraction fraction) {
        return IntFraction.valueOf(numerator * fraction.denominator + denominator * fraction.numerator, denominator * fraction.denominator).simplify();
    }
}
