package euler;

import com.mycila.Palindroms;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=36
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem036 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final Palindroms palindroms10 = Palindroms.base(10);
        final Palindroms palindroms2 = Palindroms.base(2);
        int sum = 0;
        for (int i = 1; i < 1000000; i += 2) {
            if (palindroms10.isPalindromic(i) && palindroms2.isPalindromic(i)) sum += i;
        }
        out.println(sum + " in " + (currentTimeMillis() - time) + "ms");
    }
}

/*

Numbers must be palindromic in base 2 and 10. It means the numbers cannot end with 0. In base 2, it means we can skip all odd numbers.

*/