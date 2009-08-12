package euler;

import com.mycila.old.MathsOld;
import gnu.trove.TIntArrayList;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=49
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem049 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        // create a sieve up to maximum prime number having 4 digits
        final TIntArrayList sieve = MathsOld.sieve(9999);
        for (int i = 0, max = sieve.size(); i < max - 2; i++) {
            final int prime = sieve._data[i];
            if (prime > 1000 && prime < 9999
                    && sieve.binarySearch(prime + 3330) >= 0
                    && sieve.binarySearch(prime + 6660) >= 0
                    && MathsOld.arePermutations(prime, prime + 3330, prime + 6660))
                System.out.println(prime + " " + (prime + 3330) + " " + (prime + 6660));
        }
        out.println(" in " + (currentTimeMillis() - time) + "ms");
    }

}
