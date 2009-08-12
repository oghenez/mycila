package euler;

import com.mycila.Factor;
import com.mycila.old.MathsOld;
import gnu.trove.TIntArrayList;

import static java.lang.System.*;
import java.util.ArrayList;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=47
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem047 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        // will stock our results, just for display
        final List<List<Factor>> results = new ArrayList<List<Factor>>(4);
        final int[] numbers = new int[4];

        // create a prime sieve to reuse it to find factors to avoid repeated calls of tial division
        TIntArrayList sieve = MathsOld.sieve(100000);
        int max = sieve.last();

        // check all numbers starting at 2*3*5*7 which is the first composite of 4 different primes
        for (int n = 2 * 3 * 5 * 7, consecutive = 0; ; n++) {
            // if n is above the sieve limit, we extend the sieve up to 2n primes
            if (n > max) {
                sieve = MathsOld.sieveExtend(100000, sieve);
                max = sieve.last();
            }
            // then we extract the different prime factors of n
            List<Factor> factors = MathsOld.decompSieve(n, sieve);
            // if the count is 4, we increment our counter. Otherwise, we reset it.
            if (factors.size() == 4) {
                results.add(consecutive, factors);
                numbers[consecutive++] = n;
            } else consecutive = 0;
            if (consecutive == 4) {
                for (int i = 0; i < numbers.length; i++)
                    System.out.println(numbers[i] + ": " + results.get(i));
                out.println(" in " + (currentTimeMillis() - time) + "ms");
                break;
            }
        }
    }
}

/*
134043: [3^1, 7^1, 13^1, 491^1]
134044: [2^2, 23^1, 31^1, 47^1]
134045: [5^1, 17^1, 19^1, 83^1]
134046: [2^1, 3^2, 11^1, 677^1]
 in 3890ms
*/