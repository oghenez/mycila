import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestSQRTApplet extends Applet implements ActionListener {
   Label l1=new Label("The integer square root of");
   TextField numField=new TextField();
   Label l2=new Label("is");
   TextArea sqrtArea=new TextArea();
   Button doit=new Button("Compute");
   public void init() {
      setLayout(new GridLayout(5,1));
      add(l1);
      add(numField);
      add(l2);
      add(sqrtArea);
      sqrtArea.setEditable(false);
      add(doit);
      doit.addActionListener(this);
   }
   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==doit) {
         try {
            BigInteger n=new BigInteger(numField.getText());
            sqrtArea.setText(BigIntegerMath.sqrt(n).toString());
         } catch (Exception exc) {sqrtArea.setText(exc.toString());}
      }
   }
}