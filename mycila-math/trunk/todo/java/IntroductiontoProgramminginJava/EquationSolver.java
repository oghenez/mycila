
/*************************************************************************
 *  Compilation:  javac MultivariateNewton.java
 *  Execution:    java MultivariateNewton
 *
 *  Run Newton's method to finds the roots and local min/max of
 *  a twice-differentiable function of one variable.
 *
 *************************************************************************/

import Jama.Matrix; 

public class EquationSolver {
    public static final double EPSILON = 1e-14;

    // Newton's method to find x* such that f(x*) = 0, starting at x
    public static Matrix root(Equations f, Matrix x) {
        while (true) {
            Matrix J = f.jacobian(x);
            Matrix delta = J.inverse().times(f.eval(x));
            x = x.minus(delta);
            if (delta.norm1() < EPSILON) break;
        }
        return x;
    }

}
