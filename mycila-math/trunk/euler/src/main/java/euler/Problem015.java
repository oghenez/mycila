package euler;

import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=15
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem015 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        // This is the problem of the Catalan number.
        // http://en.wikipedia.org/wiki/Catalan_number
        // C(n) = (2n)! / (n+1)!n!
        // We want to know all applications for a square of length L,
        // L+1 is the total number of exceedance decreasing steps
        // so the result will be C(L)*(L+1)
        // We call P(x,y) the product of the numbers between x to y.
        // Thus P(1,n) = n!
        // The numbers of toutes R(n) = [(n+1)*(2*n)!]/[(n+1)!*n!]
        // R(n) = [(n+1)*P(1,2n)]/[P(1,n+1)*P(1,n)]
        // R(n) = [(n+1)*P(1,n)*P(n+1,2n)]/[P(1,n+1)*P(1,n)]
        // R(n) = [(n+1)*P(n+1,2n)]/[P(1,n+1)]
        // R(n) = P(n+1,2n)/P(1,n)
        BigInteger r = p(21, 40).divide(p(1, 20));
        System.out.println(r + " in " + (System.currentTimeMillis() - time) + "ms");
    }

    private static BigInteger p(long a, long b) {
        BigInteger res = BigInteger.valueOf(b);
        while (b-- > a) res = res.multiply(BigInteger.valueOf(b));
        return res;
    }
}

// 137846528820
