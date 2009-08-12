package com.mycila.old;

import com.mycila.distribution.Distribution;

import static java.lang.Math.*;
import static java.lang.System.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MathsOld {


    public static final BigInteger TEN = BigInteger.valueOf(10);

    private MathsOld() {
    }

    public static int length(int number) {
        return length(number, 10);
    }

    public static int length(BigInteger number) {
        return number.toString().length();
    }

    public static int length(long number) {
        return length(number, 10);
    }

    public static int length(int number, int base) {
        int length = 1;
        while ((number /= base) > 0) length++;
        return length;
    }

    public static int length(long number, int base) {
        int length = 1;
        while ((number /= base) > 0) length++;
        return length;
    }

    public static int digitSignature(int number) {
        int bitset = 0;
        for (; number > 0; number /= 10)
            bitset |= 1 << (number % 10);
        return bitset;
    }

    public static boolean hasDifferentDigits(long number) {
        return hasDifferentDigits(number, 10);
    }

    public static boolean hasDifferentDigits(long number, int base) {
        int bitset = 0;
        for (; number > 0; number /= base) {
            int digit = (int) (number % base);
            int bit = 1 << digit;
            if ((bitset & bit) != 0) return false;
            bitset |= bit;
        }
        return true;
    }

    public static boolean hasDifferentDigitsForLength(long number, int length) {
        return hasDifferentDigitsForLength(number, length, 10);
    }

    public static boolean hasDifferentDigitsForLength(long number, int length, int base) {
        int bitset = 0;
        for (; length > 0; number /= base, length--) {
            int digit = (int) (number % base);
            int bit = 1 << digit;
            if ((bitset & bit) != 0) return false;
            bitset |= bit;
        }
        return true;
    }

    public static Distribution<Integer> digitMap(int number) {
        Distribution<Integer> distribution = Distribution.of(Integer.class);
        do {
            distribution.add(number % 10);
            number /= 10;
        } while (number > 0);
        return distribution;
    }

    public static TIntArrayList digits(long number) {
        return digits(number, 10);
    }

    public static TIntArrayList digits(long number, int base) {
        TIntArrayList list = new TIntArrayList();
        do {
            list.add((int) (number % base));
        }
        while ((number /= base) > 0);
        list.reverse();
        return list;
    }

    public static int sumOfDigits(BigInteger n) {
        final String s = n.toString();
        int count = 0;
        for (int i = 0, max = s.length(); i < max; i++) {
            int d = s.charAt(i) - 48;
            if (d >= 0 && d <= 9) count += d;
        }
        return count;
    }

    public static int sumOfDigits(int number) {
        return sumOfDigits(number, 10);
    }

    public static int sumOfDigits(int number, int base) {
        int sum = 0;
        do {
            sum += number % base;
        }
        while ((number /= base) > 0);
        return sum;
    }

    public static long base(int number, int base) {
        int num = 0;
        for (int p = 1; number > 0; number /= base, p *= 10)
            num = p * (number % base) + num;
        return num;
    }

    public static TIntArrayList divisors(int number) {
        TIntArrayList list = new TIntArrayList();
        for (int d = 1; d <= number; d++) if (number % d == 0) list.add(d);
        return list;
    }

    public static int fact(int n) {
        int res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }

    public static long factLong(long n) {
        long res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }

    public static BigInteger factBig(long n) {
        BigInteger f = BigInteger.ONE;              // long f = 1;
        BigInteger g = BigInteger.valueOf(n);       // int n
        while (g.compareTo(BigInteger.ONE) > 0) {  // while (n > 1) {
            f = f.multiply(g);                      // f *= n;
            g = g.subtract(BigInteger.ONE);         // n--;
        }
        return f;
    }

    /**
     * Computes fact(a) / fact(b) for a > b. Example 12!/6! = (12+6)! / 6! = 12*11*...*7
     */
    public static int factDiv(int a, int b) {
        b++;
        int res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }

    public static long factDivLong(long a, long b) {
        b++;
        long res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }

    public static BigInteger factDivBig(long a, long b) {
        final BigInteger bb = BigInteger.valueOf(b);
        BigInteger ba = BigInteger.valueOf(a);
        BigInteger res = BigInteger.ONE;
        while (ba.compareTo(bb) > 0) {
            res = res.multiply(ba);
            ba = ba.subtract(BigInteger.ONE);
        }
        return res;
    }

    public static boolean arePermutations(int n1, int... numbers) {
        final char[] first = ("" + n1).toCharArray();
        final int length = first.length;
        Arrays.sort(first);
        for (int number : numbers) {
            final char[] digits = ("" + number).toCharArray();
            if (digits.length != length)
                return false;
            Arrays.sort(digits);
            for (int i = 0; i < length; i++)
                if (first[i] != digits[i])
                    return false;
        }
        return true;
    }

    public static int sumTo(int n) {
        return (n * (1 + n)) >> 1;
    }

    public static int sum(int start, int end) {
        return ((end - start + 1) * (start + end)) >> 1;
    }


    /**
     * using integers only, get a lower bound on the (integer) square
     * root for a number
     * <p/>
     * given a number, n, what is the square of the next number?
     * just expand (n + 1)^2:
     * (n + 1)^2 = n^2 + 2*n + 1
     * <p/>
     * 2*n + 1 = the set {3, 5, 7, 9, ... }, which we will call delta
     * note that delta(n+1) = delta(n) + 2
     * <p/>
     * we can remove the need to use n altogether, only calculating it
     * after the last square <= number:
     * square_1 = 1 (1^2)
     * delta_1 = (2*n + 1) = 3
     * <p/>
     * square_2 = square_1 + delta_1
     * = 4
     * delta_2 = (2*n + 1) = 5
     * = delta_1 + 2
     * <p/>
     * when we have the square we need, we  calculate n from delta:
     * n = (delta / 2) - 1
     *
     * @param number number
     * @return the first integrer lower or equals to sqrt(n)
     */
    public static int sqrtFloor(int number) {
        int square = 1;
        int delta = 3;
        while (square <= number) {
            square += delta;
            delta += 2;
        }
        return (delta >> 1) - 1;
    }

    public static long sqrtFloor(long number) {
        long square = 1;
        long delta = 3;
        while (square <= number) {
            square += delta;
            delta += 2;
        }
        return (delta >> 1) - 1;
    }

    /**
     * using integers only, get an upper bound on the (integer) square root for a number
     *
     * @param number number
     * @return the first integer number just after than sqrt(number)
     */
    public static int sqrtCeil(int number) {
        int sqrt = sqrtFloor(number);
        return sqrt * sqrt == number ? sqrt : sqrt + 1;
    }

    public static int trialDivision(int number) {
        if (number <= 3) return number;
        else if ((number & 1) == 0) return 2;
        final int index = primes.binarySearch(number);
        if (index >= 0) return primes._data[index];
        final int[] dif = {6, 4, 2, 4, 2, 4, 6, 2};
        int m = 101;
        for (int i = 7, max = (int) Math.sqrt(number) + 1; m < max; i++, m += dif[i % 8])
            if (number % m == 0)
                return m;
        return number;
    }

    public static int powInt(final int n, int exp) {
        if (exp == 0) return 0;
        int res = n;
        while (--exp > 0) res *= n;
        return res;
    }

    public static long pentagonal(long index) {
        return index * (3 * index - 1) >> 1;
    }

    public static long isPentagonal(long number) {
        long test = ((int) Math.sqrt(24 * number + 1) + 1) / 6;
        return test * test == number ? test : -1;
    }

    public static long triangle(long index) {
        return index * (index + 1) >> 1;
    }

    public static long isTriangle(long number) {
        double test = Math.sqrt((number << 1) + 0.25) - 0.5;
        return test == (long) test ? (long) test : -1;
    }

    public static long hexagonal(long index) {
        return index * ((index << 1) - 1);
    }

    public static long isHexagonal(long number) {
        long test = ((int) Math.sqrt(1 + (number << 3)) + 1) / 4;
        return test * test == number ? test : -1;
    }

    /*
     * http://en.wikipedia.org/wiki/Catalan_numbers 
     */
    public static long catalan(int index) {
        return factDivLong(index << 1, index) / factLong(index + 1);
    }

    public static long binomial(int n, int p) {
        return factDivLong(n, n - p) / factLong(p);
    }

    public static BigInteger binomialBig(long n, long p) {
        return factDivBig(n, n - p).divide(factBig(p));
    }

    private static class Test {
        public static void main(String args[]) {
            out.println("=================================");
            out.println("length(0)=" + length(0));
            out.println("length(1)=" + length(1));
            out.println("length(10)=" + length(10));
            out.println("length(99)=" + length(99));
            out.println("length(100)=" + length(100));
            out.println("length(101)=" + length(101));
            out.println("=================================");

            out.println("=================================");
            out.println("digits(1)=" + digits(1));
            out.println("digits(123)=" + digits(123));
            out.println("digits(1234)=" + digits(1234));
            out.println("=================================");
            out.println("sumOfDigits(0)=" + sumOfDigits(0));
            out.println("sumOfDigits(1)=" + sumOfDigits(1));
            out.println("sumOfDigits(11)=" + sumOfDigits(11));
            out.println("sumOfDigits(121)=" + sumOfDigits(121));
            out.println("sumOfDigits(1221)=" + sumOfDigits(1221));
            out.println("sumOfDigits(1231)=" + sumOfDigits(1231));
            out.println("=================================");
            out.println("divisors(3580)=" + divisors(3580));
            out.println("divisors(1221)=" + divisors(1221));
            out.println("divisors(0)=" + divisors(0));
            out.println("divisors(1)=" + divisors(1));
            out.println("divisors(2)=" + divisors(2));
            out.println("divisors(11)=" + divisors(11));
            out.println("=================================");

            out.println("=================================");
            out.println("sum(1, 5)=" + sum(1, 5));
            out.println("sum(3, 6)=" + sum(3, 6));
            out.println("=================================");
            out.println("pythagoreanTriplet(1000)=" + pythagoreanTriplet(1000));
            out.println("pythagoreanTriplet(840)=" + pythagoreanTriplet(840));
            out.println("=================================");

            out.println("=================================");
            out.println("sqrt(1234)=" + sqrt(1234));
            out.println("sqrt(1)=" + sqrt(1));
            out.println("sqrt(0)=" + sqrt(0));
            out.println("sqrt(625)=" + sqrt(625));
            out.println("sqrt(5)=" + sqrt(5));
            out.println("sqrt(7)=" + sqrt(7));
            out.println("sqrt(9999999)=" + sqrt(9999999));
            out.println("=================================");
            out.println("sqrtFloor(1234)=" + sqrtFloor(1234));
            out.println("sqrtFloor(1)=" + sqrtFloor(1));
            out.println("sqrtFloor(0)=" + sqrtFloor(0));
            out.println("sqrtFloor(625)=" + sqrtFloor(625));
            out.println("sqrtFloor(5)=" + sqrtFloor(5));
            out.println("sqrtFloor(7)=" + sqrtFloor(7));
            out.println("sqrtFloor(9999999)=" + sqrtFloor(9999999));
            out.println("=================================");
            out.println("sqrtCeil(1234)=" + sqrtCeil(1234));
            out.println("sqrtCeil(1)=" + sqrtCeil(1));
            out.println("sqrtCeil(0)=" + sqrtCeil(0));
            out.println("sqrtCeil(625)=" + sqrtCeil(625));
            out.println("sqrtCeil(5)=" + sqrtCeil(5));
            out.println("sqrtCeil(7)=" + sqrtCeil(7));
            out.println("sqrtCeil(9999999)=" + sqrtCeil(9999999));
            out.println("=================================");
            out.println("=================================");
            out.println("hasDifferentDigits(0)=" + hasDifferentDigits(0));
            out.println("hasDifferentDigits(1)=" + hasDifferentDigits(1));
            out.println("hasDifferentDigits(33)=" + hasDifferentDigits(33));
            out.println("hasDifferentDigits(34)=" + hasDifferentDigits(34));
            out.println("hasDifferentDigits(987654321)=" + hasDifferentDigits(987654321));
            out.println("hasDifferentDigits(987654322)=" + hasDifferentDigits(987654322));
            out.println("hasDifferentDigits(255)=" + hasDifferentDigits(255));
            out.println("hasDifferentDigits(0)=" + hasDifferentDigits(0));
            out.println("hasDifferentDigits(1)=" + hasDifferentDigits(1));
            out.println("hasDifferentDigits(11)=" + hasDifferentDigits(11));
            out.println("=================================");
            out.println("hasDifferentDigitsForLength(0, 1)=" + hasDifferentDigitsForLength(0, 1));
            out.println("hasDifferentDigitsForLength(0, 2)=" + hasDifferentDigitsForLength(0, 2));
            out.println("hasDifferentDigitsForLength(34, 1)=" + hasDifferentDigitsForLength(34, 1));
            out.println("hasDifferentDigitsForLength(34, 3)=" + hasDifferentDigitsForLength(34, 3));
            out.println("hasDifferentDigitsForLength(34, 4)=" + hasDifferentDigitsForLength(34, 4));
            out.println("hasDifferentDigitsForLength(434, 2)=" + hasDifferentDigitsForLength(434, 2));
            out.println("hasDifferentDigitsForLength(434, 3)=" + hasDifferentDigitsForLength(434, 3));
            out.println("=================================");
            out.println("digitMap(95112)=" + digitMap(95112));
            out.println("digitMap(56333)=" + digitMap(56333));
            out.println("digitMap(1)=" + digitMap(1));
            out.println("digitMap(0)=" + digitMap(0));
            out.println("digitMap(311400)=" + digitMap(311400));
            out.println("=================================");
            out.println(digitMap(311400).equals(digitMap(311400)));
            out.println(digitMap(0).equals(digitMap(0)));
            out.println(digitMap(125874).equals(digitMap(251748)));
            out.println(digitMap(311400).equals(digitMap(311400)));
            out.println(digitMap(0).equals(digitMap(0)));
            out.println(digitMap(125874).equals(digitMap(9251748)));
            out.println(digitMap(1125874).equals(digitMap(2251748)));
            out.println("=================================");
            out.println("powInt(0, 0)=" + powInt(0, 0));
            out.println("powInt(1, 0)=" + powInt(1, 0));
            out.println("powInt(1, 1)=" + powInt(1, 1));
            out.println("powInt(2, 1)=" + powInt(2, 1));
            out.println("powInt(2, 3)=" + powInt(2, 3));
            out.println("=================================");
            out.println("base(8, 10)=" + base(8, 10));
            out.println("base(34, 10)=" + base(34, 10));
            out.println("base(8, 8)=" + base(8, 8));
            out.println("base(28, 8)=" + base(28, 8));
            out.println("base(4, 2)=" + base(4, 2));
            out.println("=================================");
            out.println("digits(4, 2)=" + digits(4, 2));
            out.println("digits(8, 8)=" + digits(8, 8));
            out.println("digits(255, 2)=" + digits(255, 2));
            out.println("=================================");
            out.println("pentagonal(1)=" + pentagonal(1));
            out.println("pentagonal(2)=" + pentagonal(2));
            out.println("pentagonal(3)=" + pentagonal(3));
            out.println("pentagonal(4)=" + pentagonal(4));
            out.println("=================================");
            out.println("isPentagonal(1)=" + isPentagonal(1));
            out.println("isPentagonal(2)=" + isPentagonal(2));
            out.println("isPentagonal(4)=" + isPentagonal(4));
            out.println("isPentagonal(5)=" + isPentagonal(5));
            out.println("isPentagonal(6)=" + isPentagonal(6));
            out.println("isPentagonal(22)=" + isPentagonal(22));
            out.println("=================================");
            out.println("triangle(1)=" + triangle(1));
            out.println("triangle(2)=" + triangle(2));
            out.println("triangle(3)=" + triangle(3));
            out.println("triangle(4)=" + triangle(4));
            out.println("=================================");
            out.println("isTriangle(1)=" + isTriangle(1));
            out.println("isTriangle(3)=" + isTriangle(3));
            out.println("isTriangle(6)=" + isTriangle(6));
            out.println("isTriangle(10)=" + isTriangle(10));
            out.println("isTriangle(15)=" + isTriangle(15));
            out.println("=================================");
            out.println("hexagonal(1)=" + hexagonal(1));
            out.println("hexagonal(2)=" + hexagonal(2));
            out.println("hexagonal(3)=" + hexagonal(3));
            out.println("hexagonal(4)=" + hexagonal(4));
            out.println("=================================");
            out.println("isHexagonal(1)=" + isHexagonal(1));
            out.println("isHexagonal(6)=" + isHexagonal(6));
            out.println("isHexagonal(15)=" + isHexagonal(15));
            out.println("isHexagonal(28)=" + isHexagonal(28));
            out.println("isHexagonal(45)=" + isHexagonal(45));
            out.println("=================================");

            out.println("=================================");
            out.println("arePermutations(1487, 4817, 8147)=" + arePermutations(1487, 4817, 8147));
            out.println("arePermutations(1487, 4817, 8147)=" + arePermutations(1487, 4817, 81477));
            out.println("arePermutations(1487, 4817, 8147)=" + arePermutations(14878, 48171, 81477));
            out.println("=================================");
            out.println("factDiv(12, 6)=" + factDiv(12, 6));
            out.println("factDiv(5, 1)=" + factDiv(5, 1));
            out.println("factDiv(5, 3)=" + factDiv(5, 3));
            out.println("=================================");
            out.println("catalan(0)=" + catalan(0));
            out.println("catalan(1)=" + catalan(1));
            out.println("catalan(2)=" + catalan(2));
            out.println("catalan(6)=" + catalan(6));
            out.println("=================================");
            out.println("binomial(1, 1)=" + binomial(1, 1));
            out.println("binomial(5, 1)=" + binomial(5, 1));
            out.println("binomial(100, 2)=" + binomial(100, 2));
            out.println("binomialBig(100, 98)=" + binomialBig(100, 98));
            out.println("binomial(5, 3)=" + binomial(5, 3));
            out.println("binomial(23, 10)=" + binomial(23, 10));
            out.println("binomial(52, 5)=" + binomial(52, 5));
            out.println("=================================");
        }
    }

}
