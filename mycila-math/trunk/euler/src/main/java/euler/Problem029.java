package euler;

import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=29
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem029 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        Set<BigInteger> numbers = new TreeSet<BigInteger>();
        for (int i = 2; i <= 100; i++)
            for (int j = 2; j <= 100; j++)
                numbers.add(BigInteger.valueOf(i).pow(j));
        System.out.println(numbers.size() + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}