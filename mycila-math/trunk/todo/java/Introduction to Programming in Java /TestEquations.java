/*************************************************************************
 *  Compilation:  javac TestEquations.java
 *
 *  f0(x0, x1) = x0^3 - 3 x0 x1^2 -1 = 0
 *  f1(x0, x1) = 3x0^2 x1 - x1^3   = 0
 *
 *************************************************************************/

import Jama.Matrix;

public class TestEquations implements Equations {

    public Matrix eval(Matrix x) {
        int m = x.getRowDimension();
        int n = x.getColumnDimension();
        double x0 = x.get(0, 0);
        double x1 = x.get(1, 0);
        if (n == 1 && m == 2) {
            Matrix f = new Matrix(m, n);
            f.set(0, 0, x0*x0*x0 - 3*x0*x1*x1 - 1);
            f.set(1, 0, 3*x0*x0*x1 - x1*x1*x1);
            return f;
        }
        throw new RuntimeException("Illegal argument");
    }


    // Jacobian
    public Matrix jacobian(Matrix x) {
        int m = x.getRowDimension();
        int n = x.getColumnDimension();
        double x0 = x.get(0, 0);
        double x1 = x.get(1, 0);
        if (n == 1 && m == 2) {
            Matrix J = new Matrix(m, m);
            J.set(0, 0, 3*x0*x0 - 3*x1*x1);
            J.set(1, 0, 6*x0);
            J.set(0, 1, -6*x0*x1);
            J.set(1, 1, 3*x0*x0 - 3*x1*x1);
            return J;
        }
        throw new RuntimeException("Illegal argument");
    }



    // sample client
    public static void main(String[] args) { 
        Equations f = new TestEquations();
        Matrix x = new Matrix(2, 1);
        Matrix root;
        x.set(0, 0, -0.6);
        x.set(1, 0,  0.6);
        root = EquationSolver.root(f, x);
        root.print(9, 6);

        x.set(0, 0,  0.9);
        x.set(1, 0, -0.1);
        root = EquationSolver.root(f, x);
        root.print(9, 6);
    }


}
