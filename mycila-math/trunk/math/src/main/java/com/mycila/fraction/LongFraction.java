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