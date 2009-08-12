package euler;

import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=20
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem020 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        BigInteger res = BigInteger.valueOf(1);
        for (int i = 2; i <= 100; i++) res = res.multiply(BigInteger.valueOf(i));
        char[] digits = res.toString().toCharArray();
        long sum = 0;
        for (char digit : digits) sum += digit - 48;
        System.out.println(res + " : " + sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}