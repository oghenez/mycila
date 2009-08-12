package euler;

import com.mycila.old.MathsOld;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=45
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem045 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        for (int i = 286; ; i++) {
            final long ti = MathsOld.triangle(i);
            final long n = MathsOld.isHexagonal(ti);
            final long m = MathsOld.isPentagonal(ti);
            if (n != -1 && m != -1) {
                out.println("T(" + i + ")=P(" + m + ")=H(" + n + ")=" + ti);
                out.println("T(" + i + ")=" + MathsOld.triangle(i));
                out.println("P(" + m + ")=" + MathsOld.pentagonal(m));
                out.println("H(" + n + ")=" + MathsOld.hexagonal(n));
                out.println(" in " + (currentTimeMillis() - time) + "ms");
                break;
            }
        }
    }
}
