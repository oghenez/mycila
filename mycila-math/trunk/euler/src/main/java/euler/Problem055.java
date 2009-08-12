package euler;

import com.mycila.old.MathsOld;

import static java.lang.System.*;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=55
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem055 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        final int maxNumber = 10000;
        final int maxIterations = 50;
        final Set<BigInteger> lychrel = new TreeSet<BigInteger>();
        final Set<BigInteger> nonLychrel = new TreeSet<BigInteger>();
        final Set<BigInteger> stack = new TreeSet<BigInteger>();
        for (int n = 0; n < maxNumber; n++) {
            BigInteger test = BigInteger.valueOf(n);
            // do not do anything if this number as already be marked
            if (lychrel.contains(test))
                continue;
            // if it is not marked, we must find what it is in a maximum of 'maxIterations' iterations
            stack.add(test);
            BigInteger reverse = MathsOld.reverse(test);
            // iterate while we not found a palindrom in some maximum iterations
            for (int it = 1; it < maxIterations; it++) {
                test = test.add(reverse);
                reverse = MathsOld.reverse(test);
                stack.add(test);
                if (test.equals(reverse) || nonLychrel.contains(test)) {
                    nonLychrel.addAll(stack);
                    stack.clear();
                    break;
                } else if (lychrel.contains(test)) {
                    lychrel.addAll(stack);
                    stack.clear();
                    break;
                }
            }
            if (!stack.isEmpty()) {
                lychrel.addAll(stack);
                stack.clear();
            }
        }

        System.out.println(lychrel);
        out.println(lychrel.size() + " in " + (currentTimeMillis() - time) + "ms");

        int count = 0;
        BigInteger max = BigInteger.valueOf(maxNumber);
        for (BigInteger l : lychrel) if (l.compareTo(max) <= 0) count++;

        out.println(count + " under " + maxNumber);
    }
}

/*

We can iterate over all 10000 numbers, from 0 to 9999, and we will mark those which gives a palindromic number.
So avoid recomputing, we will mark all numbers.
In example, if we find that a number A gives a palindromic number B, we will mark A plus all numbers which give A.
If after 49 iterations we do not have a palindromic number, all numbers of each iterations will be marked as being Lychrel numbers.
Then, we just have to count (and display) all marked Lychrel numbers !

*/
