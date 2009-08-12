package euler;

import com.mycila.old.MathsOld;
import gnu.trove.TIntArrayList;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=46
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem046 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final TIntArrayList squares = new TIntArrayList(1000);
        squares.add(new int[]{1, 4, 9, 16, 25, 36, 49, 64});
        final TIntArrayList primes = new TIntArrayList(1000);
        primes.add(new int[]{2, 3, 5, 7});
        for (int i = 9; ; i++) {
            squares.add(i * i);
            if ((i & 1) == 1) {
                if (MathsOld.isPrime(i, primes)) primes.add(i);
                else {
                    int pos = -1;
                    for (int pIndex = 1, max = primes.size(), p; pIndex < max && (p = primes.getQuick(pIndex)) < i; pIndex++) {
                        pos = squares.binarySearch((i - p) >> 1);
                        if (pos >= 0) {
                            //out.println(i + "=" + p + "+2*" + squares.getQuick(pos));
                            break;
                        }
                    }
                    if (pos < 0) {
                        out.println("Not valid for: " + i + " in " + (currentTimeMillis() - time) + "ms");
                        return;
                    }
                }
            }
        }
    }
}