package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInteger;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: put in this class all methods relative to bigInt, like Factorials, ...
final class JDKBigInteger extends BigInteger {

    private final java.math.BigInteger impl;
    private final int radix;

    private int bitLength = -1;
    private int bitCount = -1;
    private int lowestSetBit = -1;
    private int length = -1;
    private int digitsSum = -1;
    private int[] digits;
    private int[] digitsSignature;
    private Boolean isPrime;
    private Boolean isPalindromic;

    JDKBigInteger(java.math.BigInteger bigInteger, int radix) {
        super(bigInteger);
        this.impl = bigInteger;
        this.radix = radix;
    }

    @Override
    public String toString(int tradix) {
        return impl.toString(tradix);
    }

    @Override
    public String toString() {
        return impl.toString(radix);
    }

    @Override
    public int toInt() {
        return impl.intValue();
    }

    @Override
    public int bitLength() {
        return bitLength == -1 ? (bitLength = impl.bitLength()) : bitLength;
    }

    @Override
    public long toLong() {
        return impl.longValue();
    }

    @Override
    public int compareTo(BigInteger o) {
        return impl.compareTo(o.<java.math.BigInteger>internal());
    }

    @Override
    public byte toByte() {
        return impl.byteValue();
    }

    @Override
    public int bitCount() {
        return bitCount == -1 ? (bitCount = impl.bitCount()) : bitCount;
    }

    @Override
    public int lowestSetBit() {
        return lowestSetBit == -1 ? (lowestSetBit = impl.getLowestSetBit()) : lowestSetBit;
    }

    @Override
    public int signum() {
        return impl.signum();
    }

    @Override
    public boolean testBit(int n) {
        return impl.testBit(n);
    }

    @Override
    public BigInteger shiftLeft(int n) {
        return new JDKBigInteger(impl.shiftLeft(n), radix);
    }

    @Override
    public BigInteger shiftRight(int n) {
        return new JDKBigInteger(impl.shiftRight(n), radix);
    }

