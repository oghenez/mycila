import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;
import java.applet.*;
public class TestCRTApplet extends Applet implements ActionListener {

  BigInteger[] residue;
  BigInteger[] modulus;
  BigInteger[] answer;
  TextArea info1 = new TextArea("Enter residues on left, one per line; enter moduli on right, one per line:",10,10,TextArea.SCROLLBARS_NONE);
  TextArea info2 = new TextArea("Solution is all integers congruent to",10,10,TextArea.SCROLLBARS_NONE);
  TextArea info3=new TextArea("modulo",10,10,TextArea.SCROLLBARS_NONE);
  TextArea residuesArea=new TextArea("",10,30,TextArea.SCROLLBARS_BOTH);
  TextArea moduliArea=new TextArea("",10,30,TextArea.SCROLLBARS_BOTH);
  Button solveButton=new Button("Solve");
  TextField resField=new TextField();
  TextField modField=new TextField();
  Panel[] p=new Panel[3];

  public void init() {
    setLayout(new GridLayout(1,3));
    for (int i=0;i<p.length;i++) {
      p[i]=new Panel();
      add(p[i]);
    }
    p[0].setLayout(new GridLayout(1,1));
    p[1].setLayout(new GridLayout(6,1));
    p[2].setLayout(new GridLayout(1,1));
    p[0].add(residuesArea);
    p[2].add(moduliArea);
    p[1].add(info1);
    info1.setEditable(false);
    p[1].add(solveButton);
    solveButton.addActionListener(this);
    p[1].add(info2);
    info2.setEditable(false);
    p[1].add(resField);
    resField.setEditable(false);
    p[1].add(info3);
    info3.setEditable(false);
    p[1].add(modField);
    modField.setEditable(false);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==solveButton) {
      StringTokenizer rst=new StringTokenizer(residuesArea.getText());
      StringTokenizer mst=new StringTokenizer(moduliArea.getText());
      try {
        residue=new BigInteger[rst.countTokens()];
        modulus=new BigInteger[mst.countTokens()];
        for (int i=0;i<residue.length;i++) residue[i]=new BigInteger(rst.nextToken());
        for (int i=0;i<modulus.length;i++) modulus[i]=new BigInteger(mst.nextToken());
        answer=BigIntegerMath.solveCRT(residue,modulus);
        resField.setText(answer[0].toString());
        modField.setText(answer[1].toString());
      } catch (ArithmeticException ae) {
        resField.setText(ae.toString());
      } catch (NumberFormatException nfe) {
        resField.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        resField.setText(iae.toString());
      }
    }
  }

}
