import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestDiophantineApplet extends Applet implements ActionListener {

   Label aLabel=new Label(" a: ");
   TextField entryField1=new TextField(40);
   Label bLabel=new Label(" b: ");
   TextField entryField2=new TextField(40);
   Label cLabel=new Label(" c: ");
   TextField entryField3=new TextField(40);
   Label xLabel=new Label(" x: ");
   TextField outField1=new TextField(40);
   Label yLabel=new Label(" y: ");
   TextField outField2=new TextField(40);
   Button computeButton=new Button("ax + by = c: Compute x & y");
   TextField msg=new TextField(40);

   public void init() {
      setLayout(new GridLayout(12,1));
      add(aLabel);
      add(entryField1);
      add(bLabel);
      add(entryField2);
      add(cLabel);
      add(entryField3);
      add(xLabel);
      add(outField1);
      outField1.setEditable(false);
      add(yLabel);
      add(outField2);
      outField2.setEditable(false);
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
            BigInteger c=new BigInteger(entryField3.getText());
            answers=BigIntegerMath.solveLinearDiophantine(a,b,c);
            outField1.setText(answers[1].toString());
            outField2.setText(answers[2].toString());
            msg.setText("");
         } catch (NumberFormatException nfe) {
            msg.setText(nfe.toString());
         } catch (IllegalArgumentException iae) {
            msg.setText(iae.toString());
         }
      }
   }

}
