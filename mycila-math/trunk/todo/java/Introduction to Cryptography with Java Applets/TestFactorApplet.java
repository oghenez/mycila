import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestFactorApplet extends Applet implements ActionListener {

   Label promptLabel=new Label("Enter a positive integer to factor here:");
   TextArea bigNumArea=new TextArea("",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
   Panel buttonPanel=new Panel();
   Button monteButton=new Button("Find Monte Carlo Factor");
   Button pollardButton=new Button("Find Pollard p-1 Factor");
   TextArea factorArea=new TextArea("",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);

   Thread th;

   public void init() {
      setLayout(new GridLayout(4,1));
      add(promptLabel);
      add(bigNumArea);
      buttonPanel.setLayout(new GridLayout(1,2));
      monteButton.addActionListener(this);
      buttonPanel.add(monteButton);
      pollardButton.addActionListener(this);
      buttonPanel.add(pollardButton);
      add(buttonPanel);
      add(factorArea);
      factorArea.setEditable(false);
   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      if (source==monteButton) {
         th=new Thread(
            new Runnable() {
               public void run() {
                  try {
                     BigInteger inputNum=new BigInteger(bigNumArea.getText());
                     BigInteger factor=BigIntegerMath.monteCarloFactor(inputNum,1000000);
                     if (factor!=null) factorArea.setText(factor+" is a Monte Carlo factor of "+inputNum);
                     else factorArea.setText("Could not find Monte Carlo factor - out of memory!");
                  } catch (NumberFormatException nfe) {
                     factorArea.setText(nfe.toString());
                  } catch (IllegalArgumentException iae) {
                     factorArea.setText(iae.toString());
                  }
               }
            }
         );
         th.start();
      } else if (source==pollardButton) {
         th=new Thread(
            new Runnable() {
               public void run() {
                  try {
                     BigInteger inputNum=new BigInteger(bigNumArea.getText());
                     BigInteger factor=BigIntegerMath.pMinusOneFactor(inputNum);
                     factorArea.setText(factor+" is a Pollard p-1 factor of "+inputNum);
                  } catch (NumberFormatException nfe) {
                     factorArea.setText(nfe.toString());
                  } catch (IllegalArgumentException iae) {
                     factorArea.setText(iae.toString());
                  }
               }
            }
         );
         th.start();
      }
   }

}