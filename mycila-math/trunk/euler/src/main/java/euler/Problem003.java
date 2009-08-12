package euler;

import static java.lang.Math.*;
import static java.lang.System.*;
import java.math.BigInteger;
import static java.math.BigInteger.*;
import java.util.LinkedList;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=3
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem003 {
    public static void main(String[] args) {

        System.out.println("SOLUTION 1: Using Long:");

        out.println("-1: " + isPrime(-1));
        out.println("0: " + isPrime(0));
        out.println("1: " + isPrime(1));
        out.println("2: " + isPrime(2));
        out.println("3: " + isPrime(3));
        out.println("4: " + isPrime(4));
        out.println("5: " + isPrime(5));
        out.println("6: " + isPrime(6));
        out.println("600851475143: " + isPrime(600851475143L));

        out.println("factor(-1): " + factor(-1));
        out.println("factor(0): " + factor(0));
        out.println("factor(1): " + factor(1));
        out.println("factor(2): " + factor(2));
        out.println("factor(3): " + factor(3));
        out.println("factor(4): " + factor(4));
        out.println("factor(5): " + factor(5));
        out.println("factor(6): " + factor(6));
        out.println("factor(600851475143): " + factor(600851475143L));

        System.out.println("SOLUTION 2: Using BigInteger:");

        out.println("-1: " + valueOf(-1).isProbablePrime(100));
        out.println("0: " + valueOf(0).isProbablePrime(100));
        out.println("1: " + valueOf(1).isProbablePrime(100));
        out.println("2: " + valueOf(2).isProbablePrime(100));
        out.println("3: " + valueOf(3).isProbablePrime(100));
        out.println("4: " + valueOf(4).isProbablePrime(100));
        out.println("5: " + valueOf(5).isProbablePrime(100));
        out.println("6: " + valueOf(6).isProbablePrime(100));
        out.println("600851475143: " + valueOf(600851475143L).isProbablePrime(100));

        out.println("factor(-1): " + factor(valueOf(-1)));
        out.println("factor(0): " + factor(valueOf(0)));
        out.println("factor(1): " + factor(valueOf(1)));
        out.println("factor(2): " + factor(valueOf(2)));
        out.println("factor(3): " + factor(valueOf(3)));
        out.println("factor(4): " + factor(valueOf(4)));
        out.println("factor(5): " + factor(valueOf(5)));
        out.println("factor(6): " + factor(valueOf(6)));
        out.println("factor(600851475143): " + factor(valueOf(600851475143L)));
    }

    private static boolean isPrime(long n) {
        if (n == 2L) return true;
        else if (n <= 1L || (n & 1L) == 0L) return false;
        for (long p = 3L, sqrt = (long) sqrt(n); p <= sqrt; p += 2L) if (n % p == 0L) return false;
        return true;
    }

    public static List<Long> factor(long n) {
        List<Long> factors = new LinkedList<Long>();
        if (n < 0L) {
            n = abs(n);
        }
        if (n <= 1L) {
            return factors;
        }
        while ((n & 1L) == 0L) {
            n >>= 1L;
            factors.add(2L);
        }
        for (long p = 3L; p <= n; p += 2)
            if (isPrime(p)) while (n % p == 0L) {
                n /= p;
                factors.add(p);
            }
        return factors;
    }

    public static List<BigInteger> factor(BigInteger n) {
        List<BigInteger> factors = new LinkedList<BigInteger>();
        if (n.signum() == -1) {
            n = n.abs();
        }
        if (n.equals(ZERO) || n.equals(ONE)) {
            return factors;
        }
        for (BigInteger p = valueOf(2); p.compareTo(n) <= 0; p = p.nextProbablePrime())
            while (n.remainder(p).equals(ZERO)) {
                n = n.divide(p);
                factors.add(p);
            }
        return factors;
    }

}