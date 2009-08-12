package euler;

import com.mycila.old.MathsOld;
import gnu.trove.TIntArrayList;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=49
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem050 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        final TIntArrayList sieve = MathsOld.sieve(999999);
        int maxSum = 0, maxStart = 0, maxEnd = 0, maxTerms = 0;
        for (int start = 0, max = sieve.size(); start < max - 21; start++) {
            int sum = sieve._data[start], end = start + 1;
            for (; end < max; end++) {
                int s = sum + sieve._data[end];
                if (s <= sum) break; // overflow !
                sum = s;
                final int terms = end - start + 1;
                if (maxTerms < terms && sieve.binarySearch(sum) >= 0) {
                    maxStart = sieve._data[start];
                    maxEnd = sieve._data[end];
                    maxSum = sum;
                    maxTerms = terms;
                    out.println("S(" + maxStart + "," + maxEnd + ")=" + maxSum + " (" + maxTerms + " terms) in " + (currentTimeMillis() - time) + "ms");
                }
            }
        }
        out.println("=> MAX: S(" + maxStart + "," + maxEnd + ")=" + maxSum + " (" + maxTerms + " terms) in " + (currentTimeMillis() - time) + "ms");
    }

}
