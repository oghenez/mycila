/*************************************************************************
 *  Compilation:  javac Bernoulli.java
 *  Execution:    java Bernoulli N
 *
 *  Print out the first N Bernoulli numbers.
 * 
 *  % java Bernoulli 20
 *  1
 *  -1/2
 *  1/6
 *  0
 *  -1/30
 *  0
 *  1/42
 *  0
 *  -1/30
 *  0
 *  5/66
 *  0
 *  -691/2730
 *  0
 *  7/6
 *  0
 *  -3617/510
 *  0
 *  43867/798
 *  0
 *
 *************************************************************************/

import java.math.BigInteger;

public class Bernoulli {

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        // precompute binomial coefficients
        BigInteger[][] binomial = new BigInteger[N+1][N+1];
        for (int n = 1; n <= N; n++) binomial[0][n] = BigInteger.ZERO;
        for (int n = 0; n <= N; n++) binomial[n][0] = BigInteger.ONE;

        // bottom-up dynamic programming
        for (int n = 1; n <= N; n++)
            for (int k = 1; k <= N; k++)
                binomial[n][k] = binomial[n-1][k-1].add(binomial[n-1][k]);


        // now compute Bernoulli numbers
        BigRational[] bernoulli = new BigRational[N+1];
        bernoulli[0] = new BigRational(1, 1);
        bernoulli[1] = new BigRational(-1, 2);
        for (int k = 2; k < N; k++) {
            bernoulli[k] = new BigRational(0, 1);
            for (int i = 0; i < k; i++) {
                BigRational coef = new BigRational(binomial[k + 1][k + 1 - i], BigInteger.ONE);
                bernoulli[k] = bernoulli[k].minus(coef.times(bernoulli[i]));
            }
            bernoulli[k] = bernoulli[k].divides(new BigRational(k+1, 1));
        }

        // print out the first N Bernoulli numbers
        for (int i = 0; i < N; i++)
            System.out.println(bernoulli[i]);

    }
}
