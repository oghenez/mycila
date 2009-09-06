import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestEuclidApplet extends Applet implements ActionListener {

   Label aLabel=new Label(" a: ");
   TextField entryField1=new TextField(40);
   Label bLabel=new Label(" b: ");
   TextField entryField2=new TextField(40);
   Label xLabel=new Label(" x: ");
   TextField outField1=new TextField(40);
   Label yLabel=new Label(" y: ");
   TextField outField2=new TextField(40);
   Label gLabel=new Label(" (a,b): ");
   TextField outField3=new TextField(40);
   Button computeButton=new Button("ax + by = (a,b): Compute x, y, & (a,b)");
   TextField msg=new TextField(40);

   public void init() {
      setLayout(new GridLayout(12,1));
      add(aLabel);
      add(entryField1);
      add(bLabel);
      add(entryField2);
      add(xLabel);
      add(outField1);
      outField1.setEditable(false);
      add(yLabel);
      add(outField2);
      outField2.setEditable(false);
      add(gLabel);
      add(outField3);
      outField3.setEditable(false);
      add(computeButton);
      computeButton.addActionListener(this);
      add(msg);
      msg.setEditable(false);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==computeButton) {
         BigInteger[] answers=null;
         try {
            BigInteger a=new BigInteger(entryField1.getText());
            BigInteger b=new BigInteger(entryField2.getText());
            answers=BigIntegerMath.euclid(a,b);
            outField1.setText(answers[1].toString());
            outField2.setText(answers[2].toString());
            outField3.setText(answers[0].toString());
            msg.setText("");
         } catch (NumberFormatException nfe) {
            msg.setText(nfe.toString());
         } catch (IllegalArgumentException iae) {
            msg.setText(iae.toString());
         }
      }
   }

}
