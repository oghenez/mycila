package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInt;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: optimize all methods
//TODO: see BigIntegerMath.java for general implementations
final class JDKBigInt extends BigInt<BigInteger> {

    private static final BigInteger MaxInt = BigInteger.valueOf(Integer.MAX_VALUE);

    private final int radix;

    private int length = -1;
    private int digitsSum = -1;
    private int[] digits;
    private int[] digitsSignature;
    private Double isPrime;
    private Boolean isPalindromic;

    JDKBigInt(BigInteger bigInteger, int radix) {
        super(bigInteger);
        this.radix = radix;
    }

    @Override
    public String toString(int tradix) {
        return internal.toString(tradix);
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
    public byte toByte() {
        return internal.byteValue();
    }

    @Override
    public short toShort() {
        return internal.shortValue();
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
        return new JDKBigInt(internal.not(), radix);
    }

    @Override
    public BigInt or(BigInt val) {
        return new JDKBigInt(internal.or(impl(val)), radix);
    }

    @Override
    public BigInt xor(BigInt val) {
        return new JDKBigInt(internal.xor(impl(val)), radix);
    }

    // from http://www.coderanch.com/t/385132/Java-General/java/BigInteger-Power-Exponent-BigInteger
    @Override
    public BigInt pow(BigInt exponent) {
        BigInteger exp = impl(exponent);
        if (exp.signum() == 0)
            return one();
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
        return new JDKBigInt(internal.add(impl(val)), radix);
    }

    @Override
    public BigInt subtract(BigInt val) {
        return new JDKBigInt(internal.subtract(impl(val)), radix);
    }

    @Override
    public BigInt multiply(BigInt val) {
        return new JDKBigInt(internal.multiply(impl(val)), radix);
    }

    @Override
    public BigInt divide(BigInt val) {
        return new JDKBigInt(internal.divide(impl(val)), radix);
    }

    @Override
    public BigInt opposite() {
        return new JDKBigInt(internal.negate(), radix);
    }

    @Override
    public int compareTo(BigInt o) {
        return internal.compareTo(impl(o));
    }

    private JDKBigInt wrap(BigInteger bi) {
        return new JDKBigInt(bi, radix);
    }

    // OVERRIDEN FOR BETTER PERF.

    /*@Override
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
        return new JDKBigInt(internal.andNot(impl(n)), radix);
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
    public BigInt pow(long exponent) {
        return exponent <= Integer.MAX_VALUE ?
                new JDKBigInt(internal.pow((int) exponent), radix) :
                pow(big(exponent));
    }

    @Override
    public BigInt[] divideAndRemainder(BigInt val) {
        BigInt[] bi = new JDKBigInt[2];
        BigInteger[] qr = internal.divideAndRemainder(impl(val));
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
        return new JDKBigInt(internal.max(impl(val)), radix);
    }

    @Override
    public BigInt min(BigInt val) {
        return new JDKBigInt(internal.min(impl(val)), radix);
    }

    @Override
    public BigInt mod(BigInt m) {
        return new JDKBigInt(internal.mod(impl(m)), radix);
    }

    @Override
    public BigInt remainder(BigInt val) {
        return new JDKBigInt(internal.remainder(impl(val)), radix);
    }

    @Override
    public BigInt modInverse(BigInt m) {
        return new JDKBigInt(internal.modInverse(impl(m)), radix);
    }

    @Override
    public BigInt modPow(BigInt exponent, BigInt m) {
        return new JDKBigInt(internal.modPow(impl(exponent), impl(m)), radix);
    }

    @Override
    public BigInt gcd(BigInt val, BigInt... others) {
        BigInteger gcd = internal.gcd(impl(val));
        for (int i = 0, max = others.length; i < max; i++)
            gcd = gcd.gcd(impl(others[i]));
        return new JDKBigInt(gcd, radix);
    }

    @Override
    public BigInt lcm(BigInt val, BigInt... others) {
        BigInteger lcm = internal.divide(internal.gcd(impl(val))).multiply(internal);
        for (int i = 0, max = others.length; i < max; i++)
            lcm = lcm.divide(lcm.gcd(impl(others[i]))).multiply(lcm);
        return new JDKBigInt(lcm, radix);
    }

    @Override
    public double isPrime() {
        return isPrime == null ? (isPrime = internal.isProbablePrime(100) ? 1.0 - Math.pow(0.5, 100) : 0) : isPrime;
    }

    @Override
    public BigInt nextPrime() {
        return new JDKBigInt(internal.nextProbablePrime(), radix);
    }

    @Override
    public BigInt toRadix(int radix) {
        if (this.radix == radix) return this;
        return new JDKBigInt(internal, radix);
    }

    @Override
    public BigInt concat(BigInt... others) {
        if (others.length == 0) return this;
        final StringBuilder sb = new StringBuilder().append(toString());
        for (int i = 0, max = others.length; i < max; i++)
            sb.append(others[i].internal.toString());
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
        BigInteger p = BigInteger.ONE,
                q = BigInteger.ZERO,
                a = internal,
                r = BigInteger.ZERO,
                s = BigInteger.ONE,
                b = impl(val);
        while (b.signum() != 0) {
            BigInteger quot = a.divide(b);
            BigInteger t1 = p.subtract(quot.multiply(r));
            BigInteger t2 = q.subtract(quot.multiply(s));
            BigInteger t3 = a.subtract(quot.multiply(b));
            p = r;
            q = s;
            a = b;
            r = t1;
            s = t2;
            b = t3;
        }
        return new BigInt[]{
                new JDKBigInt(p, radix),
                new JDKBigInt(q, radix),
                new JDKBigInt(a, radix)};
    }

    @Override
    public BigInt[] sqrtInt() {
        if (internal.signum() == 0 || internal.equals(BigInteger.ONE))
            return new BigInt[]{this, zero()};
        BigInteger lastGuess = BigInteger.ZERO;
        BigInteger guess = BigInteger.ONE.shiftLeft(bitLength() >>> 1);
        BigInteger test = lastGuess.subtract(guess);
        BigInteger remainder = internal.subtract(guess.pow(2));
        while (test.signum() != 0 && !test.equals(BigInteger.ONE) || remainder.signum() < 0) {
            lastGuess = guess;
            guess = internal.divide(guess).add(lastGuess).shiftRight(1);
            test = lastGuess.subtract(guess);
            remainder = internal.subtract(guess.pow(2));
        }
        return new BigInt[]{
                new JDKBigInt(guess, radix),
                new JDKBigInt(remainder, radix)};
    }


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
    public double isMersennePrime() {
        return 0;
    }*/

}
