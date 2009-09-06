import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
public class TestRSACipherApplet extends Applet implements ActionListener {

   BigInteger dexponent=null;
   BigInteger exponent=null;
   int modByteLength=0;
   BigInteger p=null,q=null,modulus=null;
   byte[] msgArray=null;
   byte[] encmsgArray=null;

   Label byteLengthLabel=new Label("Desired minimum BYTE length of RSA modulus:");
   TextField byteLengthField=new TextField("0");
   Button getKeysButton=new Button("Get keys and modulus");
   TextArea modArea=new TextArea("Modulus",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
   TextArea eArea=new TextArea("Public enciphering key",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
   TextArea dArea=new TextArea("Secret deciphering key",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
   TextArea plaintextArea=new TextArea("Plaintext",10,40,TextArea.SCROLLBARS_BOTH);
   TextArea ciphertextArea=new TextArea("Ciphertext",10,40,TextArea.SCROLLBARS_BOTH);
   Button encButton=new Button("Encipher");
   Button decButton=new Button("Decipher");
   Panel topPanel=new Panel();
   Panel centerPanel=new Panel();
   Panel bottomPanel=new Panel();

   SecureRandom sr=null;

   public void init() {
      setLayout(new GridLayout(3,1));

      topPanel.setLayout(new GridLayout(4,1));
      topPanel.add(byteLengthLabel);
      topPanel.add(byteLengthField);
      topPanel.add(getKeysButton);
      getKeysButton.addActionListener(this);
      topPanel.add(modArea);
      modArea.setEditable(false);

      add(topPanel);

      centerPanel.setLayout(new GridLayout(2,2));
      centerPanel.add(eArea);
      eArea.setEditable(false);
      centerPanel.add(dArea);
      dArea.setEditable(false);
      centerPanel.add(encButton);
      encButton.setEnabled(false);
      encButton.addActionListener(this);
      centerPanel.add(decButton);
      decButton.setEnabled(false);
      decButton.addActionListener(this);

      add(centerPanel);

      bottomPanel.setLayout(new GridLayout(1,2));
      bottomPanel.add(plaintextArea);
      plaintextArea.setEditable(false);
      bottomPanel.add(ciphertextArea);
      ciphertextArea.setEditable(false);

      add(bottomPanel);
   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      if (source==getKeysButton) {
         try {
               modByteLength=Integer.parseInt(byteLengthField.getText());
               sr=new SecureRandom();
               PrimeGenerator pg=new PrimeGenerator(modByteLength*4+1,10,sr);
               p=pg.getStrongPrime();
               q=pg.getStrongPrime();
               modulus=p.multiply(q);
               modArea.setText(modulus.toString());
               BigInteger pMinusOne=p.subtract(BigIntegerMath.ONE);
               BigInteger qMinusOne=q.subtract(BigIntegerMath.ONE);
               do {
                  exponent=new BigInteger(modulus.bitLength()-1,10,sr);
               } while (!exponent.gcd(pMinusOne.multiply(qMinusOne)).equals(BigIntegerMath.ONE));
               dexponent=exponent.modInverse(pMinusOne.multiply(qMinusOne));
               eArea.setText(exponent.toString());
               dArea.setText(dexponent.toString());
               getKeysButton.setEnabled(false);
               byteLengthField.setEditable(false);
               plaintextArea.setEditable(true);
               encButton.setEnabled(true);
         } catch (Exception nfe) {
            byteLengthField.setText(nfe.toString());
         }
      } else if (source==encButton) {
         msgArray=plaintextArea.getText().getBytes();
         encmsgArray=Ciphers.RSAEncipherWSalt(msgArray,exponent,modulus,sr);
         ciphertextArea.setText(new String(encmsgArray));
         plaintextArea.setText("");
         encButton.setEnabled(false);
         decButton.setEnabled(true);
      } else if (source==decButton) {
         msgArray=Ciphers.RSADecipherWSalt(encmsgArray,dexponent,modulus);
         plaintextArea.setText(new String(msgArray));
         ciphertextArea.setText("");
         decButton.setEnabled(false);
         encButton.setEnabled(true);
      }
   }

}
