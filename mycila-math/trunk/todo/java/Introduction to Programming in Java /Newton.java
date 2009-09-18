
/*************************************************************************
 *  Compilation:  javac Newton.java
 *  Execution:    java Newton
 *
 *  Run Newton's method to finds the roots and local min/max of
 *  a twice-differentiable function of one variable.
 *
 *************************************************************************/


public class Newton {
    public static final double EPSILON = 1e-14;

    // Newton's method to find x* such that f(x*) = 0, starting at x
    public static double root(Function f, double x) {
        while (Math.abs(f.eval(x) / f.deriv(x)) > EPSILON) {
            x = x - f.eval(x) / f.deriv(x);
        }
        return x;
    }

    // Newton's method to find x* such that f'(x*) = 0, starting at x
    public static double optimum(Function f, double x) {
        while (Math.abs(f.deriv(x) / f.deriv2(x)) > EPSILON) {
            x = x - f.deriv(x) / f.deriv2(x);
        }
        return x;
    }

}
