package euler;

import com.mycila.Factoradic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=24
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem024 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();

        // http://en.wikipedia.org/wiki/Factoradic
        // We use the Factoradic numbering system to find the solution faster
        // Our base is: 0 1 2 3 4 5 6 7 8 9
        // We have to find F(999999).
        // F(0) = 0x9! + 0x8! + 0x7! + 0x6! + 0x5! + 0x4! + 0x3! + 0x2! + 0x1! + 0x0! (=> 0 1 2 3 4 5 6 7 8 9)
        // F(1) = 0x9! + 0x8! + 0x7! + 0x6! + 0x5! + 0x4! + 0x3! + 0x2! + 1x1! + 0x0! (=> 0 1 2 3 4 5 6 7 9 8)
        // F(1440) = 0x9! + 0x8! + 0x7! + 2x6! + 0x5! + 0x4! + 0x3! + 0x2! + 0x1! + 0x0! (=> 0 1 2 3 6 4 5 7 9 8)

        final int BASE = 10;

        // compute the base (9!, 8!, 7!, ...)
        int[] base = new int[BASE];
        for (int i = BASE - 1; i >= 0; i--) base[BASE - 1 - i] = fact(i);
        System.out.println(Arrays.toString(base));

        // find the digits of the Factoradic number for 999999
        int[] digits = new int[BASE];
        for (int i = BASE - 1, n = 999999; i >= 0; i--) {
            int remain = n % base[BASE - 1 - i];
            digits[BASE - 1 - i] = (n - remain) / base[BASE - 1 - i];
            n = remain;
        }
        System.out.println("F(999999): " + Arrays.toString(digits));

        // now, that we have the index of the permuted numbers, we display these numbers:
        List<Integer> numbers = new ArrayList<Integer>(BASE);
        for (int i = 0; i < BASE; i++) numbers.add(i);
        StringBuilder sb = new StringBuilder();
        for (int digit : digits) sb.append(numbers.remove(digit));

        System.out.println("1000000th permutation is " + sb + " in " + (System.currentTimeMillis() - time) + "ms");

        System.out.println(Factoradic.base(10).permutation(1000000 - 1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    private static int fact(int n) {
        int res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }
}
