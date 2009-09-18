/*************************************************************************
 *  Compilation:  javac -classpath .:jama.jar KarhunenLoeve.java
 *  Execution:    java  -classpath .:jama.jar KarhunenLoeve M N
 *  Dependencies: jama.jar
 *  
 *  Compute best rank r approximation to matrix using SVD.
 *  
 *       http://math.nist.gov/javanumerics/jama/
 *       http://math.nist.gov/javanumerics/jama/Jama-1.0.1.jar
 *
 *************************************************************************/

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.awt.Color;

public class KarhunenLoeve {

    // return the integer between 0 and 255 closest to c
    public static int truncate(double c) {
        if (c <= 0) return 0;
        if (c >= 255) return 255;
        return (int) (c + 0.5);
    }

    public static Matrix KL(Matrix A, int r) {
        int M = A.getRowDimension();
        int N = A.getColumnDimension();
        SingularValueDecomposition svd = A.svd();
        Matrix Ur = svd.getU().getMatrix(0, M-1, 0, r-1);  // first r columns of U
        Matrix Vr = svd.getV().getMatrix(0, N-1, 0, r-1);  // first r columns of V
        Matrix Sr = svd.getS().getMatrix(0, r-1, 0, r-1);  // first r rows and columns of S
        return Ur.times(Sr).times(Vr.transpose());
    }


    public static void main(String[] args) { 

        // rank of approximation
        int rank = Integer.parseInt(args[1]);

        // read in the original picture and display it
        Picture pic1 = new Picture(args[0]);
        int M = pic1.height();
        int N = pic1.width();
        pic1.show();
        System.err.println("Done reading " + M + "-by-" + N + " image");

        // create matrix of grayscale intensities
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                Color color = pic1.get(i, j);
                double lum = Luminance.lum(color);
                A.set(i, j, lum);
            }
        }

        // compute best approximation of given rank
        Matrix Ar = KL(A, rank);
        System.err.println("Done computing best rank " + rank + " approximation");

        // create new picture
        Picture pic2 = new Picture(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                int y = (int) (Math.round(truncate(Ar.get(i, j))));
                Color gray = new Color(y, y, y);
                pic2.set(i, j, gray);
            }
        }
        pic2.show();
        System.err.println("Done");
    }



}
