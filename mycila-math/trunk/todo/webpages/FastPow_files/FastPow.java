import java.awt.*;

/*
 * Paul Garrett
 *
 * copyright Paul Garrett, GNU Public License, 1998
 *
 * fast exponentiation with 18-digit limitation
 */

public class FastPow extends java.applet.Applet {
	 
	 int height, width, background_color, foreground_color;
	 
	 TextField baseTF; // to enter the base
	 TextField expTF; // to enter the exponent
	 TextField modulusTF; // to enter the modulus
	 TextField outTF; // to display the outcome
	 Button computeB; // also to compute...

	 final long longBoundSqrt = 3037000499L;

/****************************/

  public void init() {

    setFont(new Font("TimesRoman", Font.PLAIN, 12));
    try {
      background_color = Integer.parseInt(getParameter("background_color"), 16);
      foreground_color = Integer.parseInt(getParameter("foreground_color"), 16);
      height = Integer.parseInt(getParameter("height"), 16);
      width = Integer.parseInt(getParameter("width"), 16);
    }
    catch (Exception e) {
      background_color = 0x503050;
      foreground_color = 0xe0e0e0;
      height = 320;
      width = 550;
    }
    setBackground(new Color(background_color));
    setForeground(new Color(foreground_color));

	 setLayout(new GridLayout(4,2));

    add(new Label("Enter a base < 10^18"));
    baseTF = new TextField("", 20);
    add(baseTF);

    add(new Label("Enter positive exponent < 10^18"));
    expTF = new TextField("", 20);
    add(expTF);

    add(new Label("Enter a modulus < 3,037,000,499"));
    modulusTF = new TextField("", 12);
    add(modulusTF);

    computeB = new Button(" compute:  base^exponent % modulus = ");
    add(computeB);

	 outTF = new TextField("", 20);
	 add(outTF);
  }

  /************** End of init() **********************/

  public final synchronized boolean action(Event evt, Object arg) {
		if (evt.target instanceof TextField || evt.target == computeB) {
			 outTF.setText("");
			 long b,e,m;
			 String base = Clean.clean(baseTF.getText());
			 String exp = Clean.clean(expTF.getText());
			 String modulus = Clean.clean(modulusTF.getText());
			 if (base.length() <= 18 && exp.length() <= 18 && modulus.length() <= 10) {
				  boolean queryOK;
				  b = Long.parseLong(base);
				  e = Long.parseLong(exp);
				  m = Long.parseLong(modulus);
				  if (m > longBoundSqrt || m < 1 || e < 0 ) {
						queryOK = false;
				  }
				  else {
						queryOK = true;
				  }
				  if (queryOK == true) {
						compute_and_display(b,e,m);
				  }
				  else {
						outTF.setText("modulus too large or < 0, or exponent < 0");
				  }
				  return true;
			 }
			 else {
				  outTF.setText("number(s) too large, or do not parse");
				  return true;
			 }
		}
		else {
			 return false; // old AWT event model...
		}
  }
	 
	 /************** End of event handling *****************/
	 
	 final synchronized void compute_and_display(long b, long e, long m) {
		  outTF.setText("" + fastpow(b,e,m));
	 }
	 
	 final synchronized long fastpow(long x, long e, long m) {
		  long out = 1;
		  x = x % m;
		  while (e > 0) {
				if (e % 2 == 1) {
					 out = (out * x) % m;
					 e -= 1;
				}
				x = (x * x) % m;
				e /= 2;
		  }
		  return out;
	 }

}
	 
/******************************************
 *
 * The End
 *
 ********************************************/
