/*************************************************************************
 *  Compilation:  javac BohrRadius.java
 *  Execution:    java BohrRadius
 *
 *  Comptues the function f(r) = (1 - 3r/4 + r^2 / 8 - r^3 / 192)^2  e^(-r/2)
 *  and its first and second derivatives.

 *  The sample client runs Newton's method to finds the roots and local
 *  minima and maxima. The Bohr radius function has three zeros at 1.872,
 *  6.611, and 15.518. These are also global minima. It has two local
 *  maxima at 3.420 and 22.676.
 *
 *  It illustrates the use of a "callback" using Java interfaces.
 *
 *  % java BohrRadius
 *  Possible roots: 
 *  f(1.8716444550481592) = 1.1817103296889357E-29
 *  f(6.610814578664537) = 6.529313277927562E-31
 *  f(6.6108145786645425) = 4.630205537810115E-31
 *  f(15.517540966287259) = 5.38926090372337E-33
 *  f(1.8716444550481608) = 9.872670880655912E-30
 *
 *  Possible optimum: 
 *  f(1.8716444550481757) = 3.191866873333672E-33
 *  f(3.419589235198309) = 0.017526400937842416
 *  f(6.610814578664558) = 1.6278066343863564E-32
 *  f(15.51754096628727) = 0.0
 *  f(22.676316353129153) = 0.0018496340643479514
 *  
 *************************************************************************/

public class BohrRadius implements Function {

    // f(r)
    public double eval(double x) {
        double temp = 1 - 3*x/4 + x*x/8 - x*x*x/192;
        double exp  = Math.exp(-x/2);
        return temp * temp * exp;
    }

    // f'(r) = first derivative 
    public double deriv(double x) {
        double temp  = 1 - 3*x/4 + x*x/8 - x*x*x/192;
        double temp1 = - 3.0/4 + x/4 - x*x/64;
        double exp  = Math.exp(-x/2);
        return 2*temp*temp1*exp - temp*temp*exp/2;
    }

    // f''(r) = second derivative
    public double deriv2(double x) {
        double temp  = 1 - 3*x/4 + x*x/8 - x*x*x/192;
        double temp1 = - 3.0/4 + x/4 - x*x/64;
        double temp2 = 1.0/4 - x/32;
        double exp  = Math.exp(-x/2);
        return 2*temp1*temp1*exp - 2*temp*exp*temp1 + 2*temp*temp2*exp + temp*temp*exp/4;
    }




    // sample client
    public static void main(String[] args) { 
        Function f = new BohrRadius();
        double root, optimum;

        System.out.println("Possible roots: ");
        root = Newton.root(f, 0.0);
        System.out.println("f(" + root + ") = " + f.eval(root));
        root = Newton.root(f, 4.0);
        System.out.println("f(" + root + ") = " + f.eval(root));
        root = Newton.root(f, 5.0);
        System.out.println("f(" + root + ") = " + f.eval(root));
        root = Newton.root(f, 13.0);
        System.out.println("f(" + root + ") = " + f.eval(root));
        root = Newton.root(f, 22.0);
        System.out.println("f(" + root + ") = " + f.eval(root));
        System.out.println();


        System.out.println("Possible optimum: ");
        optimum = Newton.optimum(f, 0.0);
        System.out.println("f(" + optimum + ") = " + f.eval(optimum));
        optimum = Newton.optimum(f, 4.0);
        System.out.println("f(" + optimum + ") = " + f.eval(optimum));
        optimum = Newton.optimum(f, 5.0);
        System.out.println("f(" + optimum + ") = " + f.eval(optimum));
        optimum = Newton.optimum(f, 13.0);
        System.out.println("f(" + optimum + ") = " + f.eval(optimum));
        optimum = Newton.optimum(f, 22.0);
        System.out.println("f(" + optimum + ") = " + f.eval(optimum));
        System.out.println();
    }


}
