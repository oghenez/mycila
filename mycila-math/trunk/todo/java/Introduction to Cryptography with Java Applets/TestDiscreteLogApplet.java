import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestDiscreteLogApplet extends Applet implements ActionListener {

   TextArea baseArea=new TextArea();
   Label l1=new Label("to the x power is congruent to");
   TextArea resArea=new TextArea();
   Label l2=new Label("modulo");
   TextArea modArea=new TextArea();
   Button bsgsButton=new Button("Solve for x using baby step giant step");
   Button esButton=new Button("Solve for x using exhaustive search");
   TextArea logArea=new TextArea();

   BigInteger base;
   BigInteger residue;
   BigInteger modulus;

   public void init() {
      setLayout(new GridLayout(8,1));
      add(baseArea);
      add(l1);
      add(resArea);
      add(l2);
      add(modArea);
      add(bsgsButton);
      bsgsButton.addActionListener(this);
      add(esButton);
      esButton.addActionListener(this);
      add(logArea);
   }

   public void actionPerformed(ActionEvent e) {
      try {
         base=new BigInteger(baseArea.getText());
         residue=new BigInteger(resArea.getText());
         modulus=new BigInteger(modArea.getText());
         if (e.getSource()==bsgsButton) {
            logArea.setText(BigIntegerMath.logBabyStepGiantStep(base,residue,modulus).toString());
         } else if (e.getSource()==esButton) {
            logArea.setText(BigIntegerMath.logExhaustiveSearch(base,residue,modulus).toString());
         }
      } catch (Exception exc) {
         logArea.setText(exc.toString());
      }
   }

}