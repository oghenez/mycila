package euler;

import static java.lang.Math.*;
import static java.lang.System.*;
import java.math.BigInteger;
import static java.math.BigInteger.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=10
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem010 {
    public static void main(String[] args) {
        // SOLUTION 1:
        long time = System.currentTimeMillis();
        BigInteger sum = ZERO;
        for (BigInteger p = valueOf(2), max = valueOf(2000000); p.compareTo(max) < 0; sum = sum.add(p), p = p.nextProbablePrime()) ;
        out.println(sum); // 142913828922
        System.out.println(System.currentTimeMillis() - time);

        // SOLUTION 2:
        time = System.currentTimeMillis();
        long sump = 2L;
        for (long i = 3L; i < 2000000L; i += 2L) if (isPrime(i)) sump += i;
        out.println(sump); // 142913828922
        System.out.println(System.currentTimeMillis() - time);
    }

    private static boolean isPrime(long n) {
        if (n == 2L) return true;
        else if (n <= 1L || (n & 1L) == 0L) return false;
        for (long p = 3L, sqrt = (long) sqrt(n); p <= sqrt; p += 2L) if (n % p == 0L) return false;
        return true;
    }

}
