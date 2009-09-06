import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestPowApplet extends Applet implements ActionListener {
  TextArea baseArea=new TextArea();
  Label l1=new Label("raised to the power");
  TextArea expArea=new TextArea();
  Label l2=new Label("is congruent to");
  TextArea resArea=new TextArea();
  Label l3=new Label("modulo");
  TextArea modArea=new TextArea();
  Button doit=new Button("Compute");
  public void init() {
    setLayout(new GridLayout(4,2));
    add(baseArea);
    add(l1);
    add(expArea);
    add(l2);
    add(resArea);
    resArea.setEditable(false);
    add(l3);
    add(modArea);
    add(doit);
    doit.addActionListener(this);
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==doit) {
      try {
        BigInteger base=new BigInteger(baseArea.getText());
        BigInteger exp=new BigInteger(expArea.getText());
        BigInteger mod=new BigInteger(modArea.getText());
        BigInteger res=base.modPow(exp,mod);
        resArea.setText(res.toString());
      } catch (NumberFormatException nfe) {
        resArea.setText(nfe.toString());
      }
    }
  }
}
