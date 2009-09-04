package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JDKBigInt extends BigInt<BigInteger> {

    private final int radix;

    JDKBigInt(BigInteger bigInteger, int radix) {
        super(bigInteger);
        this.radix = radix;
    }

    @Override
    public String toString(int radix) {
        return internal.toString(radix);
    }

    @Override
    public String toString() {
        return internal.toString(radix);
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
        return new JDKBigInt(internal.and((BigInteger) n.internal), radix);
    }

    @Override
    public BigInt not() {
        return new JDKBigInt(internal.not(), radix);
    }

    @Override
    public BigInt or(BigInt val) {
        return new JDKBigInt(internal.or((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt xor(BigInt val) {
        return new JDKBigInt(internal.xor((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt pow(int exponent) {
        return new JDKBigInt(internal.pow(exponent), radix);
    }

    @Override
    public BigInt add(BigInt val) {
        return new JDKBigInt(internal.add((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt subtract(BigInt val) {
        return new JDKBigInt(internal.subtract((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt multiply(BigInt val) {
        return new JDKBigInt(internal.multiply((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt divide(BigInt val) {
        return new JDKBigInt(internal.divide((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt opposite() {
        return new JDKBigInt(internal.negate(), radix);
    }

    @Override
    public int compareTo(BigInt o) {
        return internal.compareTo((BigInteger) o.internal);
    }

    @Override
    public BigInt toRadix(int radix) {
        if (this.radix == radix) return this;
        return new JDKBigInt(internal, radix);
    }

    @Override
    public int radix() {
        return radix;
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
        return new JDKBigInt(internal.shiftLeft(n), radix);
    }

    @Override
    public BigInt shiftRight(int n) {
        return new JDKBigInt(internal.shiftRight(n), radix);
    }

    @Override
    public BigInt andNot(BigInt n) {
        return new JDKBigInt(internal.andNot((BigInteger) n.internal), radix);
    }

    @Override
    public BigInt clearBit(int n) {
        return new JDKBigInt(internal.clearBit(n), radix);
    }

    @Override
    public BigInt flipBit(int n) {
        return new JDKBigInt(internal.flipBit(n), radix);
    }

    @Override
    public BigInt setBit(int n) {
        return new JDKBigInt(internal.setBit(n), radix);
    }

    @Override
    public BigInt[] divideAndRemainder(BigInt val) {
        BigInt[] bi = new JDKBigInt[2];
        BigInteger[] qr = internal.divideAndRemainder((BigInteger) val.internal);
        bi[0] = new JDKBigInt(qr[0], radix);
        bi[1] = new JDKBigInt(qr[1], radix);
        return bi;
    }

    @Override
    public BigInt abs() {
        return new JDKBigInt(internal.abs(), radix);
    }

    @Override
    public BigInt max(BigInt val) {
        return new JDKBigInt(internal.max((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt min(BigInt val) {
        return new JDKBigInt(internal.min((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt mod(BigInt m) {
        return new JDKBigInt(internal.mod((BigInteger) m.internal), radix);
    }

    @Override
    public BigInt remainder(BigInt val) {
        return new JDKBigInt(internal.remainder((BigInteger) val.internal), radix);
    }

    @Override
    public BigInt modInverse(BigInt m) {
        return new JDKBigInt(internal.modInverse((BigInteger) m.internal), radix);
    }

    @Override
    public BigInt modPow(BigInt exponent, BigInt m) {
        return new JDKBigInt(internal.modPow((BigInteger) exponent.internal, (BigInteger) m.internal), radix);
    }

    private Boolean isPrime;

    @Override
    public boolean isPrime() {
        return isPrime == null ? (isPrime = internal.isProbablePrime(100)) : isPrime;
    }

    @Override
    public BigInt nextPrime() {
        return new JDKBigInt(internal.nextProbablePrime(), radix);
    }

    @Override
    public BigInt gcd(BigInt val) {
        return new JDKBigInt(internal.gcd((BigInteger) val.internal), radix);
    }

}
