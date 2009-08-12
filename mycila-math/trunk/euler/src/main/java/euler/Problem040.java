package euler;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=40
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem040 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; sb.length() <= 1000000; i++) sb.append(i);
        int prd = 1;
        for (int i = 1; i <= 1000000; i *= 10) {
            out.println(sb.charAt(i));
            prd *= (sb.charAt(i) - 48);
        }
        out.println(prd + " in " + (currentTimeMillis() - time) + "ms");
    }
}
