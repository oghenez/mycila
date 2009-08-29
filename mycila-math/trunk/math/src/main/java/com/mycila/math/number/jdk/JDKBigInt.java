package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: optimize all methods
//TODO: see BigIntegerMath.java for general implementations
final class JDKBigInt extends BigInt {

    private final BigInteger impl;
    private final int radix;

    private int bitLength = -1;
    private int bitCount = -1;
    private int lowestSetBit = -1;
    private int length = -1;
    private int digitsSum = -1;
    private int[] digits;
    private int[] digitsSignature;
    private Double isPrime;
    private Boolean isPalindromic;

    JDKBigInt(BigInteger bigInteger, int radix) {
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
    public int compareTo(BigInt o) {
        return impl.compareTo(o.<BigInteger>internal());
    }

    @Override
    public byte toByte() {
        return impl.byteValue();
    }

    @Override
    public short toShort() {
        return impl.shortValue();
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
    public BigInt shiftLeft(int n) {
        return new JDKBigInt(impl.shiftLeft(n), radix);
    }

    @Override
    public BigInt shiftRight(int n) {
        return new JDKBigInt(impl.shiftRight(n), radix);
    }

    @Override
    public BigInt and(BigInt n) {
        return new JDKBigInt(impl.and(n.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt andNot(BigInt n) {
        return new JDKBigInt(impl.andNot(n.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt clearBit(int n) {
        return new JDKBigInt(impl.clearBit(n), radix);
    }

    @Override
    public BigInt flipBit(int n) {
        return new JDKBigInt(impl.flipBit(n), radix);
    }

    @Override
    public BigInt not() {
        return new JDKBigInt(impl.not(), radix);
    }

    @Override
    public BigInt or(BigInt val) {
        return new JDKBigInt(impl.or(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt xor(BigInt val) {
        return new JDKBigInt(impl.xor(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt setBit(int n) {
        return new JDKBigInt(impl.setBit(n), radix);
    }

    @Override
    public BigInt pow(long exponent) {
        return exponent <= Integer.MAX_VALUE ?
                new JDKBigInt(impl.pow((int) exponent), radix) :
                pow(BigInt.big(exponent));
    }

    @Override
    public BigInt add(BigInt val) {
        return new JDKBigInt(impl.add(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt subtract(BigInt val) {
        return new JDKBigInt(impl.subtract(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt multiply(BigInt val) {
        return new JDKBigInt(impl.multiply(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt divide(BigInt val) {
        return new JDKBigInt(impl.divide(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt[] divideAndRemainder(BigInt val) {
        BigInt[] bi = new JDKBigInt[2];
        BigInteger[] qr = impl.divideAndRemainder(val.<BigInteger>internal());
        bi[0] = new JDKBigInt(qr[0], radix);
        bi[1] = new JDKBigInt(qr[1], radix);
        return bi;
    }

    @Override
    public BigInt abs() {
        return new JDKBigInt(impl.abs(), radix);
    }

    @Override
    public BigInt negate() {
        return new JDKBigInt(impl.negate(), radix);
    }

    @Override
    public BigInt max(BigInt val) {
        return new JDKBigInt(impl.max(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt min(BigInt val) {
        return new JDKBigInt(impl.min(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt mod(BigInt m) {
        return new JDKBigInt(impl.mod(m.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt remainder(BigInt val) {
        return new JDKBigInt(impl.remainder(val.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt modInverse(BigInt m) {
        return new JDKBigInt(impl.modInverse(m.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt modPow(BigInt exponent, BigInt m) {
        return new JDKBigInt(impl.modPow(exponent.<BigInteger>internal(), m.<BigInteger>internal()), radix);
    }

    @Override
    public BigInt gcd(BigInt val, BigInt... others) {
        BigInteger gcd = impl.gcd(val.<BigInteger>internal());
        for (int i = 0, max = others.length; i < max; i++)
            gcd = gcd.gcd(others[i].<BigInteger>internal());
        return new JDKBigInt(gcd, radix);
    }

    @Override
    public BigInt lcm(BigInt val, BigInt... others) {
        BigInteger lcm = impl.divide(impl.gcd(val.<BigInteger>internal())).multiply(impl);
        for (int i = 0, max = others.length; i < max; i++)
            lcm = lcm.divide(lcm.gcd(others[i].<BigInteger>internal())).multiply(lcm);
        return new JDKBigInt(lcm, radix);
    }

    @Override
    public double isPrime() {
        return isPrime == null ? (isPrime = impl.isProbablePrime(100) ? 1.0 - Math.pow(0.5, 100) : 0) : isPrime;
    }

    @Override
    public BigInt nextPrime() {
        return new JDKBigInt(impl.nextProbablePrime(), radix);
    }

    @Override
    public BigInt toRadix(int radix) {
        if (this.radix == radix) return this;
        return new JDKBigInt(impl, radix);
    }

    @Override
    public BigInt concat(BigInt... others) {
        if (others.length == 0) return this;
        final StringBuilder sb = new StringBuilder().append(toString());
        for (int i = 0, max = others.length; i < max; i++)
            sb.append(others[i].<BigInteger>internal().toString());
        return new JDKBigInt(new BigInteger(sb.toString(), this.radix), radix);
    }

    @Override
    public int length() {
        return length == -1 ? (length = toString().length()) : length;
    }

    @Override
    public BigInt sort() {
        final char c[] = toString().toCharArray();
        Arrays.sort(c);
        return new JDKBigInt(new BigInteger(String.valueOf(c), this.radix), radix);
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
    public BigInt reverseDigits() {
        final String s = toString();
        final int max = s.length() - 1;
        final char chars[] = new char[max + 1];
        for (int i = 0; i <= max; i++)
            chars[i] = s.charAt(max - i);
        return new JDKBigInt(new BigInteger(String.valueOf(chars), this.radix), radix);
    }

    @Override
    public BigInt rotateDigits(int offset) {
        if (offset == 0) return this;
        final String s = toString();
        final int len = s.length();
        offset %= s.length();
        offset %= len;
        if (offset == 0) return this;
        if (offset < 0) offset = len + offset;
        return new JDKBigInt(new BigInteger(s.substring(len - offset) + s.substring(0, len - offset), this.radix), radix);
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
        for (int i = (s.length() - 1) >>> 1; i >= 0; i--)
            if (s.charAt(i) != s.charAt(len - i))
                return (isPalindromic = false);
        return (isPalindromic = true);
    }

    @Override
    public boolean isPermutation(BigInt val) {
        return Arrays.equals(this.digitsSignature(), val.digitsSignature());
    }

    @Override
    public int radix() {
        return radix;
    }

    @Override
    public BigInt[] euclidExtended(BigInt val) {
        BigInteger u1 = BigInteger.ONE,
                u2 = BigInteger.ZERO,
                u3 = impl,
                v1 = BigInteger.ZERO,
                v2 = BigInteger.ONE,
                v3 = val.internal();
        while (v3.signum() != 0) {
            BigInteger q = u3.divide(v3);
            BigInteger t1 = u1.subtract(q.multiply(v1));
            BigInteger t2 = u2.subtract(q.multiply(v2));
            BigInteger t3 = u3.subtract(q.multiply(v3));
            u1 = v1;
            u2 = v2;
            u3 = v3;
            v1 = t1;
            v2 = t2;
            v3 = t3;
        }
        return new BigInt[]{
                new JDKBigInt(u1, radix),
                new JDKBigInt(u2, radix),
                new JDKBigInt(u3, radix)};
    }

    @Override
    public BigInt[] sqrtInt() {
        if (impl.signum() == 0 || impl.equals(BigInteger.ONE))
            return new BigInt[]{this, zero()};
        BigInteger lastGuess = BigInteger.ZERO;
        BigInteger guess = BigInteger.ONE.shiftLeft(bitLength() >>> 1);
        BigInteger test = lastGuess.subtract(guess);
        BigInteger remainder = impl.subtract(guess.pow(2));
        while (test.signum() != 0 && !test.equals(BigInteger.ONE) || remainder.signum() < 0) {
            lastGuess = guess;
            guess = impl.divide(guess).add(lastGuess).shiftRight(1);
            test = lastGuess.subtract(guess);
            remainder = impl.subtract(guess.pow(2));
        }
        return new BigInt[]{
                new JDKBigInt(guess, radix),
                new JDKBigInt(remainder, radix)};
    }


    //TODO: implement


    @Override
    public BigInt[] rootInt(BigInt root) {
        return new BigInt[0];
    }

    @Override
    public BigInt[] rootInt(long root) {
        return new BigInt[0];
    }

    @Override
    public BigInt add(long val) {
        return null;
    }

    @Override
    public BigInt subtract(long val) {
        return null;
    }

    @Override
    public BigInt multiply(long val) {
        return null;
    }

    @Override
    public BigInt divide(long val) {
        return null;
    }

    @Override
    public BigInt[] divideAndRemainder(long val) {
        return new BigInt[0];
    }

    @Override
    public BigInt mod(long m) {
        return null;
    }

    @Override
    public BigInt remainder(long val) {
        return null;
    }

    @Override
    public BigInt modInverse(long m) {
        return null;
    }

    @Override
    public BigInt modPow(long exponent, long m) {
        return null;
    }

    @Override
    public BigInt modMultiply(BigInt val, BigInt m) {
        return null;
    }

    @Override
    public BigInt modMultiply(long val, long m) {
        return null;
    }

    @Override
    public BigInt modAdd(BigInt val, BigInt m) {
        return null;
    }

    @Override
    public BigInt modAdd(long val, long m) {
        return null;
    }

    @Override
    public BigInt concat(long... numbers) {
        return null;
    }

    @Override
    public BigInt square() {
        return null;
    }

    @Override
    public BigInt binomialCoeff(int k) {
        return null;
    }

    @Override
    public BigInt factorial() {
        return null;
    }

    @Override
    public BigInt fallingFactorial(BigInt n) {
        return null;
    }

    @Override
    public BigInt fallingFactorial(long n) {
        return null;
    }

    @Override
    public BigInt sumTo(BigInt n) {
        return null;
    }

    @Override
    public BigInt sumTo(long n) {
        return null;
    }

    @Override
    public BigInt productTo(BigInt n) {
        return null;
    }

    @Override
    public BigInt productTo(long n) {
        return null;
    }

    @Override
    public BigInt pow(BigInt exponent) {
        return null; // TODO: http://www.coderanch.com/t/385132/Java-General/java/BigInteger-Power-Exponent-BigInteger
    }

    @Override
    public double isMersennePrime() {
        return 0;
    }

}
