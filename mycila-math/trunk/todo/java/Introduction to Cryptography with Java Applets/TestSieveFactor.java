import javax.swing.*;
public class TestSieveFactor {
   public static void main(String[] args) {
      boolean idiot;
      do {
         idiot=false;
         try {
            int n=new Integer(JOptionPane.showInputDialog("Enter an integer > 1:")).intValue();
            if (n<=1) {
               idiot=true;
               JOptionPane.showMessageDialog(null,"Invalid integer entered!");
            } else {
              int d=sieveFactor(n);
              if (d==n) JOptionPane.showMessageDialog(null,n+" is prime.");
              else JOptionPane.showMessageDialog(null,d+" divides "+n+".");
            }
         } catch (NumberFormatException e) {
            idiot=true;
            JOptionPane.showMessageDialog(null,e.toString());
         }
      } while (idiot);
      System.exit(0);
   }
   private static int sieveFactor(int n) {
      int divisor; boolean prime=true;
      for (divisor=2;divisor<=Math.sqrt(n);divisor++)
         if (n%divisor==0) {prime=false; break;}
      return prime?n:divisor;
   }
}
