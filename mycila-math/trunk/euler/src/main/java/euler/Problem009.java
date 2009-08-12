package euler;

import static java.lang.Math.*;
import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=9
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem009 {
    public static void main(String[] args) {
        for (int a = 2; a < 1000; a++) for (int b = a + 1; b < 1000; b++) if (1000.0 - a - b == sqrt(a * a + b * b)) out.println(a + "," + b + "," + (1000 - a - b) + " : " + a * b * (1000 - a - b));
    }
}

// 200,375,425 : 31875000
