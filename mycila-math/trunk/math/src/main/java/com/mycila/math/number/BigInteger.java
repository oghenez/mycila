package com.mycila.math.number;

import com.mycila.math.number.jdk.JDKBigIntegerFactory;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class BigInteger<T> {

    private static BigIntegerFactory factory;

    static {
        setBigIntegerFactory(new JDKBigIntegerFactory());
    }

    public static void setBigIntegerFactory(BigIntegerFactory factory) {
        BigInteger.factory = factory;
    }

    public static BigInteger bigInt(long number) {
        return factory.create(number);
    }

    public static BigInteger bigInt(String number) {
        return factory.create(number);
    }

    public static BigInteger bigInt(String number, int radix) {
        return factory.create(number, radix);
    }

    public static BigInteger zero() {
        return factory.get(0);
    }

    public static BigInteger one() {
        return factory.get(1);
    }

    public static BigInteger two() {
        return factory.get(2);
    }

    public static BigInteger ten() {
        return factory.get(10);
    }

    protected final T impl;

    protected BigInteger(T impl) {
        this.impl = impl;
    }

    public abstract String toString(int tradix);

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
        return o.getClass() == getClass() && impl.equals(((BigInteger<T>) o).impl);
    }

    @Override
    public final String toString() {
        return impl.toString();
    }

    public abstract int toInt();

}
