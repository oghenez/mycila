/*************************************************************************
 *  Compilation:  javac Farey.java Rational.java
 *  Execution:    java Farey.java N
 *
 *  Sample execution:
 *
 *   % java Farey 2
 *   0/1  1/2  1/1  
 *
 *   % java Farey 3
 *   0/1  1/3  1/2  2/3  1/1  
 *
 *   % java Farey 4
 *   0/1  1/4  1/3  1/2  2/3  3/4  1/1  
 *
 *   % java Farey 5
 *   0/1  1/5  1/4  1/3  2/5  1/2  3/5  2/3  3/4  4/5  1/1  
 *
 *
 *************************************************************************/


class Farey {

   public static void main(String[] args) {
      int N = Integer.parseInt(args[0]);

      Rational one = new Rational(1, 1);
      Rational r0  = new Rational(0, 1);
      Rational r1  = new Rational(1, N);

      // repeat until r0 equals 1/1
      while (r0.compareTo(one) < 0) {
         System.out.print(r0 + "  ");
         int num = ((r0.denominator() + N) / r1.denominator()) * r1.numerator()
                 - r0.numerator();
         int den = ((r0.denominator() + N) / r1.denominator()) * r1.denominator()
                 - r0.denominator();
         Rational rnew = new Rational(num, den);
         r0 = r1;
         r1 = rnew;
      }
      System.out.println(r0);
   }


}
