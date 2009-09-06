/*************************************************************************
 *  Compilation:  javac Inverse.java
 *  Execution:    java Euclid a n
 *  
 *  Reads two command line parameters p and q and computes the a^-1 mod n
 *  if it exists.
 * 
 *  % java Inverse 345454 2323351
 *  103835

 *************************************************************************/

class Inverse {

   static int[] gcd(int p, int q) {
      if (q == 0)
         return new int[] { p, 1, 0 };
      int[] vals = gcd(q, p % q);
      int d = vals[0];
      int a = vals[2];
      int b = vals[1] - (p / q) * vals[2];
      return new int[] { d, a, b };
   }

   static int inverse(int k, int n) {
      int[] vals = gcd(k, n);
      int d = vals[0];
      int a = vals[1];
      int b = vals[2];
      if (d > 1) { System.out.println("Inverse does not exist."); return 0; }
      if (a > 0) return a;
      return n + a;
   }

   public static void main(String[] args) {
      int k = Integer.parseInt(args[0]);
      int n = Integer.parseInt(args[1]);
      System.out.println("k^-1 = " + inverse(k, n));
   }
}


