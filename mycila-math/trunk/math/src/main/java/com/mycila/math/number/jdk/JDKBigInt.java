package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JDKBigInt extends BigInt<BigInteger> {

    private static final BigInteger MaxInt = BigInteger.valueOf(Integer.MAX_VALUE);

    private final int radix;

    JDKBigInt(BigInteger bigInteger, int radix) {
        super(bigInteger);
        this.radix = radix;
    }

    @Override
    public String toString(int tradix) {
        return internal.toString(tradix);
    }

    @Override
    public int toInt() {
        return internal.intValue();
    }

    @Override
    public long toLong() {
        return internal.longValue();
    }

    @Override
    public int bitLength() {
        return internal.bitLength();
    }

    @Override
    public int bitCount() {
        return internal.bitCount();
    }

    @Override
    public int lowestSetBit() {
        return internal.getLowestSetBit();
    }

    @Override
    public BigInt and(BigInt n) {
        return wrap(internal.and(impl(n)));
    }

    @Override
    public BigInt not() {
        return wrap(internal.not());
    }

    @Override
    public BigInt or(BigInt val) {
        return wrap(internal.or(impl(val)));
    }

    @Override
    public BigInt xor(BigInt val) {
        return wrap(internal.xor(impl(val)));
    }

    @Override
    public BigInt pow(BigInt exponent) {
        // from http://www.coderanch.com/t/385132/Java-General/java/BigInteger-Power-Exponent-BigInteger
        BigInteger exp = impl(exponent);
        if (exp.signum() == 0)
            return ONE;
        if (exp.compareTo(MaxInt) <= 0)
            return wrap(internal.pow(exp.intValue()));
        BigInteger z = internal;
        // z will successively become x^2, x^4, x^8, x^16, x^32...
        BigInteger result = BigInteger.ONE;
        final byte[] bytes = exp.toByteArray();
        for (int i = bytes.length - 1; i >= 0; i--) {
            byte bits = bytes[i];
            for (int j = 0; j < 8; j++) {
                if ((bits & 1) != 0)
                    result = result.multiply(z);
                // short cut out if there are no more bits to handle:
                if ((bits >>= 1) == 0 && i == 0)
                    return wrap(result);
                z = z.multiply(z);
            }
        }
        return wrap(result);
    }

    @Override
    public BigInt add(BigInt val) {
        return wrap(internal.add(impl(val)));
    }

    @Override
    public BigInt subtract(BigInt val) {
        return wrap(internal.subtract(impl(val)));
    }

    @Override
    public BigInt multiply(BigInt val) {
        return wrap(internal.multiply(impl(val)));
    }

    @Override
    public BigInt divide(BigInt val) {
        return wrap(internal.divide(impl(val)));
    }

    @Override
    public BigInt opposite() {
        return wrap(internal.negate());
    }

    @Override
    public int compareTo(BigInt o) {
        return internal.compareTo(impl(o));
    }

    @Override
    public BigInt toRadix(int radix) {
        if (this.radix == radix) return this;
        return wrapBig(internal, radix);
    }

    @Override
    public int radix() {
        return radix;
    }

    private BigInt wrap(BigInteger bi) {
        return wrapBig(bi, radix());
    }

    // OVERRIDEN FOR BETTER PERF.

    @Override
    public int signum() {
        return internal.signum();
    }

    @Override
    public boolean testBit(int n) {
        return internal.testBit(n);
    }

    @Override
    public BigInt shiftLeft(int n) {
        return wrap(internal.shiftLeft(n));
    }

    @Override
    public BigInt shiftRight(int n) {
        return wrap(internal.shiftRight(n));
    }

    @Override
    public BigInt andNot(BigInt n) {
        return wrap(internal.andNot(impl(n)));
    }

    @Override
    public BigInt clearBit(int n) {
        return wrap(internal.clearBit(n));
    }

    @Override
    public BigInt flipBit(int n) {
        return wrap(internal.flipBit(n));
    }

    @Override
    public BigInt setBit(int n) {
        return wrap(internal.setBit(n));
    }

    @Override
    public BigInt pow(long exponent) {
        return exponent <= Integer.MAX_VALUE ?
                wrap(internal.pow((int) exponent)) :
                pow(big(exponent));
    }

    @Override
    public BigInt[] divideAndRemainder(BigInt val) {
        BigInt[] bi = new JDKBigInt[2];
        BigInteger[] qr = internal.divideAndRemainder(impl(val));
        bi[0] = wrap(qr[0]);
        bi[1] = wrap(qr[1]);
        return bi;
    }

    @Override
    public BigInt abs() {
        return wrap(internal.abs());
    }

    @Override
    public BigInt max(BigInt val) {
        return wrap(internal.max(impl(val)));
    }

    @Override
    public BigInt min(BigInt val) {
        return wrap(internal.min(impl(val)));
    }

    @Override
    public BigInt mod(BigInt m) {
        return wrap(internal.mod(impl(m)));
    }

    @Override
    public BigInt remainder(BigInt val) {
        return wrap(internal.remainder(impl(val)));
    }

    @Override
    public BigInt modInverse(BigInt m) {
        return wrap(internal.modInverse(impl(m)));
    }

    @Override
    public BigInt modPow(BigInt exponent, BigInt m) {
        return wrap(internal.modPow(impl(exponent), impl(m)));
    }

    private Boolean isPrime;

    @Override
    public boolean isPrime() {
        return isPrime == null ? (isPrime = internal.isProbablePrime(100)) : isPrime;
    }

    @Override
    public BigInt nextPrime() {
        return wrap(internal.nextProbablePrime());
    }

    @Override
    public BigInt gcd(BigInt val) {
        return wrap(internal.gcd(impl(val)));
    }

}
