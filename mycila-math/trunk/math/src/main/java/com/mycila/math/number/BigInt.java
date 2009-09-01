package com.mycila.math.number;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: as classes are moved to math package, add methods to this class
//TODO: make wrapper for optimized BigInteger + BigIntegerMath.java, apflot, jscience, ... + Javolution contexts and factories
public abstract class BigInt<T> implements Comparable<BigInt> {

    public static final BigInt ZERO;
    public static final BigInt ONE;
    public static final BigInt TWO;
    public static final BigInt TEN;
    public static final BigInt INT_MAX;
    public static final BigInt LONG_MAX;

    private static final BigIntFactory FACTORY;

    static {
        try {
            String cname = System.getProperty("mycila.bigint.factory");
            if (cname == null) cname = "com.mycila.math.number.jdk.JDKBigIntFactory";
            @SuppressWarnings({"unchecked"})
            Class<BigIntFactory> fClass = (Class<BigIntFactory>) Thread.currentThread().getContextClassLoader().loadClass(cname);
            if (!BigIntFactory.class.isAssignableFrom(fClass))
                throw new RuntimeException(cname + " does not implement " + BigIntFactory.class.getName());
            FACTORY = fClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        ZERO = big(0);
        ONE = big(1);
        TWO = big(2);
        TEN = big(10);
        INT_MAX = big(Integer.MAX_VALUE);
        LONG_MAX = big(Long.MAX_VALUE);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> BigInt wrapBig(T number, int radix) {
        return FACTORY.wrap(number, radix);
    }

    public static BigInt big(long number) {
        return FACTORY.create(number);
    }

    public static BigInt big(String number) {
        return FACTORY.create(number);
    }

    public static BigInt big(String number, int radix) {
        return FACTORY.create(number, radix);
    }

    public static BigInt randomBig(int length) {
        return FACTORY.random(length);
    }

    public final T internal;

    protected BigInt(T internal) {
        this.internal = internal;
    }

    @SuppressWarnings({"unchecked"})
    protected final T impl(BigInt o) {
        return (T) o.internal;
    }

    @Override
    public final int hashCode() {
        return internal.hashCode();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == internal.getClass()) return internal.equals(o);
        return o.getClass() == getClass() && internal.equals(((BigInt) o).internal);
    }

    public abstract String toString(int tradix);

    /**
     * Converts this BigInteger to an {@code int}.  This
     * conversion is analogous to a <a
     * href="http://java.sun.com/docs/books/jls/second_edition/html/conversions.doc.html#25363"><i>narrowing
     * primitive conversion</i></a> from {@code long} to
     * {@code int} as defined in the <a
     * href="http://java.sun.com/docs/books/jls/html/">Java Language
     * Specification</a>: if this BigInteger is too big to fit in an
     * {@code int}, only the low-order 32 bits are returned.
     * Note that this conversion can lose information about the
     * overall magnitude of the BigInteger value as well as return a
     * result with the opposite sign.
     *
     * @return this BigInteger converted to an {@code int}.
     */
    public abstract int toInt();

    /**
     * Converts this BigInteger to a {@code long}.  This
     * conversion is analogous to a <a
     * href="http://java.sun.com/docs/books/jls/second_edition/html/conversions.doc.html#25363"><i>narrowing
     * primitive conversion</i></a> from {@code long} to
     * {@code int} as defined in the <a
     * href="http://java.sun.com/docs/books/jls/html/">Java Language
     * Specification</a>: if this BigInteger is too big to fit in a
     * {@code long}, only the low-order 64 bits are returned.
     * Note that this conversion can lose information about the
     * overall magnitude of the BigInteger value as well as return a
     * result with the opposite sign.
     *
     * @return this BigInteger converted to a {@code long}.
     */
    public abstract long toLong();

    /**
     * Returns the number of bits in the minimal two's-complement
     * representation of this BigInteger, <i>excluding</i> a sign bit.
     * For positive BigIntegers, this is equivalent to the number of bits in
     * the ordinary binary representation.  (Computes
     * {@code (ceil(log2(this < 0 ? -this : this+1)))}.)
     *
     * @return number of bits in the minimal two's-complement
     *         representation of this BigInteger, <i>excluding</i> a sign bit.
     */
    public abstract int bitLength();

    /**
     * Returns the number of bits in the two's complement representation
     * of this BigInteger that differ from its sign bit.  This method is
     * useful when implementing bit-vector style sets atop BigIntegers.
     *
     * @return number of bits in the two's complement representation
     *         of this BigInteger that differ from its sign bit.
     */
    public abstract int bitCount();

    /**
     * Returns the index of the rightmost (lowest-order) one bit in this
     * BigInteger (the number of zero bits to the right of the rightmost
     * one bit).  Returns -1 if this BigInteger contains no one bits.
     * (Computes {@code (this==0? -1 : log2(this & -this))}.)
     *
     * @return index of the rightmost one bit in this BigInteger.
     */
    public abstract int lowestSetBit();

    /**
     * Returns a BigInteger whose value is {@code (this & val)}.  (This
     * method returns a negative BigInteger if and only if this and val are
     * both negative.)
     *
     * @param n value to be AND'ed with this BigInteger.
     * @return {@code this & val}
     */
    public abstract BigInt and(BigInt n);

    /**
     * Returns a BigInteger whose value is {@code (~this)}.  (This method
     * returns a negative value if and only if this BigInteger is
     * non-negative.)
     *
     * @return {@code ~this}
     */
    public abstract BigInt not();

    /**
     * Returns a BigInteger whose value is {@code (this | val)}.  (This method
     * returns a negative BigInteger if and only if either this or val is
     * negative.)
     *
     * @param val value to be OR'ed with this BigInteger.
     * @return {@code this | val}
     */
    public abstract BigInt or(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (this ^ val)}.  (This method
     * returns a negative BigInteger if and only if exactly one of this and
     * val are negative.)
     *
     * @param val value to be XOR'ed with this BigInteger.
     * @return {@code this ^ val}
     */
    public abstract BigInt xor(BigInt val);

    /**
     * Returns a BigInteger whose value is <tt>(this<sup>exponent</sup>)</tt>.
     *
     * @param exponent exponent to which this BigInteger is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>
     * @throws ArithmeticException {@code exponent} is negative.  (This would
     *                             cause the operation to yield a non-integer value.)
     */
    public abstract BigInt pow(BigInt exponent);

    /**
     * Returns a BigInteger whose value is {@code (this + val)}.
     *
     * @param val value to be added to this BigInteger.
     * @return {@code this + val}
     */
    public abstract BigInt add(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (this - val)}.
     *
     * @param val value to be subtracted from this BigInteger.
     * @return {@code this - val}
     */
    public abstract BigInt subtract(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (this * val)}.
     *
     * @param val value to be multiplied by this BigInteger.
     * @return {@code this * val}
     */
    public abstract BigInt multiply(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (this / val)}.
     *
     * @param val value by which this BigInteger is to be divided.
     * @return {@code this / val}
     * @throws ArithmeticException {@code val==0}
     */
    public abstract BigInt divide(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (-this)}.
     *
     * @return {@code -this}
     */
    public abstract BigInt opposite();

    /**
     * Compares this BigInteger with the specified BigInteger.  This
     * method is provided in preference to individual methods for each
     * of the six boolean comparison operators ({@literal <}, ==,
     * {@literal >}, {@literal >=}, !=, {@literal <=}).  The suggested
     * idiom for performing these comparisons is: {@code
     * (x.compareTo(y)} &lt;<i>op</i>&gt; {@code 0)}, where
     * &lt;<i>op</i>&gt; is one of the six comparison operators.
     *
     * @param val BigInteger to which this BigInteger is to be compared.
     * @return -1, 0 or 1 as this BigInteger is numerically less than, equal
     *         to, or greater than {@code val}.
     */
    public abstract int compareTo(BigInt val);

    /**
     * Convert a number to the given radix.
     *
     * @param radix new radix
     * @return this number is the new radix
     */
    public abstract BigInt toRadix(int radix);

    /**
     * Get the current radix used to display this number
     *
     * @return the current radix
     */
    public abstract int radix();

    /**
     * Returns {@code true} if this BigInteger is prime,
     * {@code false} if it's definitely composite.
     *
     * @return the probabilty it is prime. O if the BigInteger is determined to be composite, 1 if it is prime
     */
    public abstract boolean isPrime();

    /**
     * Returns the first integer greater than this {@code BigInteger} that
     * is prime.  This method will
     * never skip over a prime when searching: if it returns {@code p}, there
     * is no prime {@code q} such that {@code this < q < p}.
     *
     * @return the first integer greater than this {@code BigInteger} that
     *         is prime.
     * @throws ArithmeticException {@code this < 0}.
     */
    public abstract BigInt nextPrime();

    @Override
    public String toString() {
        return toString(radix());
    }

    /**
     * Returns the signum function of this BigInteger.
     *
     * @return -1, 0 or 1 as the value of this BigInteger is negative, zero or
     *         positive.
     */
    public int signum() {
        int comp = this.compareTo(ZERO);
        return comp < 0 ? -1 : comp == 0 ? 0 : 1;
    }

    /**
     * Returns {@code true} if and only if the designated bit is set.
     * (Computes {@code ((this & (1<<n)) != 0)}.)
     *
     * @param n index of bit to test.
     * @return {@code true} if and only if the designated bit is set.
     * @throws ArithmeticException {@code n} is negative.
     */
    public boolean testBit(int n) {
        return !ZERO.equals(this.and(ONE.shiftLeft(n)));
    }

    /**
     * Returns a BigInteger whose value is {@code (this << n)}.
     * The shift distance, {@code n}, may be negative, in which case
     * this method performs a right shift.
     * (Computes <tt>floor(this * 2<sup>n</sup>)</tt>.)
     *
     * @param n shift distance, in bits.
     * @return {@code this << n}
     * @see #shiftRight
     */
    public BigInt shiftLeft(int n) {
        return this.multiply(TWO.pow(n));
    }

    /**
     * Returns a BigInteger whose value is {@code (this >> n)}.  Sign
     * extension is performed.  The shift distance, {@code n}, may be
     * negative, in which case this method performs a left shift.
     * (Computes <tt>floor(this / 2<sup>n</sup>)</tt>.)
     *
     * @param n shift distance, in bits.
     * @return {@code this >> n}
     * @see #shiftLeft
     */
    public BigInt shiftRight(int n) {
        return this.divide(TWO.pow(n));
    }

    /**
     * Returns a BigInteger whose value is {@code (this & ~val)}.  This
     * method, which is equivalent to {@code and(val.not())}, is provided as
     * a convenience for masking operations.  (This method returns a negative
     * BigInteger if and only if {@code this} is negative and {@code val} is
     * positive.)
     *
     * @param val value to be complemented and AND'ed with this BigInteger.
     * @return {@code this & ~val}
     */
    public BigInt andNot(BigInt val) {
        return this.and(val.not());
    }

    /**
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit cleared.
     * (Computes {@code (this & ~(1<<n))}.)
     *
     * @param n index of bit to clear.
     * @return {@code this & ~(1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public BigInt clearBit(int n) {
        return this.and(ONE.shiftLeft(n).not());
    }

    /**
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit flipped.
     * (Computes {@code (this ^ (1<<n))}.)
     *
     * @param n index of bit to flip.
     * @return {@code this ^ (1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public BigInt flipBit(int n) {
        return this.xor(ONE.shiftLeft(n).not());
    }

    /**
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit set.  (Computes {@code (this | (1<<n))}.)
     *
     * @param n index of bit to set.
     * @return {@code this | (1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public BigInt setBit(int n) {
        return this.or(ONE.shiftLeft(n).not());
    }

    /**
     * Returns a BigInteger whose value is <tt>(this<sup>exponent</sup>)</tt>.
     *
     * @param exponent exponent to which this BigInteger is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>
     * @throws ArithmeticException {@code exponent} is negative.  (This would
     *                             cause the operation to yield a non-integer value.)
     */
    public BigInt pow(long exponent) {
        return pow(big(exponent));
    }

    /**
     * Returns this^2
     *
     * @return this^2
     */
    public BigInt square() {
        return this.multiply(this);
    }

    /**
     * Returns a BigInteger whose value is {@code (this + val)}.
     *
     * @param val value to be added to this BigInteger.
     * @return {@code this + val}
     */
    public BigInt add(long val) {
        return add(big(val));
    }

    /**
     * Returns a BigInteger whose value is {@code (this - val)}.
     *
     * @param val value to be subtracted from this BigInteger.
     * @return {@code this - val}
     */
    public BigInt subtract(long val) {
        return subtract(big(val));
    }

    /**
     * Returns a BigInteger whose value is {@code (this * val)}.
     *
     * @param val value to be multiplied by this BigInteger.
     * @return {@code this * val}
     */
    public BigInt multiply(long val) {
        return multiply(big(val));
    }

    /**
     * Returns a BigInteger whose value is {@code (this / val)}.
     *
     * @param val value by which this BigInteger is to be divided.
     * @return {@code this / val}
     * @throws ArithmeticException {@code val==0}
     */
    public BigInt divide(long val) {
        return divide(big(val));
    }

    /**
     * Returns an array of two BigIntegers containing {@code (this / val)}
     * followed by {@code (this % val)}.
     *
     * @param val value by which this BigInteger is to be divided, and the
     *            remainder computed.
     * @return an array of two BigIntegers: the quotient {@code (this / val)}
     *         is the initial element, and the remainder {@code (this % val)}
     *         is the final element.
     * @throws ArithmeticException {@code val==0}
     */
    public BigInt[] divideAndRemainder(BigInt val) {
        BigInt[] res = new BigInt[2];
        res[0] = divide(val);
        res[1] = subtract(val.multiply(res[0]));
        return res;
    }

    /**
     * Returns an array of two BigIntegers containing {@code (this / val)}
     * followed by {@code (this % val)}.
     *
     * @param val value by which this BigInteger is to be divided, and the
     *            remainder computed.
     * @return an array of two BigIntegers: the quotient {@code (this / val)}
     *         is the initial element, and the remainder {@code (this % val)}
     *         is the final element.
     * @throws ArithmeticException {@code val==0}
     */
    public BigInt[] divideAndRemainder(long val) {
        return divideAndRemainder(big(val));
    }

    /**
     * Returns a BigInteger whose value is the absolute value of this
     * BigInteger.
     *
     * @return {@code abs(this)}
     */
    public BigInt abs() {
        return signum() == -1 ? opposite() : this;
    }

    /**
     * Returns the maximum of this BigInteger and {@code val}.
     *
     * @param val value with which the maximum is to be computed.
     * @return the BigInteger whose value is the greater of this and
     *         {@code val}.  If they are equal, either may be returned.
     */
    public BigInt max(BigInt val) {
        return compareTo(val) > 0 ? this : val;
    }

    /**
     * Returns the minimum of this BigInteger and {@code val}.
     *
     * @param val value with which the minimum is to be computed.
     * @return the BigInteger whose value is the lesser of this BigInteger and
     *         {@code val}.  If they are equal, either may be returned.
     */
    public BigInt min(BigInt val) {
        return compareTo(val) < 0 ? this : val;
    }

    /**
     * Returns a BigInteger whose value is {@code (thismodm)}.  This method
     * differs from {@code remainder} in that it always returns a
     * <i>non-negative</i> BigInteger.
     *
     * @param m the modulus.
     * @return {@code this mod m}
     * @throws ArithmeticException {@code m <= 0}
     */
    public BigInt mod(BigInt m) {
        BigInt result = remainder(m);
        return result.signum() >= 0 ? result : result.add(m);
    }

    /**
     * Returns a BigInteger whose value is {@code (thismodm)}.  This method
     * differs from {@code remainder} in that it always returns a
     * <i>non-negative</i> BigInteger.
     *
     * @param m the modulus.
     * @return {@code this mod m}
     * @throws ArithmeticException {@code m <= 0}
     */
    public BigInt mod(long m) {
        if (bitLength() <= 63) return big(toLong() % m);
        return mod(big(m));
    }

    /**
     * Returns a BigInteger whose value is {@code (this % val)}.
     *
     * @param val value by which this BigInteger is to be divided, and the
     *            remainder computed.
     * @return {@code this % val}
     * @throws ArithmeticException {@code val==0}
     */
    public BigInt remainder(BigInt val) {
        return subtract(val.multiply(divide(val)));
    }

    /**
     * Returns a BigInteger whose value is {@code (this % val)}.
     *
     * @param val value by which this BigInteger is to be divided, and the
     *            remainder computed.
     * @return {@code this % val}
     * @throws ArithmeticException {@code val==0}
     */
    public BigInt remainder(long val) {
        return remainder(big(val));
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">Extended Euclidean algorithm</a>.
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * For 'this' and 'val', this algorithm finds (a, b, c) such that <code>this * a + val * b = c = this.gcd(val)</code>.
     *
     * @param val a number
     * @return An array of length 3 containing the values a, b, c at respective positions 0, 1, 2
     */
    public BigInt[] euclidExtended(BigInt val) {
        BigInt p = ONE,
                q = ZERO,
                a = this,
                r = ZERO,
                s = ONE,
                b = val;
        while (b.signum() != 0) {
            BigInt quot = a.divide(b);
            BigInt t1 = p.subtract(quot.multiply(r));
            BigInt t2 = q.subtract(quot.multiply(s));
            BigInt t3 = a.subtract(quot.multiply(b));
            p = r;
            q = s;
            a = b;
            r = t1;
            s = t2;
            b = t3;
        }
        return new BigInt[]{p, q, a};
    }

    /**
     * Returns a BigInteger whose value is <tt>x = this<sup>-1</sup> mod m, this*x mod m = 1</tt>.
     *
     * @param m the modulus.
     * @return <tt>this<sup>-1</sup> mod m</tt>.
     * @throws ArithmeticException <tt> m &lt;= 0</tt>, or this BigInteger
     *                             has no multiplicative inverse mod m (that is, this BigInteger
     *                             is not <i>relatively prime</i> to m).
     */
    public BigInt modInverse(BigInt m) {
        BigInt[] euclid = euclidExtended(m);
        if (euclid[2].equals(ONE))
            return euclid[0].signum() < 0 ? euclid[0].add(m) : euclid[0];
        if (euclid[2].equals(ONE.opposite()))
            return m.subtract(euclid[0]);
        throw new ArithmeticException("BigInt not invertible: gcd(" + this + ", " + m + ") = " + euclid[2]);
    }

    /**
     * Returns a BigInteger whose value is <tt>x = this<sup>-1</sup> mod m, this*x mod m = 1</tt>.
     *
     * @param m the modulus.
     * @return <tt>this<sup>-1</sup> mod m</tt>.
     * @throws ArithmeticException <tt> m &lt;= 0</tt>, or this BigInteger
     *                             has no multiplicative inverse mod m (that is, this BigInteger
     *                             is not <i>relatively prime</i> to m).
     */
    public BigInt modInverse(long m) {
        return modInverse(big(m));
    }

    /**
     * Returns a BigInteger whose value is
     * <tt>(this<sup>exponent</sup> mod m)</tt>.  (Unlike {@code pow}, this
     * method permits negative exponents.)
     *
     * @param exponent the exponent.
     * @param m        the modulus.
     * @return <tt>this<sup>exponent</sup> mod m</tt>
     * @throws ArithmeticException {@code m <= 0}
     * @see #modInverse
     */
    public BigInt modPow(BigInt exponent, BigInt m) {
        return mod(m).pow(exponent).mod(m);
    }

    /**
     * Returns a BigInteger whose value is
     * <tt>(this<sup>exponent</sup> mod m)</tt>.  (Unlike {@code pow}, this
     * method permits negative exponents.)
     *
     * @param exponent the exponent.
     * @param m        the modulus.
     * @return <tt>this<sup>exponent</sup> mod m</tt>
     * @throws ArithmeticException {@code m <= 0}
     * @see #modInverse
     */
    public BigInt modPow(long exponent, long m) {
        return modPow(big(exponent), big(m));
    }

    /**
     * Returns a BigInteger whose value is {@code (this * val) % m}.
     *
     * @param val value by which this BigInteger is to be multiplied
     * @param m   the modulus.
     * @return {@code (this * val) % m}
     */
    public BigInt modMultiply(BigInt val, BigInt m) {
        return mod(m).multiply(val.mod(m)).mod(m);
    }

    /**
     * Returns a BigInteger whose value is {@code (this * val) % m}.
     *
     * @param val value by which this BigInteger is to be multiplied
     * @param m   the modulus.
     * @return {@code (this * val) % m}
     */
    public BigInt modMultiply(long val, long m) {
        return modMultiply(big(val), big(m));
    }

    /**
     * Returns a BigInteger whose value is {@code (this + val) % m}.
     *
     * @param val value by which this BigInteger is to be added
     * @param m   the modulus.
     * @return {@code (this + val) % m}
     */
    public BigInt modAdd(BigInt val, BigInt m) {
        return mod(m).add(val.mod(m)).mod(m);
    }

    /**
     * Returns a BigInteger whose value is {@code (this + val) % m}.
     *
     * @param val value by which this BigInteger is to be added
     * @param m   the modulus.
     * @return {@code (this + val) % m}
     */
    public BigInt modAdd(long val, long m) {
        return modAdd(big(val), big(m));
    }

    /**
     * Returns a BigInteger whose value is the greatest common divisor of
     * given numbers, using {@link #gcd(BigInt, BigInt[])}
     *
     * @param val value with which the GCD is to be computed.
     * @return {@code GCD(abs(this), abs(val))}
     */
    public BigInt gcd(BigInt val) {
        if (signum() == 0) return val;
        if (val.signum() == 0) return this;
        return abs().euclidExtended(val.abs())[2];
    }

    /**
     * Returns a BigInteger whose value is the greatest common divisor of
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val    value with which the GCD is to be computed.
     * @param others Additional numbers to include in GCD computation
     * @return {@code GCD(abs(this), abs(val), ...)}
     */
    public BigInt gcd(BigInt val, BigInt... others) {
        BigInt gcd = gcd(val);
        for (int i = others.length - 1; i >= 0; i--)
            gcd = gcd.gcd(others[i]);
        return gcd;
    }

    /**
     * Returns a BigInteger whose value is the least common multiple of
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val value with which the GCD is to be computed.
     * @return {@code GCD(abs(this), abs(val))}
     */
    public BigInt lcm(BigInt val) {
        return divide(gcd(val)).multiply(val);
    }

    /**
     * Returns a BigInteger whose value is the least common multiple of
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val    value with which the GCD is to be computed.
     * @param others Other numbers
     * @return {@code GCD(abs(this), abs(val), ...)}
     */
    public BigInt lcm(BigInt val, BigInt... others) {
        BigInt lcm = lcm(val);
        for (int i = others.length - 1; i >= 0; i--)
            lcm = lcm.lcm(others[i]);
        return lcm;
    }

    /**
     * Concatenate positive numbers
     *
     * @param numbers Numbers to concatenate to this number
     * @return The concatenated number
     */
    public BigInt concat(long... numbers) {
        if (numbers.length == 0) return this;
        StringBuilder sb = new StringBuilder().append(numbers[0]);
        for (int i = 1, max = numbers.length; i < max; i++)
            sb.append(numbers[i]);
        return concat(big(sb.toString(), radix()));
    }

    /**
     * Concatenate positive numbers
     *
     * @param numbers Numbers to concatenate to this number
     * @return The concatenated number
     */
    public BigInt concat(BigInt... numbers) {
        if (numbers.length == 0) return this;
        StringBuilder sb = new StringBuilder().append(toString());
        for (int i = 0, max = numbers.length; i < max; i++)
            sb.append(numbers[i].internal.toString());
        return big(sb.toString(), radix());
    }

    protected int digitsCount = -1;

    /**
     * Get the number of digits. Note: 0 as a length of 1.
     *
     * @return Its length
     */
    public int digitsCount() {
        return digitsCount == -1 ? (digitsCount = toString().length()) : digitsCount;
    }

    /**
     * Sort the digits of a number
     *
     * @return Another number with the same digits, sorted
     */
    public BigInt sort() {
        final char c[] = toString().toCharArray();
        Arrays.sort(c);
        return big(String.valueOf(c), radix());
    }

    protected int[] digits;

    /**
     * List all digits of a number in this base, in descending order of powers.
     * <p/>
     * I.e. 123 gives {1, 2, 3}
     *
     * @return the list of digits
     */
    public int[] digits() {
        if (digits != null) return Arrays.copyOf(digits, digits.length);
        final String s = toString();
        final int[] digits = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--)
            digits[i] = s.charAt(i) - 48;
        return this.digits = digits;
    }

    protected int digitsSum = -1;

    /**
     * Returns the digit sum of a number in this base
     *
     * @return the digit sum
     */
    public int digitsSum() {
        if (digitsSum != -1) return digitsSum;
        int sum = 0;
        final String s = toString();
        for (int i = s.length() - 1; i >= 0; i--)
            sum += s.charAt(i) - 48;
        return this.digitsSum = sum;
    }

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Digital_root">Digital Root</a> of this number
     *
     * @return The <a href="http://en.wikipedia.org/wiki/Digital_root">Digital Root</a> of this number
     */
    public int digitalRoot() {
        if (signum() == 0) return 0;
        return ONE.add(this.subtract(ONE).mod(9)).toInt();
    }


    /**
     * Reverse the digits of a number
     *
     * @return the reversed number
     */
    public BigInt digitsReversed() {
        final String s = toString();
        final int max = s.length() - 1;
        final char chars[] = new char[max + 1];
        for (int i = 0; i <= max; i++)
            chars[i] = s.charAt(max - i);
        return big(String.valueOf(chars), radix());
    }

    /**
     * Rotate digits of a number.<br>
     * - The rotation direction is specified by the sign of offset<br>
     * - The rotation length is determined by the value of the offset
     * <p/>
     * I.e., rotate(1234, 3) and rotate(1234, -1) will both give 2341
     *
     * @param offset The direction and length of the rotation
     * @return The rotated number
     */
    public BigInt digitsRotated(int offset) {
        if (offset == 0) return this;
        final String s = toString();
        final int len = s.length();
        offset %= s.length();
        offset %= len;
        if (offset == 0) return this;
        if (offset < 0) offset = len + offset;
        return big(s.substring(len - offset) + s.substring(0, len - offset), radix());
    }

    protected int[] digitsSignature;

    /**
     * Returns the signature of a number. The signature is composed of all the digit of the number, sorted.
     *
     * @return Its digit list
     */
    public int[] digitsSignature() {
        if (digitsSignature != null) return Arrays.copyOf(digitsSignature, digitsSignature.length);
        final String s = toString();
        final int[] digits = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--)
            digits[i] = s.charAt(i) - 48;
        Arrays.sort(digits);
        return digitsSignature = digits;
    }

