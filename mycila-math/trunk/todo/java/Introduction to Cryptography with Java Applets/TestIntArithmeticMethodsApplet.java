import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestIntArithmeticMethodsApplet extends Applet implements ActionListener {

  TextField display=new TextField();
  Button add=new Button("+");
  Button subtract=new Button("-");
  Button multiply=new Button("*");
  Button equ=new Button("=");
  Panel p1=new Panel();
  Panel p2=new Panel();
  Int val1=new Int();
  Int val2=new Int();
  Object prevButton;
  Object thisButton=equ;
  char arithmeticOp='=';

  public void init() {
    setLayout(new GridLayout(2,1));
    p1.setLayout(new GridLayout(1,1));
    p2.setLayout(new GridLayout(1,4));
    p2.setFont(new Font("Monospaced",Font.BOLD,36));
    p1.add(display);
    p2.add(add);
    add.addActionListener(this);
    p2.add(subtract);
    subtract.addActionListener(this);
    p2.add(multiply);
    multiply.addActionListener(this);
    p2.add(equ);
    equ.addActionListener(this);
    add(p1);
    add(p2);
  }

  public void actionPerformed(ActionEvent e) {
   char op=e.getActionCommand().charAt(0);
   switch (op) {
      case '+': case '-': case '*':
      try {
         val1=new Int(display.getText());
      } catch (IntException nfe) {
         display.setText(nfe.toString());
         return;
      }
      arithmeticOp=op;
      display.setText("");
      return;
      case '=':
      try {
         val2=new Int(display.getText());
      } catch (IntException nfe) {
         display.setText(nfe.toString());
         return;
      }
      display.setText(calc(val1,val2).toString());
   }
  }

	private Int calc(Int op1, Int op2) {
		Int result = new Int();
		switch (arithmeticOp) {
			case '+': result = op1.add(op2); break;
			case '-': result = op1.subtract(op2); break;
			case '*': result = op1.multiply(op2); break;
		}
		return result;
	}

}
