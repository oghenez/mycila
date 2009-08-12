package euler;

import com.mycila.Digits;
import com.mycila.Factorial;
import com.mycila.sequence.IntProcedure;

/**
 * http://projecteuler.net/index.php?section=problems&id=34
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem034 {
    public static void main(String[] args) throws Exception {
        final long time = System.currentTimeMillis();
        int s = 0;
        final Digits digits = Digits.base(10);
        for (int i = 100; i < 2540160; i++) {
            final int[] sum = new int[]{0};
            final int n = i;
            digits.each(i, new IntProcedure() {
                public boolean execute(int digit) {
                    sum[0] += Factorial.of(digit);
                    return sum[0] <= n;
                }
            });
            if (sum[0] == n) {
                System.out.println(n);
                s += n;
            }
        }
        System.out.println(s + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}

/*

Numbers of 2 digits can only be composed with [0..4] since their factorial is below 99
Combinations of 0, 1, 2 and 3 are not valid since to small
The only possibilities are numbers like ?4 and 4? where ? is in [0..4]. As the number contains 4, it will be between 14 (1!+4!=25) and 44 (4!+4!=48)

=> give a try, there is no possibility (14, 24, 34, 44, 43, 42, 41)

So our numbers begins with 3 digits

9! = 362880

So the maximum number we can product with 9! is such that length(N)*9!=N

For 7 digits:  9999999 gives 7*9!=2540160 (7 digits)
For 8 digits: 99999999 gives 8*9!=2903040 (7 digits also !)

Oups ! It means that we cannot produce numbers greater than 7 digits.

The factorion (http://mathworld.wolfram.com/Factorion.html) of an n-digit number cannot exceed n·9!. So the maximum to check will be 2540160.

*/
