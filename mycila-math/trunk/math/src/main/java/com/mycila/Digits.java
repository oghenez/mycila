package com.mycila;

import com.mycila.sequence.IntProcedure;

import java.math.BigInteger;

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

    public int reverse(int number) {
        int reverse = 0;
        while (number != 0) {
            reverse = reverse * base + number % base;
            number /= base;
        }
        return reverse;
    }

    public BigInteger reverse(BigInteger number) {
        BigInteger reverse = BigInteger.ZERO;
        while (number.signum() == 1) {
            final BigInteger[] qr = number.divideAndRemainder(bigBase);
            reverse = reverse.multiply(bigBase).add(qr[1]);
            number = qr[0];
        }
        return reverse;
    }

    public int rotate(int number) {
        int n = number, p = 1;
        while ((n /= base) > 0) p *= base;
        return number / base + p * (number % base);
    }

    public boolean each(int number, IntProcedure procedure) {
        do if (!procedure.execute(number % base)) return false;
        while ((number /= base) > 0);
        return true;
    }

    public static Digits base(int base) {
        return new Digits(base);
    }
}
