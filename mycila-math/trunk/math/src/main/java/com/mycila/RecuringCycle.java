package com.mycila;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class RecuringCycle {

    private static final BigInteger TEN = BigInteger.valueOf(10);

    private int length = 0;
    private BigInteger cycle = BigInteger.ZERO;
    private final int prime;

    private RecuringCycle(int prime) {
        this.prime = prime;
        // if p is prime, we check the least number that satisfy 10^l mod p = 1
        BigInteger p = BigInteger.valueOf(prime);
        for (int l = 1; l < prime; l++) {
            BigInteger[] qr = TEN.pow(l).divideAndRemainder(p);
            // qr[0] is the quotient. It is also equals to the cycle of 1/primeNumber
            // qr[1] is the remainder.
            if (qr[1].equals(BigInteger.ONE)) {
                // we found the length l of the cycle of 1/p
                cycle = qr[0];
                length = l;
                break;
            }
        }
    }

    public int length() {
        return length;
    }

    public BigInteger cycle() {
        return cycle;
    }

    public int prime() {
        return prime;
    }

    @Override
    public String toString() {
        return "Recuring cycle of 1/" + prime + ": length=" + length + ", cycle=" + Format.leftPad(cycle.toString(), length, '0');
    }

    public static RecuringCycle of(int prime) {
        return new RecuringCycle(prime);
    }
}