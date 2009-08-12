package euler;

import com.mycila.old.MathsOld;
import gnu.trove.TIntArrayList;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=44
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem044 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        TIntArrayList pentagonals = new TIntArrayList();
        pentagonals.add(1);
        for (int k = 2; ; k++) {
            final int pk = pentagonals.getQuick(k - 2) + 3 * k - 2;
            pentagonals.add(pk);
            for (int j = k - 2; j >= 0; j--) {
                final int d = pk - pentagonals.getQuick(j);
                if (pentagonals.binarySearch(d) > 0 && MathsOld.isPentagonal(pk + pentagonals.getQuick(j)) != -1) {
                    out.println(d + " in " + (currentTimeMillis() - time) + "ms");
                    return;
                }
            }
        }
    }
}

/*

P(n)=n(3n-1)/2

The pentagonal sequence can be created with the following recursive definition:

P(1) = 1
P(n) = P(n-1) + 3n - 2

*/