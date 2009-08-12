package euler;

import com.mycila.old.MathsOld;

import static java.lang.System.*;
import java.math.BigInteger;
import java.text.MessageFormat;

/**
 * http://projecteuler.net/index.php?section=problems&id=53
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem053 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        int count = 0;
        final BigInteger MAX = BigInteger.valueOf(1000000);
        for (int n = 23; n <= 100; n++) {
            for (int p = 2, max = n >> 1; p <= max; p++) {
                final BigInteger c = MathsOld.binomialBig(n, p);
                if (c.compareTo(MAX) > 0) {
                    System.out.println(MessageFormat.format("C({0},{1})=C({0},{2})={3}", n, p, n - p, c));
                    count += p << 1 == n ? 1 : 2;
                }
            }
        }
        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

}

/*

We need to compute all C(n,p) with 23 <= n <= 100 and 1 <= p =< n
We know that C(n,1)=n and C(n,n)=1 So we can reduce to 1 < p < n.
We can reduce the search knowing that C(n, p) = C(n, n-p). Thus: 1 < p <= n/2.
If C(n,p) > 1000000, we double count for p and n-p, except if n=2p, where we count only one.

*/