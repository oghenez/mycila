/*************************************************************************
 *  Compilation:  javac MatrixMultiplication.java
 *  Execution:    java MatrixMultiplication
 * 
 *  6 different ways to multiply two N-by-N matrices.
 *  Illustrates importance of row-major vs. column-major ordering.
 *
 *  % java MatrixMultiplication 500
 *  Generating input:  0.048 seconds
 *  Order ijk:   3.562 seconds
 *  Order ikj:   1.348 seconds
 *  Order jik:   2.368 seconds
 *  Order jki:   4.846 seconds
 *  Order kij:   1.407 seconds
 *  Order kji:   4.91 seconds
 *  Order jik JAMA optimized:   0.571 seconds
 *  Order ikj pure row:   0.483 seconds
 *
 *  These timings are on a SUN-FIRE-X4100 running Linux.
 *
 *************************************************************************/

public class MatrixMultiplication {
    public static void show(double[][] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%6.4f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        long start, stop;
        double elapsed;


        // generate input
        start = System.currentTimeMillis(); 

        double[][] A = new double[N][N];
        double[][] B = new double[N][N];
        double[][] C;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                A[i][j] = Math.random();

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                B[i][j] = Math.random();

        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Generating input:  " + elapsed + " seconds");

        // order 1: ijk = dot product version
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                for (int k = 0; k < N; k++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order ijk:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 2: ikj
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int i = 0; i < N; i++)
            for (int k = 0; k < N; k++)
                for (int j = 0; j < N; j++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order ikj:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 3: jik
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int j = 0; j < N; j++)
            for (int i = 0; i < N; i++)
                for (int k = 0; k < N; k++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order jik:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 4: jki = GAXPY version
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int j = 0; j < N; j++)
            for (int k = 0; k < N; k++)
                for (int i = 0; i < N; i++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order jki:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 5: kij
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int k = 0; k < N; k++)
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order kij:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 6: kji = outer product version
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int k = 0; k < N; k++)
            for (int j = 0; j < N; j++)
                for (int i = 0; i < N; i++)
                    C[i][j] += A[i][k] * B[k][j];
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order kji:   " + elapsed + " seconds");
        if (N < 10) show(C);


        // order 7: jik optimized ala JAMA 
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        double[] bcolj = new double[N];
        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) bcolj[k] = B[k][j];
            for (int i = 0; i < N; i++) {
                double[] arowi = A[i];
                double sum = 0.0;
                for (int k = 0; k < N; k++) {
                    sum += arowi[k] * bcolj[k];
                }
                C[i][j] = sum;
            }
        }
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order jik JAMA optimized:   " + elapsed + " seconds");
        if (N < 10) show(C);

        // order 8: ikj pure row
        C = new double[N][N];
        start = System.currentTimeMillis(); 
        for (int i = 0; i < N; i++) {
            double[] arowi = A[i];
            double[] crowi = C[i];
            for (int k = 0; k < N; k++) {
                double[] browk = B[k];
                double aik = arowi[k];
                for (int j = 0; j < N; j++) {
                    crowi[j] += aik * browk[j];
                }
            }
        }
        stop = System.currentTimeMillis();
        elapsed = (stop - start) / 1000.0;
        System.out.println("Order ikj pure row:   " + elapsed + " seconds");
        if (N < 10) show(C);

    }

}
