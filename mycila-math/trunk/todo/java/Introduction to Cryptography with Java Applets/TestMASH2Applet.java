import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.security.*;
public class TestMASH2Applet extends Applet implements ActionListener {

   Label l1=new Label("Mash-2 modulus:");
   TextArea modArea=new TextArea("Wait-generating modulus");
   Label l2=new Label("Enter message here:");
   TextArea msgArea=new TextArea();
   Button getDigest=new Button("Compute digest of message-display as base 10 integer");
   TextArea digestArea=new TextArea();

   MASH2 masher;

   public void init() {

      setLayout(new GridLayout(6,1));
      add(l1);
      add(modArea);
      modArea.setEditable(false);
      add(l2);
      add(msgArea);
      add(getDigest);
      getDigest.addActionListener(this);
      add(digestArea);
      digestArea.setEditable(false);

      SecureRandom sr=new SecureRandom();
      PrimeGenerator pg=new PrimeGenerator(513,10,sr);
      BigInteger p=pg.getStrongPrime();
      BigInteger q=pg.getStrongPrime();
      BigInteger m=p.multiply(q);
      modArea.setText(m.toString());

      masher=new MASH2(m);

   }

   public void actionPerformed(ActionEvent e) {
      byte[] msg=msgArea.getText().getBytes();
      byte[] digest=masher.digestOf(msg);
      BigInteger digestInt=new BigInteger(1,digest);
      digestArea.setText(digestInt.toString());
   }

}