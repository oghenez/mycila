package com.mycila.fraction;

import com.mycila.Divisors;

/**
 * @author Mathieu Carbou
 */
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
