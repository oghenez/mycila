import javax.swing.*;
public class TestIntComparisonMethods {
   public static void main(String[] args) throws Exception {
      Int integer1=new Int(JOptionPane.showInputDialog("Enter an arbitrarily large integer: "));
      Int integer2=new Int(JOptionPane.showInputDialog("Enter another arbitrarily large integer: "));
      if (integer1.equals(integer2)) JOptionPane.showMessageDialog(null,integer1.toString()+" equals "+integer2.toString());
      else JOptionPane.showMessageDialog(null,integer1.toString()+" is not equal to "+integer2.toString());
      if (integer1.lessThan(integer2)) JOptionPane.showMessageDialog(null,integer1.toString()+" is less than "+integer2.toString());
      else JOptionPane.showMessageDialog(null,integer1.toString()+" is not less than "+integer2.toString());
      System.exit(0);
   }
}
