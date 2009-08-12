package euler;

import static java.lang.System.*;
import java.math.BigInteger;
import static java.math.BigInteger.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=7
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem007 {
    public static void main(String[] args) {
        BigInteger p = valueOf(2);
        for (int i = 1; i < 10001; p = p.nextProbablePrime(), i++) ;
        out.println(p);
    }
}