package euler;

import com.mycila.Decomposition;
import com.mycila.Sieve;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=12
 * <p/>
 * http://home.att.net/~numericana/answer/numbers.htm#divisors
 * The number of divisors of a number N is (a+1)(b+1)(c+1)
 * where N = A^a + B^b + C^c
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem012 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        // We know that the first number to have over 500 divisors will be less or equals than
        // the number composed of the first 501 primes.
        // So all the divisors of the triangle number will be composed of some of these 501 primes
        Sieve primes501 = Sieve.to(501);
        for (int i = 1, triangle = 1; ; i++, triangle = i * (1 + i) / 2) {
            if (Decomposition.of(triangle, primes501).divisorCount() > 500) {
                out.println(triangle);
                break;
            }
        }
        System.out.println(System.currentTimeMillis() - time + "ms");
    }
}

// output:
// 76576500
// 124ms

