package euler;

import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=16
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem016 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        BigInteger n = BigInteger.valueOf(2).pow(1000);
        char[] digits = n.toString().toCharArray();
        long sum = 0;
        for (int i = 0; i < digits.length; i++) sum += digits[i] - 48;
        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}