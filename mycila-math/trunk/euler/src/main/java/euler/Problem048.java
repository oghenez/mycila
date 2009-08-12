package euler;

import com.mycila.old.MathsOld;

import static java.lang.System.*;
import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=48
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem048 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        BigInteger sum = BigInteger.ZERO;
        for (int n = 1; n <= 1000; n++) sum = sum.add(BigInteger.valueOf(n).pow(n));
        out.println(sum + " in " + (currentTimeMillis() - time) + "ms");

        time = currentTimeMillis();
        final long mod = 10000000000L;
        long s = 0;
        for (int n = 1; n <= 1000; n++) s = MathsOld.addMod(MathsOld.powMod(n, n, mod), s, mod);
        out.println(s + " in " + (currentTimeMillis() - time) + "ms");
    }

}

// 9110846700 in 88ms
