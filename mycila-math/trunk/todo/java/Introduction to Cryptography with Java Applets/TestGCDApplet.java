import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestGCDApplet extends Applet implements ActionListener {

   Label gcdLabel=new Label(" The greatest common divisor of a and b: ");
   TextField msg=new TextField(40);
   Label num1Label=new Label(" a: ");
   TextField entryField1=new TextField(40);
   Label num2Label=new Label(" b: ");
   TextField entryField2=new TextField(40);
   Button genGCDButton=new Button("Get the greatest common divisor");

   public void init() {
      setLayout(new GridLayout(7,1));
      add(gcdLabel);
      add(msg);
      msg.setEditable(false);
      add(num1Label);
      add(entryField1);
      add(num2Label);
      add(entryField2);
      add(genGCDButton);
      genGCDButton.addActionListener(this);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==genGCDButton) {
         BigInteger g=null;
         try {
            BigInteger a=new BigInteger(entryField1.getText());
            BigInteger b=new BigInteger(entryField2.getText());
            g=a.gcd(b);
            msg.setText(g.toString());
         } catch (NumberFormatException nfe) {
            msg.setText("Not an integer");
         }
      }
   }

}
