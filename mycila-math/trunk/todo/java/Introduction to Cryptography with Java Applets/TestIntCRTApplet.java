import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestIntCRTApplet extends Applet implements ActionListener {

  TextField limitField=new TextField();
  Label l1=new Label("Enter a bit length which the computations will never meet above:");
  TextField display=new TextField();
  Button add=new Button("+");
  Button multiply=new Button("*");
  Button equ=new Button("=");

  Panel p1=new Panel();
  Panel p2=new Panel();

  int maxBits=0;
  IntCRT val1;
  IntCRT val2;
  Object prevButton;
  Object thisButton=equ;
  char arithmeticOp='=';

  public void init() {
    setLayout(new GridLayout(2,1));
    p1.setLayout(new GridLayout(3,1));
    p2.setLayout(new GridLayout(1,3));
    p2.setFont(new Font("Monospaced",Font.BOLD,36));
    p1.add(limitField);
    p1.add(l1);
    p1.add(display);
    p2.add(add);
    add.addActionListener(this);
    p2.add(multiply);
    multiply.addActionListener(this);
    p2.add(equ);
    equ.addActionListener(this);
    add(p1);
    add(p2);
  }

  public void actionPerformed(ActionEvent e) {
   try {
      maxBits=Integer.parseInt(limitField.getText());
   } catch (Exception exc) {
         display.setText(exc.toString());
         return;
   }
   char op=e.getActionCommand().charAt(0);
   switch (op) {
      case '+': case '*':
      try {
         val1=new IntCRT(display.getText(),maxBits);
      } catch (Exception nfe) {
         display.setText(nfe.toString());
         return;
      }
      arithmeticOp=op;
      display.setText("");
      return;
      case '=':
      try {
         val2=new IntCRT(display.getText(),maxBits);
      } catch (Exception nfe) {
         display.setText(nfe.toString());
         return;
      }
      display.setText(calc(val1,val2).toString());
   }
  }

  private IntCRT calc(IntCRT op1, IntCRT op2) {
		IntCRT result=null;
		switch (arithmeticOp) {
			case '+': result = op1.add(op2); break;
			case '*': result = op1.multiply(op2); break;
		}
		return result;

  }

}
