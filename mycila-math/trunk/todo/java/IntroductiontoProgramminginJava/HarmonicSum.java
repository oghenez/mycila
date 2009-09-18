
/*************************************************************************
 *  Compilation:  javac HarmonicSum.java
 *  Execution:    java HarmonicSum N
 *
 *  Compute the first N terms of the harmonic sum:  1/1 + 1/2 + ... + 1/N.
 *  Compares single precision vs. double precision. Compares summing from
 *  left-to-right vs. right-to-left.
 *  
 *
 *  % java HarmonicSum 10000
 *  9.787613
 *  9.787604
 *  9.787606036044348
 *  9.787606036044386
 *
 *  % java HarmonicSum 100000
 *  12.090851
 *  12.090153
 *  12.090146129863335
 *  12.090146129863408    // true answer = 12.090146129863428

 *
 *  % java HarmonicSum 1000000
 *  14.357358
 *  14.392652
 *  14.392726722864989
 *  14.392726722865772    // true answer = 14.392726722865724
 *
 *  % java HarmonicSum 10000000
 *  15.403683
 *  16.686031
 *  16.695311365857272
 *  16.695311365859965    // true answer = 16.695311365859852
 *
 *  % java HarmonicSum 100000000
 *  15.403683
 *  18.807919
 *  18.997896413852555
 *  18.997896413853447    // true answer = 18.997896413853898
 *
 *  % java HarmonicSum 1000000000
 *  15.403683
 *  18.807919
 *  21.30048150234855
 *  21.30048150234615     // true answer  21.30048150234794401668510
 *
 * java HarmonicSum 2000000000
 * 15.403683
 * 18.807919
 * 21.993628682662845
 * 21.99362868265598
 *
 *************************************************************************/

public class HarmonicSum {

    public static void main(String[] args) { 
        int N = Integer.parseInt(args[0]);

        // using single precision, left-to-right
        float sum1 = 0.0f;
        for (int i = 1; i <= N; i++)
            sum1 = sum1 + 1.0f / i;
        System.out.println(sum1);

        // using single precision, right-to-left
        float sum2 = 0.0f;
        for (int i = N; i >= 1; i--)
            sum2 = sum2 + 1.0f / i;
        System.out.println(sum2);

        // using double precision, left-to-right
        double sum3 = 0.0;
        for (int i = 1; i <= N; i++)
            sum3 = sum3 + 1.0 / i;
        System.out.println(sum3);

        // using double precision, right-to-left
        double sum4 = 0.0;
        for (int i = N; i >= 1; i--)
            sum4 = sum4 + 1.0 / i;
        System.out.println(sum4);
   }

}
