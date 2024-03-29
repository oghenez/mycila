import javax.swing.*;
import java.math.BigInteger;
public class TestGCD {
   public static void main(String[] args) {
      BigInteger i=new BigInteger(JOptionPane.showInputDialog("Enter an integer: "));
      BigInteger j=new BigInteger(JOptionPane.showInputDialog("Enter another integer: "));
      JOptionPane.showMessageDialog(null,"The gcd of "+i+" and "+j+" is "+gcd(i,j));
      System.exit(0);
   }
   static BigInteger ZERO=new BigInteger("0");

   //Compute the gcd recursively using the Euclidian algorithm
   private static BigInteger gcd(BigInteger first, BigInteger second) {
   //Make sure both are nonnegative
      first=first.abs();
      second=second.abs();
   //Call the recursive method
      return recurseGCD(first,second);
   }

   private static BigInteger recurseGCD(BigInteger x, BigInteger y) {
      if (y.equals(ZERO)) return x;
      else return recurseGCD(y,x.mod(y));
   }

}