    @Override
    public BigInteger and(BigInteger n) {
        return new JDKBigInteger(impl.and(n.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger andNot(BigInteger n) {
        return new JDKBigInteger(impl.andNot(n.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger clearBit(int n) {
        return new JDKBigInteger(impl.clearBit(n), radix);
    }

    @Override
    public BigInteger flipBit(int n) {
        return new JDKBigInteger(impl.flipBit(n), radix);
    }

    @Override
    public BigInteger not() {
        return new JDKBigInteger(impl.not(), radix);
    }

    @Override
    public BigInteger or(BigInteger val) {
        return new JDKBigInteger(impl.or(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger xor(BigInteger val) {
        return new JDKBigInteger(impl.xor(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger setBit(int n) {
        return new JDKBigInteger(impl.setBit(n), radix);
    }

    @Override
    public BigInteger pow(int exponent) {
        return new JDKBigInteger(impl.pow(exponent), radix);
    }

    @Override
    public BigInteger add(BigInteger val) {
        return new JDKBigInteger(impl.add(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger subtract(BigInteger val) {
        return new JDKBigInteger(impl.subtract(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger multiply(BigInteger val) {
        return new JDKBigInteger(impl.multiply(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger divide(BigInteger val) {
        return new JDKBigInteger(impl.divide(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger[] divideAndRemainder(BigInteger val) {
        BigInteger[] bi = new JDKBigInteger[2];
        java.math.BigInteger[] qr = impl.divideAndRemainder(val.<java.math.BigInteger>internal());
        bi[0] = new JDKBigInteger(qr[0], radix);
        bi[1] = new JDKBigInteger(qr[1], radix);
        return bi;
    }

    @Override
    public BigInteger abs() {
        return new JDKBigInteger(impl.abs(), radix);
    }

    @Override
    public BigInteger negate() {
        return new JDKBigInteger(impl.negate(), radix);
    }

    @Override
    public BigInteger max(BigInteger val) {
        return new JDKBigInteger(impl.max(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger min(BigInteger val) {
        return new JDKBigInteger(impl.min(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger mod(BigInteger m) {
        return new JDKBigInteger(impl.mod(m.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger remainder(BigInteger val) {
        return new JDKBigInteger(impl.remainder(val.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger modInverse(BigInteger m) {
        return new JDKBigInteger(impl.modInverse(m.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger modPow(BigInteger exponent, BigInteger m) {
        return new JDKBigInteger(impl.modPow(exponent.<java.math.BigInteger>internal(), m.<java.math.BigInteger>internal()), radix);
    }

    @Override
    public BigInteger gcd(BigInteger val, BigInteger... others) {
        java.math.BigInteger gcd = impl.gcd(val.<java.math.BigInteger>internal());
        for (int i = 0, max = others.length; i < max; i++)
            gcd = gcd.gcd(others[i].<java.math.BigInteger>internal());
        return new JDKBigInteger(gcd, radix);
    }

    @Override
    public BigInteger lcm(BigInteger val, BigInteger... others) {
        java.math.BigInteger lcm = impl.divide(impl.gcd(val.<java.math.BigInteger>internal())).multiply(impl);
        for (int i = 0, max = others.length; i < max; i++)
            lcm = lcm.divide(lcm.gcd(others[i].<java.math.BigInteger>internal())).multiply(lcm);
        return new JDKBigInteger(lcm, radix);
    }

    @Override
    public boolean isPrime() {
        return isPrime == null ? (isPrime = impl.isProbablePrime(100)) : isPrime;
    }

    @Override
    public BigInteger nextPrime() {
        return new JDKBigInteger(impl.nextProbablePrime(), radix);
    }

    @Override
    public BigInteger toRadix(int radix) {
        if (this.radix == radix) return this;
        return new JDKBigInteger(impl, radix);
    }

    @Override
    public BigInteger concat(BigInteger... others) {
        if (others.length == 0) return this;
        final StringBuilder sb = new StringBuilder().append(toString());
        for (int i = 0, max = others.length; i < max; i++)
            sb.append(others[i].<java.math.BigInteger>internal().toString());
        return new JDKBigInteger(new java.math.BigInteger(sb.toString(), this.radix), radix);
    }

    @Override
    public int length() {
        return length == -1 ? (length = toString().length()) : length;
    }

    @Override
    public BigInteger sort() {
        final char c[] = toString().toCharArray();
        Arrays.sort(c);
        return new JDKBigInteger(new java.math.BigInteger(String.valueOf(c), this.radix), radix);
    }

    @Override
    public int[] digits() {
        if (digits != null) return Arrays.copyOf(digits, digits.length);
        final String s = toString();
        final int[] digits = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--)
            digits[i] = s.charAt(i) - 48;
        return (this.digits = digits);
    }

    @Override
    public int digitsSum() {
        if (digitsSum != -1) return digitsSum;
        int sum = 0;
        final String s = toString();
        for (int i = s.length() - 1; i >= 0; i--)
            sum += s.charAt(i) - 48;
        return (this.digitsSum = sum);

    }

    @Override
    public BigInteger reverseDigits() {
        final String s = toString();
        final int max = s.length() - 1;
        final char chars[] = new char[max + 1];
        for (int i = 0; i <= max; i++)
            chars[i] = s.charAt(max - i);
        return new JDKBigInteger(new java.math.BigInteger(String.valueOf(chars), this.radix), radix);
    }

    @Override
    public BigInteger rotateDigits(int offset) {
        if (offset == 0) return this;
        final String s = toString();
        final int len = s.length();
        offset %= s.length();
        offset %= len;
        if (offset == 0) return this;
        if (offset < 0) offset = len + offset;
        return new JDKBigInteger(new java.math.BigInteger(s.substring(len - offset) + s.substring(0, len - offset), this.radix), radix);
    }

    @Override
    public int[] digitsSignature() {
        if (digitsSignature != null) return Arrays.copyOf(digitsSignature, digitsSignature.length);
        final String s = toString();
        final int[] digits = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--)
            digits[i] = s.charAt(i) - 48;
        Arrays.sort(digits);
        return (digitsSignature = digits);
    }

    @Override
    public boolean isPalindromic() {
        if (isPalindromic != null) return isPalindromic;
        final String s = toString();
        final int len = s.length() - 1;
        for (int i = (s.length() - 1) >> 1; i >= 0; i--)
            if (s.charAt(i) != s.charAt(len - i))
                return (isPalindromic = false);
        return (isPalindromic = true);
    }

    @Override
    public boolean isPermutation(BigInteger val) {
        return Arrays.equals(this.digitsSignature(), val.digitsSignature());
    }

    @Override
    public int radix() {
        return radix;
    }
}
