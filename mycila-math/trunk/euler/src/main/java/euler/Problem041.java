package euler;

import com.mycila.Pandigital;
import com.mycila.PrimaltyTest;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=41
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem041 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        Pandigital pandigital = Pandigital.base(10);
        for (int i = 7654321; i >= 1234567; i -= 2) {
            if (pandigital.isPandigital(i, 1, 7) && PrimaltyTest.millerRabin(i)) {
                out.println(i + " in " + (currentTimeMillis() - time) + "ms");
                break;
            }
        }
    }
}
/*

Problem 41 requires little thinking since testing all primes up to 987654321 is too expensive in Java. So we must try to reduce the quantity of prime numbers to generate for testing against our pandigital test.

A number is divisible by 3 if its digit sum is a multil[le of 3. We try to find the maximum pandigital number not divisible:

9+8+7+6+5+4+3+2+1=45
8+7+6+5+4+3+2+1=36
6+5+4+3+2+1=21
5+4+3+2+1=15
3+2+1=6
2+1=3

Divisible by 3 => There can't be any 1-9, 1-8, 1-6, 1-5 pandigital prime.

7+6+5+4+3+2+1=28
4+3+2+1=10

Not divisible by 3. So we will check primes from 7654321 to 1234567 and then if not found from 4321 to 1234.  

*/