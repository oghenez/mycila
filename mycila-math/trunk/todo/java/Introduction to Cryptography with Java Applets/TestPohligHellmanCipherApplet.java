import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
public class TestPohligHellmanCipherApplet extends Applet implements ActionListener {

   BigInteger dexponent=null;
   BigInteger exponent=null;
   int modBitLength=0;
   BigInteger modulus=null;
   byte[] msgArray=null;
   byte[] encmsgArray=null;

   Label bitLengthLabel=new Label("Desired bit length of prime modulus:");
   TextField bitLengthField=new TextField("0");
   Button getKeysButton=new Button("Get keys and modulus");
   TextArea modArea=new TextArea("Modulus",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
   TextArea eArea=new TextArea("Secret enciphering key",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
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
      topPanel.add(bitLengthLabel);
      topPanel.add(bitLengthField);
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
            modBitLength=Integer.parseInt(bitLengthField.getText());
            if (modBitLength<9||modBitLength>2047) bitLengthLabel.setText("Enter an integer larger than 8 but less than 2048");
            else {
               sr=new SecureRandom();
               PrimeGenerator pg=new PrimeGenerator(modBitLength,10,sr);
               modulus=pg.getSafePrime();
               modArea.setText(modulus.toString());
               BigInteger modMinusOne=modulus.subtract(BigIntegerMath.ONE);
               do {
                  exponent=new BigInteger(modBitLength-1,10,sr);
               } while (!exponent.gcd(modMinusOne).equals(BigIntegerMath.ONE));
               dexponent=exponent.modInverse(modMinusOne);
               eArea.setText(exponent.toString());
               dArea.setText(dexponent.toString());
               getKeysButton.setEnabled(false);
               bitLengthField.setEditable(false);
               plaintextArea.setEditable(true);
               encButton.setEnabled(true);
            }
         } catch (NumberFormatException nfe) {
            bitLengthField.setText(nfe.toString());
         }
      } else if (source==encButton) {
         msgArray=plaintextArea.getText().getBytes();
         encmsgArray=Ciphers.pohligHellmanEncipherWSalt(msgArray,exponent,modulus,sr);
         ciphertextArea.setText(new String(encmsgArray));
         plaintextArea.setText("");
         encButton.setEnabled(false);
         decButton.setEnabled(true);
      } else if (source==decButton) {
         msgArray=Ciphers.pohligHellmanDecipherWSalt(encmsgArray,dexponent,modulus);
         plaintextArea.setText(new String(msgArray));
         ciphertextArea.setText("");
         decButton.setEnabled(false);
         encButton.setEnabled(true);
      }
   }

}
