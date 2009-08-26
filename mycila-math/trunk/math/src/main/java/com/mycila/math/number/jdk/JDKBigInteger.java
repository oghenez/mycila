package com.mycila.math.number.jdk;

import com.mycila.math.number.BigInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: use JDKBigInteger wrapper everywhere
final class JDKBigInteger extends BigInteger<java.math.BigInteger> {

    JDKBigInteger(java.math.BigInteger bigInteger) {
        super(bigInteger);
    }

    @Override
    public String toString(int tradix) {
        return impl.toString(tradix);
    }

    @Override
    public int toInt() {
        return impl.intValue();
    }

}
