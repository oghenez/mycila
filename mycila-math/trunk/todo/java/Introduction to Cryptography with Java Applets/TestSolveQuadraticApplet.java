import java.applet.*;
import java.math.*;
import java.awt.*;
import java.awt.event.*;
public class TestSolveQuadraticApplet extends Applet implements ActionListener {

  BigInteger a;
  BigInteger b;
  BigInteger c;
  BigInteger p;
  BigInteger q;
  BigInteger n;
  BigInteger[] answer;

  TextField af=new TextField(30);
  Label label1=new Label("x^2 + ");
  TextField bf=new TextField(30);
  Label label2=new Label("x + ");
  TextField cf=new TextField(30);
  Label label3=new Label(" is congruent to zero modulo ");
  TextField pf=new TextField(30);
  Label label4=new Label(" times ");
  TextField qf=new TextField(30);
  Label label5=new Label("Solution: x is congruent to ");
  TextField ansArea=new TextField(30);
  Label label6=new Label(" modulo ");
  TextField nf=new TextField(80);
  Button solveButton=new Button("Solve");

  public void init() {
    setLayout(new GridLayout(7,2));
    add(af);
    add(label1);
    add(bf);
    add(label2);
    add(cf);
    add(label3);
    add(pf);
    add(label4);
    add(qf);
    add(label5);
    add(ansArea);
    ansArea.setEditable(false);
    add(label6);
    add(nf);
    nf.setEditable(false);
    add(solveButton);
    solveButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    String outString="";
    if (e.getSource()==solveButton) {
      try {
        a=new BigInteger(af.getText());
        b=new BigInteger(bf.getText());
        c=new BigInteger(cf.getText());
        p=new BigInteger(pf.getText());
        q=new BigInteger(qf.getText());
        n=p.multiply(q);
        ansArea.setText("");
        answer=BigIntegerMath.solveQuadratic(a,b,c,p,q,16);
        for (int i=0;i<answer.length-1;i++) {
           outString+=answer[i]+" or ";
        }
        outString+=answer[answer.length-1].toString();
        ansArea.setText(outString);
        nf.setText(n.toString());
      } catch (NumberFormatException nfe) {
        ansArea.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        ansArea.setText(iae.toString());
      } catch (ArithmeticException ae) {
        ansArea.setText(ae.toString());
      }
    }
  }

}
