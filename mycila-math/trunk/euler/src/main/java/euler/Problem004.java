package euler;

import com.mycila.Palindroms;

import static java.lang.System.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=4
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem004 {
    public static void main(String[] args) {

        final Palindroms palindrom = Palindroms.base(10);

        Set<Integer> palindroms = new TreeSet<Integer>();
        for (int i = 999; i >= 100; i--) for (int j = i, prod = i * j; j >= 100; j--, prod = i * j) if (prod % 11 == 0 && ("" + prod).equals(new StringBuilder("" + prod).reverse().toString())) palindroms.add(prod);
        out.println(palindroms);

        palindroms.clear();
        for (int i = 999; i >= 100; i--) for (int j = i, prod = i * j; j >= 100; j--, prod = i * j) if (prod % 11 == 0 && palindrom.isPalindromic(prod)) palindroms.add(prod);
        out.println(palindroms);
    }
}