package com.mycila.math.number;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: add sqrt(), square(), binomial, fact, fallingFact, ...
public abstract class BigInt implements Comparable<BigInt> {

    private static BigIntFactory factory;

    static {
        try {
            String cname = System.getProperty("mycila.bigint.factory");
            if (cname == null) cname = "com.mycila.math.number.jdk.JDKBigIntFactory";
            @SuppressWarnings({"unchecked"})
            Class<BigIntFactory> fClass = (Class<BigIntFactory>) Thread.currentThread().getContextClassLoader().loadClass(cname);
            if (!BigIntFactory.class.isAssignableFrom(fClass))
                throw new RuntimeException(cname + " does not implement " + BigIntFactory.class.getName());
            factory = fClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static BigInt big(long number) {
        return factory.create(number);
    }

    public static BigInt big(String number) {
        return factory.create(number);
    }

    public static BigInt big(String number, int radix) {
        return factory.create(number, radix);
    }

    public static BigInt wrap(Object internal) {
        return factory.wrap(internal);
    }

    public static BigInt random(int length) {
        return factory.random(length);
    }

    public static BigInt zero() {
        return factory.create(0);
    }

    public static BigInt one() {
        return factory.create(1);
    }

    public static BigInt two() {
        return factory.create(2);
    }

    public static BigInt ten() {
        return factory.create(10);
    }

    private final Object impl;

    protected BigInt(Object impl) {
        this.impl = impl;
    }

    @SuppressWarnings({"unchecked"})
    public final <T> T internal() {
        return (T) impl;
    }

    @Override
    public final int hashCode() {
        return impl.hashCode();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == impl.getClass()) return impl.equals(o);
        return o.getClass() == getClass() && impl.equals(((BigInt) o).impl);
    }

    /* CONVERSIONS */

    @Override
    public abstract String toString();

    public abstract String toString(int tradix);

    public abstract int toInt();

    public abstract long toLong();

    public abstract byte toByte();

    /* INFO */

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
     * Returns the signum function of this BigInteger.
     *
     * @return -1, 0 or 1 as the value of this BigInteger is negative, zero or
     *         positive.
     */
    public abstract int signum();

    /**
     * Returns {@code true} if and only if the designated bit is set.
     * (Computes {@code ((this & (1<<n)) != 0)}.)
     *
     * @param n index of bit to test.
     * @return {@code true} if and only if the designated bit is set.
     * @throws ArithmeticException {@code n} is negative.
     */
    public abstract boolean testBit(int n);

    /* BIT MANIPULATION */

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
    public abstract BigInt shiftLeft(int n);

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
    public abstract BigInt shiftRight(int n);

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
     * Returns a BigInteger whose value is {@code (this & ~val)}.  This
     * method, which is equivalent to {@code and(val.not())}, is provided as
     * a convenience for masking operations.  (This method returns a negative
     * BigInteger if and only if {@code this} is negative and {@code val} is
     * positive.)
     *
     * @param n value to be complemented and AND'ed with this BigInteger.
     * @return {@code this & ~val}
     */
    public abstract BigInt andNot(BigInt n);

    /**
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit cleared.
     * (Computes {@code (this & ~(1<<n))}.)
     *
     * @param n index of bit to clear.
     * @return {@code this & ~(1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public abstract BigInt clearBit(int n);

    /**
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit flipped.
     * (Computes {@code (this ^ (1<<n))}.)
     *
     * @param n index of bit to flip.
     * @return {@code this ^ (1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public abstract BigInt flipBit(int n);

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
     * Returns a BigInteger whose value is equivalent to this BigInteger
     * with the designated bit set.  (Computes {@code (this | (1<<n))}.)
     *
     * @param n index of bit to set.
     * @return {@code this | (1<<n)}
     * @throws ArithmeticException {@code n} is negative.
     */
    public abstract BigInt setBit(int n);

    /* BASICS */

    /**
     * Returns a BigInteger whose value is <tt>(this<sup>exponent</sup>)</tt>.
     * Note that {@code exponent} is an integer rather than a BigInteger.
     *
     * @param exponent exponent to which this BigInteger is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>
     * @throws ArithmeticException {@code exponent} is negative.  (This would
     *                             cause the operation to yield a non-integer value.)
     */
    public abstract BigInt pow(int exponent);

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
    public abstract BigInt[] divideAndRemainder(BigInt val);

    /* OTHERS */

    /**
     * Returns a BigInteger whose value is the absolute value of this
     * BigInteger.
     *
     * @return {@code abs(this)}
     */
    public abstract BigInt abs();

    /**
     * Returns a BigInteger whose value is {@code (-this)}.
     *
     * @return {@code -this}
     */
    public abstract BigInt negate();

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
     * Returns the maximum of this BigInteger and {@code val}.
     *
     * @param val value with which the maximum is to be computed.
     * @return the BigInteger whose value is the greater of this and
     *         {@code val}.  If they are equal, either may be returned.
     */
    public abstract BigInt max(BigInt val);

    /**
     * Returns the minimum of this BigInteger and {@code val}.
     *
     * @param val value with which the minimum is to be computed.
     * @return the BigInteger whose value is the lesser of this BigInteger and
     *         {@code val}.  If they are equal, either may be returned.
     */
    public abstract BigInt min(BigInt val);

    /* MODULO OPERATIONS */

    /**
     * Returns a BigInteger whose value is {@code (thismodm}).  This method
     * differs from {@code remainder} in that it always returns a
     * <i>non-negative</i> BigInteger.
     *
     * @param m the modulus.
     * @return {@code this mod m}
     * @throws ArithmeticException {@code m <= 0}
     */
    public abstract BigInt mod(BigInt m);

    /**
     * Returns a BigInteger whose value is {@code (this % val)}.
     *
     * @param val value by which this BigInteger is to be divided, and the
     *            remainder computed.
     * @return {@code this % val}
     * @throws ArithmeticException {@code val==0}
     */
    public abstract BigInt remainder(BigInt val);

    /**
     * Returns a BigInteger whose value is {@code (this}<sup>-1</sup> {@codemodm)}.
     *
     * @param m the modulus.
     * @return {@code this}<sup>-1</sup> {@code mod m}.
     * @throws ArithmeticException {@code m <= 0}, or this BigInteger
     *                             has no multiplicative inverse mod m (that is, this BigInteger
     *                             is not <i>relatively prime</i> to m).
     */
    public abstract BigInt modInverse(BigInt m);

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
    public abstract BigInt modPow(BigInt exponent, BigInt m);

    /* DIVISORS */

    /**
     * Returns a BigInteger whose value is the greatest common divisor of
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val    value with which the GCD is to be computed.
     * @param others Other numbers
     * @return {@code GCD(abs(this), abs(val), ...)}
     */
    public abstract BigInt gcd(BigInt val, BigInt... others);

    /**
     * Returns a BigInteger whose value is the least common multiple of
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val    value with which the GCD is to be computed.
     * @param others Other numbers
     * @return {@code GCD(abs(this), abs(val), ...)}
     */
    public abstract BigInt lcm(BigInt val, BigInt... others);

    /* PRIMALTY */

    /**
     * Returns {@code true} if this BigInteger is prime,
     * {@code false} if it's definitely composite.
     *
     * @return {@code true} if this BigInteger is  prime,
     *         {@code false} if it's definitely composite.
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

    /**
     * Convert a number to the given radix.
     *
     * @param radix new radix
     * @return this number is the new radix
     */
    public abstract BigInt toRadix(int radix);

    /**
     * Concatenate positive numbers
     *
     * @param numbers Numbers to concatenate to this number
     * @return The concatenated number
     */
    public abstract BigInt concat(BigInt... numbers);

    /**
     * Get the number of digits. Note: 0 as a length of 1.
     *
     * @return Its length
     */
    public abstract int length();

    /**
     * Sort the digits of a number
     *
     * @return Another number with the same digits, sorted
     */
    public abstract BigInt sort();

    /**
     * List all digits of a number in this base, in descending order of powers.
     * <p/>
     * I.e. 123 gives {1, 2, 3}
     *
     * @return the list of digits
     */
    public abstract int[] digits();

    /**
     * Returns the digit sum of a number in this base
     *
     * @return the digit sum
     */
    public abstract int digitsSum();

    /**
     * Reverse the digits of a number
     *
     * @return the reversed number
     */
    public abstract BigInt reverseDigits();

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
    public abstract BigInt rotateDigits(int offset);

    /**
     * Returns the signature of a number. The signature is composed of all the digit of the number, sorted.
     *
     * @return Its digit list
     */
    public abstract int[] digitsSignature();

    /**
     * Check if the number is a palyndrom
     *
     * @return true if it is
     */
    public abstract boolean isPalindromic();

    /**
     * Check wheter the given numbers is a permutations of the digits of this number
     *
     * @param val The  number to check
     * @return True if the numbers is a permutation of number
     */
    public abstract boolean isPermutation(BigInt val);

    /**
     * Get the radix in which the number is represented for
     *
     * @return the radix
     */
    public abstract int radix();

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">Extended Euclidean algorithm</a>.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses <a href="http://en.literateprograms.org/Extended_Euclidean_algorithm_(Python)">this algorithm</a>
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * For 'this' and 'val', this algorithm finds (a, b, c) such that <code>this * a + val * b = c = this.gcd(val)</code>.
     *
     * @param val a number
     * @return An array of length 3 containing the values a, b, c at respective positions 0, 1, 2
     */
    public abstract BigInt[] euclidExtended(BigInt val);

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     *
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>2</sup> + r = number</code>.
     */
    public abstract BigInt[] sqrtInt();
}
