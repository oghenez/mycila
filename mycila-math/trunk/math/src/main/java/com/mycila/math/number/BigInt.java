package com.mycila.math.number;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: as classes are moved to math package, add methods to this class
//TODO: add methods: factorize(), fibonacci(), isFibonacci(), digitMap, recurringCycle, panDigitalRange, isPandifital,  
public abstract class BigInt<T> implements Comparable<BigInt> {

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

    public final T internal;

    protected BigInt(T internal) {
        this.internal = internal;
    }

    @SuppressWarnings({"unchecked"})
    protected T impl(Object o) {
        return (T) o;
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

    @Override
    public abstract String toString();

    public abstract String toString(int tradix);

    public abstract int toInt();

    public abstract long toLong();

    public abstract byte toByte();

    public abstract short toShort();

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
     * Returns the signum function of this BigInteger.
     *
     * @return -1, 0 or 1 as the value of this BigInteger is negative, zero or
     *         positive.
     */
    public int signum() {
        int comp = this.compareTo(zero());
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
        return !zero().equals(this.and(one().shiftLeft(n)));
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
        return this.multiply(two().pow(n));
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
        return this.divide(two().pow(n));
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
        return this.and(one().shiftLeft(n).not());
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
        return this.xor(one().shiftLeft(n).not());
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
        return this.or(one().shiftLeft(n).not());
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
     * Returns a BigInteger whose value is <tt>x = this<sup>-1</sup> mod m, this*x mod m = 1</tt>.
     *
     * @param m the modulus.
     * @return <tt>this<sup>-1</sup> mod m</tt>.
     * @throws ArithmeticException <tt> m &lt;= 0</tt>, or this BigInteger
     *                             has no multiplicative inverse mod m (that is, this BigInteger
     *                             is not <i>relatively prime</i> to m).
     */
    public BigInt modInverse(BigInt m) {//FIXME: use extended euclide http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
        // Extended Euclidean algorithm
        if (m.signum() < 0)
            throw new ArithmeticException("Modulus is not a positive number");
        BigInt a = this;
        BigInt b = m;
        BigInt p = one();
        BigInt q = zero();
        BigInt r = zero();
        BigInt s = one();
        while (b.signum() != 0) {
            BigInt[] qr = a.divideAndRemainder(b);
            a = b;
            b = qr[1];
            BigInt newR = p.subtract(qr[0].multiply(r));
            BigInt newS = q.subtract(qr[0].multiply(s));
            p = r;
            q = s;
            r = newR;
            s = newS;
        }
        if (!a.abs().equals(one())) // (a != 1) || (a != -1)
            throw new ArithmeticException("GCD(" + this + ", " + m + ") = " + a);
        return a.signum() == -1 ? p.opposite().mod(m) : p.mod(m);

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
     * {@code abs(this)} and {@code abs(val)}.  Returns 0 if
     * {@code this==0 && val==0}.
     *
     * @param val    value with which the GCD is to be computed.
     * @param others Other numbers
     * @return {@code GCD(abs(this), abs(val), ...)}
     */
    public abstract BigInt gcd(BigInt val, BigInt... others); //FIXME - jsciense LargeInteger

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
     * Concatenate positive numbers
     *
     * @param numbers Numbers to concatenate to this number
     * @return The concatenated number
     */
    public abstract BigInt concat(long... numbers);

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

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>root</sup> + r = number</code>.
     */
    public abstract BigInt[] rootInt(BigInt root);

    /**
     * Compute the integer root q of a number so that q^root + r = number
     *
     * @param root The root to compute
     * @return An array of two BigIntegers: <code>[q, r]</code>, where <code>q<sup>root</sup> + r = number</code>.
     */
    public abstract BigInt[] rootInt(long root);

    /**
     * Compute <code>this * this</code>
     *
     * @return The square of this number
     */
    public abstract BigInt square();

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Binomial_coefficient">Binomial Coefficient</a>
     * <code>C(this, k)</code>
     *
     * @param k Coefficient
     * @return The binomial coefficient
     */
    public abstract BigInt binomialCoeff(int k);

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Factorial">Factorial of this number</a>
     * <code>this!</code>
     *
     * @return this!
     */
    public abstract BigInt factorial();

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">Falling Factorial</a>
     * <code>this! / (this-n)!</code>
     *
     * @param n The falling factor to substract from this number
     * @return this! / (this-n)!
     */
    public abstract BigInt fallingFactorial(BigInt n);

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">Falling Factorial</a>
     * <code>this! / (this-n)!</code>
     *
     * @param n The falling factor to substract from this number
     * @return this! / (this-n)!
     */
    public abstract BigInt fallingFactorial(long n);

    /**
     * Computes the sum of the consecutive numbers from this to n
     * <code>S = this + (this+1) + ... + n
     *
     * @param n The limit which will determine
     *          the direction of the sum, from this to n or n to this
     * @return the consecutive sum
     */
    public abstract BigInt sumTo(BigInt n);

    /**
     * Computes the sum of the consecutive numbers from this to n
     * <code>S = this + (this+1) + ... + n
     *
     * @param n The limit which will determine
     *          the direction of the sum, from this to n or n to this
     * @return the consecutive sum
     */
    public abstract BigInt sumTo(long n);

    /**
     * Computes the product of the consecutive numbers from this to n
     * <code>S = this * (this+1) * ... * n
     *
     * @param n The limit which will determine
     *          the direction of the product, from this to n or n to this
     * @return the consecutive product
     */
    public abstract BigInt productTo(BigInt n);

    /**
     * Computes the product of the consecutive numbers from this to n
     * <code>S = this * (this+1) * ... * n
     *
     * @param n The limit which will determine
     *          the direction of the product, from this to n or n to this
     * @return the consecutive product
     */
    public abstract BigInt productTo(long n);

    /* PRIMALTY */

    //TODO: keep this methods or use PrimaltyTest ?

    /**
     * Returns {@code true} if this BigInteger is prime,
     * {@code false} if it's definitely composite.
     *
     * @return the probabilty it is prime. O if the BigInteger is determined to be composite, 1 if it is prime
     */
    //public abstract double isPrime();

    /**
     * Check wheter this number is a Mersenne Prime <code>Mp = 2^this - 1</code>
     *
     * @return the probabilty it is prime. O if the BigInteger is determined to be composite, 1 if it is prime
     */
    //public abstract double isMersennePrime();

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
    //public abstract BigInt nextPrime();

}
