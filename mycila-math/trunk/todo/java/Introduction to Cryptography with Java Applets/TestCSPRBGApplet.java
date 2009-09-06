import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.math.*;
public class TestCSPRBGApplet extends Applet implements ActionListener {

   Label l1=new Label("Enter seed below");
   TextField seedField=new TextField();
   Button computeButton=new Button("Generate random byte");
   TextField byteField=new TextField();

   boolean firstClick=true;
   byte[] seed;
   CSPRBG c=null;

   public void init() {
      setLayout(new GridLayout(4,1));
      add(l1);
      add(seedField);
      add(computeButton);
      add(byteField);
      computeButton.addActionListener(this);
   }

   public void actionPerformed(ActionEvent e) {
      try {
         if (firstClick) {
            seed=new BigInteger(seedField.getText()).toByteArray();
            c=new CSPRBG(seed);
            firstClick=false;
            seedField.setEditable(false);
         }
         byteField.setText(" "+c.getRandomByte());
      } catch (Exception exc) {byteField.setText(exc.toString()+"-hit reload.");}
   }
}