package other;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BigSqrt {

    private static final BigDecimal ZERO = new BigDecimal("0");
    private static final BigDecimal ONE = new BigDecimal("1");
    private static final BigDecimal TWO = new BigDecimal("2");

    public static final int DEFAULT_MAX_ITERATIONS = 50;
    public static final int DEFAULT_SCALE = 10;

    private final BigDecimal number;
    private BigDecimal error = ZERO;
    private int iterations;

    private BigSqrt(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal error() {
        return error;
    }

    public BigDecimal number() {
        return number;
    }

    public int iterations() {
        return iterations;
    }

    public BigDecimal get() {
        return get(DEFAULT_SCALE, DEFAULT_MAX_ITERATIONS);
    }

    public BigDecimal get(int scale, int maxIterations) {
        if (number.compareTo(ZERO) <= 0) throw new IllegalArgumentException();
        BigDecimal initialGuess = getInitialApproximation(number);
        BigDecimal lastGuess;
        BigDecimal guess = new BigDecimal(initialGuess.toString());
        iterations = 0;
        boolean more = true;
        while (more) {
            lastGuess = guess;
            guess = number.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
            guess = guess.add(lastGuess);
            guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
            error = number.subtract(guess.multiply(guess));
            if (++iterations >= maxIterations) more = false;
            else if (lastGuess.equals(guess)) more = error.abs().compareTo(ONE) >= 0;
        }
        return guess;
    }

    private static BigDecimal getInitialApproximation(BigDecimal n) {
        BigInteger integerPart = n.toBigInteger();
        int length = integerPart.toString().length();
        if ((length & 1) == 0) length--;
        length >>= 1;
        return ONE.movePointRight(length);
    }

    public static BigSqrt sqrt(BigDecimal bigDecimal) {
        return new BigSqrt(bigDecimal);
    }

    public static BigSqrt sqrt(BigInteger bigInteger) {
        return new BigSqrt(new BigDecimal(bigInteger));
    }

    static class Test {
        public static void main(String[] args) {
            assert sqrt(new BigInteger("4091003901585987357290452999329796564377935868371101464249814903417070889078692626733675441097647521")).get().toString().equals("63960956071544047529338853709828067819172266042961");
            assert sqrt(new BigInteger("4091003901585987357290452999329796564377935868371155936881162018216821980804517176141145508100318254")).get().toString().equals("63960956071544047529338853709828067819172266042961.4258272132");
        }
    }

}
