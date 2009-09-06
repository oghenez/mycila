import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestLinearCongruenceApplet extends Applet implements ActionListener {
  private static BigInteger t;
  BigInteger[] ans;
  BigInteger a;
  BigInteger b;
  BigInteger m;
  TextField aBox=new TextField(20);
  Label Label1=new Label("x is congruent to");
  TextField bBox=new TextField(20);
  Label Label2=new Label("modulo");
  TextField mBox=new TextField(20);
  Button solveButton=new Button("Solve congruence");
  Button nextButton=new Button("Next solution");
  TextField msgBox=new TextField(80);
  public void init() {
    setLayout(new GridLayout(8,1));
    add(aBox);
    add(Label1);
    add(bBox);
    add(Label2);
    add(mBox);
    add(solveButton);
    solveButton.addActionListener(this);
    add(nextButton);
    nextButton.addActionListener(this);
    nextButton.setEnabled(false);
    add(msgBox);
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==solveButton) {
      try {
        t=new BigInteger("0");
        a=new BigInteger(aBox.getText());
        b=new BigInteger(bBox.getText());
        m=new BigInteger(mBox.getText());
        ans=BigIntegerMath.solveLinearCongruence(a,b,m);
        msgBox.setText(ans[1].mod(m).toString());
        nextButton.setEnabled(true);
      } catch (NumberFormatException nfe) {
        nextButton.setEnabled(false);
        msgBox.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        nextButton.setEnabled(false);
        msgBox.setText(iae.toString());
      }
    } else if (e.getSource()==nextButton) {
      t=t.add(BigIntegerMath.ONE).mod(m);
      msgBox.setText((ans[1].add(t.multiply(m).divide(ans[0]))).mod(m).toString());
    }
  }
}