    protected Boolean isPalindromic;

    /**
     * Check if the number is a palyndrom
     *
     * @return true if it is
     */
    public boolean isPalindromic() {
        if (isPalindromic != null) return isPalindromic;
        final String s = toString();
        final int len = s.length() - 1;
        for (int i = (s.length() - 1) >>> 1; i >= 0; i--)
            if (s.charAt(i) != s.charAt(len - i))
                return isPalindromic = false;
        return isPalindromic = true;
    }

    /**
     * Check wheter the given numbers is a permutations of the digits of this number
     *
     * @param val The  number to check
     * @return True if the numbers is a permutation of number
     */
    public boolean isPermutation(BigInt val) {
        return Arrays.equals(this.digitsSignature(), val.digitsSignature());
    }

    /**
     * Returns the integer square root of this integer.
     *
     * @return <code>k<code> such as <code>k^2 <= this < (k + 1)^2</code>
     * @throws ArithmeticException if this integer is negative.
     */
    public BigInt sqrt() {
        return sqrtAndRemainder()[0];
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     *
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>2</sup> + r = number</code>.
     */
    public BigInt[] sqrtAndRemainder() {
        if (signum() < 0)
            throw new ArithmeticException("Square root of negative integer");
        if (signum() == 0 || equals(ONE))
            return new BigInt[]{this, ZERO};
        BigInt lastGuess = ZERO;
        BigInt guess = ONE.shiftLeft(bitLength() >>> 1);
        BigInt test = lastGuess.subtract(guess);
        BigInt remainder = subtract(guess.square());
        while (test.signum() != 0 && !test.equals(ONE) || remainder.signum() < 0) {
            lastGuess = guess;
            guess = divide(guess).add(lastGuess).shiftRight(1);
            test = lastGuess.subtract(guess);
            remainder = subtract(guess.square());
        }
        return new BigInt[]{guess, remainder};
    }

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return q such that q is the maximum number so that <code>q<sup>root</sup> <= number</code>.
     */
    public BigInt root(BigInt root) {
        return rootAndRemainder(root)[0];
    }

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return q such that q is the maximum number so that <code>q<sup>root</sup> <= number</code>.
     */
    public BigInt root(long root) {
        return root(big(root));
    }

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>root</sup> + r = this</code>.
     */
    public BigInt[] rootAndRemainder(BigInt root) {
        if (root.signum() <= 0 || signum() < 0)
            throw new ArithmeticException("Root and this number must be strictly positive");
        if (signum() == 0) return new BigInt[]{ZERO, ZERO};
        if (equals(ONE)) return new BigInt[]{ONE, ZERO};

        BigInt lastGuess = this;
        BigInt guess = root.bitLength() <= 31 ?
                ONE.shiftLeft(bitLength() / root.toInt()) :
                TWO.pow(big(bitLength()).divide(root).subtract(1));
        BigInt test = lastGuess.subtract(guess);
        BigInt remainder = subtract(guess.pow(root));
        BigInt rootMin1 = root.subtract(ONE);
        while (test.signum() != 0 && !test.equals(ONE) || remainder.signum() < 0) {
            lastGuess = guess;
            guess = rootMin1.multiply(guess).add(lastGuess.divide(guess.pow(rootMin1))).divide(guess);
            test = lastGuess.subtract(guess);
            remainder = subtract(guess.pow(root));
        }
        return new BigInt[]{guess, remainder};

        BigInt prev = this;


        double x_prev = A;
        double x = A / n;  // starting "guessed" value...
        while (Math.abs(x - x_prev) > p) {
            x_prev = x;
            x = ((n - 1.0) * x + A / Math.pow(x, n - 1.0)) / n;
        }
        return x;



        while (low.compareTo(this) > 0)
            low = low.shiftRight(1);
        BigInt high = low;
        while (high.compareTo(this) < 0)
            high = high.shiftLeft(1);
        BigInt mid = ZERO;
        while (low.compareTo(high) < 0) {
            mid = low.add(high).shiftRight(1).add(ONE);
            BigInt remainder = subtract(mid.pow(root));
            if (low.compareTo(mid) < 0 && remainder.signum() > 0)
                low = mid;
            else if (high.compareTo(mid) > 0 && remainder.signum() < 0)
                high = mid;
            else
                return new BigInt[]{mid, remainder};
        }
        mid = mid.add(ONE);
        return new BigInt[]{mid, subtract(mid.pow(root))};
    }

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>root</sup> + r = number</code>.
     */
    public BigInt[] rootAndRemainder(long root) {
        return rootAndRemainder(big(root));
    }

    /**
     * Compute <code>this * this</code>
     *
     * @return The square of this number
     */
    //public abstract BigInt square();

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Binomial_coefficient">Binomial Coefficient</a>
     * <code>C(this, k)</code>
     *
     * @param k Coefficient
     * @return The binomial coefficient
     */
    //public abstract BigInt binomialCoeff(int k);

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Factorial">Factorial of this number</a>
     * <code>this!</code>
     *
     * @return this!
     */
    //public abstract BigInt factorial();

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">Falling Factorial</a>
     * <code>this! / (this-n)!</code>
     *
     * @param n The falling factor to substract from this number
     * @return this! / (this-n)!
     */
    //public abstract BigInt fallingFactorial(BigInt n);

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">Falling Factorial</a>
     * <code>this! / (this-n)!</code>
     *
     * @param n The falling factor to substract from this number
     * @return this! / (this-n)!
     */
    //public abstract BigInt fallingFactorial(long n);

    /**
     * Computes the sum of the consecutive numbers from this to n
     * <code>S = this + (this+1) + ... + n
     *
     * @param n The limit which will determine
     *          the direction of the sum, from this to n or n to this
     * @return the consecutive sum
     */
    //public abstract BigInt sumTo(BigInt n);

    /**
     * Computes the sum of the consecutive numbers from this to n
     * <code>S = this + (this+1) + ... + n
     *
     * @param n The limit which will determine
     *          the direction of the sum, from this to n or n to this
     * @return the consecutive sum
     */
    //public abstract BigInt sumTo(long n);

    /**
     * Computes the product of the consecutive numbers from this to n
     * <code>S = this * (this+1) * ... * n
     *
     * @param n The limit which will determine
     *          the direction of the product, from this to n or n to this
     * @return the consecutive product
     */
    //public abstract BigInt productTo(BigInt n);

    /**
     * Computes the product of the consecutive numbers from this to n
     * <code>S = this * (this+1) * ... * n
     *
     * @param n The limit which will determine
     *          the direction of the product, from this to n or n to this
     * @return the consecutive product
     */
    //public abstract BigInt productTo(long n);

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test">Miller-Rabin primality test<a/>.
     * <p/>
     * If the BigInteger passes all tests, returns the probabilty it is prime as a double. Number of passes is set to 50
     *
     * @return zero if the BigInteger is determined to be composite.
     */
    public double isPrimeMillerRabin() {
        return isPrimeMillerRabin(50);
    }

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test">Miller-Rabin primality test<a/>.
     * <p/>
     * If the BigInteger passes all tests, returns the probabilty it is prime as a double.
     *
     * @param numPasses Number of different bases to try is passed in as an int
     * @return zero if the BigInteger is determined to be composite.
     */
    public double isPrimeMillerRabin(int numPasses) {
        if (compareTo(TWO) < 0) return 0;
        if (compareTo(big(Integer.MAX_VALUE)) <= 0)
            return millerRabinInt(toInt()) ? 1.0 : 0.0;
        BigInt b, x;
        BigInt nMinusOne = subtract(ONE);
        if (numPasses < 1)
            throw new IllegalArgumentException("Number of bases must be positive!");
        for (int i = 0; i < numPasses; i++) {
            b = randomBig(bitLength() - 1);
            x = b.modPow(nMinusOne, this);
            if (!x.equals(ONE)) return 0.0;
            BigInt[] dr = nMinusOne.divideAndRemainder(TWO);
            while (dr[1].signum() == 0) {
                x = b.modPow(dr[0], this);
                if (x.equals(nMinusOne)) break;
                if (!x.equals(ONE)) return 0.0;
                dr = dr[0].divideAndRemainder(TWO);
            }
        }
        return 1.0 - Math.pow(0.25, numPasses);
    }

    private static boolean millerRabinInt(int number) {
        return number > 1
                && (number == 2
                || millerRabinPass(2, number)
                && (number <= 7 || millerRabinPass(7, number))
                && (number <= 61 || millerRabinPass(61, number)));
    }

    private static boolean millerRabinPass(final int a, final int n) {
        int d = n - 1;
        int s = Integer.numberOfTrailingZeros(d);
        d >>>= s;
        int a_to_power = big(a).pow(d).mod(n).toInt();
        s--;
        if (a_to_power == 1) return true;
        for (int i = 0; i < s; i++) {
            if (a_to_power == n - 1) return true;
            a_to_power = big(a_to_power).square().mod(n).toInt();
        }
        return a_to_power == n - 1;
    }

    /**
     * Check wheter this number is a Mersenne Prime <code>Mp = 2^this - 1</code>
     * <p/>
     * Using <a href="http://en.wikipedia.org/wiki/Lucas%E2%80%93Lehmer_test_for_Mersenne_numbers">Lucas-Lehmer primality test<a/>
     *
     * @return the probabilty it is prime. O if the BigInteger is determined to be composite, 1 if it is prime
     */
    public boolean isPrimeLucasLehmer() {
        BigInt p = this.subtract(TWO);
        if (p.signum() == 0) return true;
        final BigInt m = TWO.pow(this).subtract(ONE);
        BigInt s = big(4);
        for (BigInt i = ZERO; i.compareTo(p) < 0; i = i.add(1))
            s = s.multiply(s).subtract(TWO).mod(m);
        return s.equals(ZERO);
    }

}

//TODO: add methods: factorial(), factorize(), fibonacci(), isFibonacci(), digitMap + each digits from Digits, recurringCycle, panDigitalRange, isPandifital,
//TODO: http://en.wikipedia.org/wiki/AKS_primality_test + ZIP AKS
//TODO: http://en.wikipedia.org/wiki/Adleman%E2%80%93Pomerance%E2%80%93Rumely_primality_test + ECM pour APR-CL
//TODO: http://en.wikipedia.org/wiki/Elliptic_curve_primality_proving + ECM applet
