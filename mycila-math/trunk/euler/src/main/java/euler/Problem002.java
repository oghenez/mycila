package euler;

import static java.lang.Math.*;
import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=2
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem002 {
    public static void main(String[] args) {
        // See http://fr.wikipedia.org/wiki/Suite_de_Fibonacci
        // Dans la suite de Fibo, F(n) est pair quand n est un multiple de 3
        // Donc il faut calculer la somme S des F(3i) de i=0 a i=X tant que F(3i) <= 4000000
        double sqrt5 = sqrt(5);
        double or = (1.0 + sqrt5) / 2.0;
        long sum = 0;
        for (long i = 0, fib; (fib = round(pow(or, i) / sqrt5)) <= 4000000; sum += fib, i += 3) ;
        out.println(sum);
    }
}
