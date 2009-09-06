import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.security.*;
public class TestRabinCipherApplet extends Applet implements ActionListener {

   SecureRandom sr=new SecureRandom();
   TextField minByteLengthField=new TextField();
   Button makeModulus=new Button("Produce Rabin Modulus");
   TextArea pArea=new TextArea();
   TextArea qArea=new TextArea();
   TextArea mArea=new TextArea();
   TextField plaintextField=new TextField();
   TextField ciphertextField=new TextField();
   Button cipherButton=new Button("Encipher/Decipher");
   String[] captions={"Minimum byte length of Rabin modulus",
      "Prime p",
      "Prime q",
      "Rabin modulus m=pq",
      "Plaintext",
      "Ciphertext"};
   Label[] label = new Label[captions.length];
   TextArea msg=new TextArea();

   int len=65;
   BigInteger p,q,m;
   byte[] plaintext,ciphertext;
   boolean enciphered=false;

public void init() {
   for (int i=0;i<captions.length;i++) label[i]=new Label(captions[i]);
   setLayout(new GridLayout(15,1));
   ciphertextField.setEditable(false);
   pArea.setEditable(false);
   qArea.setEditable(false);
   mArea.setEditable(false);
   cipherButton.setEnabled(false);
   add(label[0]);
   add(minByteLengthField);
   add(makeModulus);
   makeModulus.addActionListener(this);
   add(label[1]);
   add(pArea);
   add(label[2]);
   add(qArea);
   add(label[3]);
   add(mArea);
   add(label[4]);
   add(plaintextField);
   add(label[5]);
   add(ciphertextField);
   add(cipherButton);
   cipherButton.addActionListener(this);
   add(msg);
   msg.setEditable(false);
}

public void actionPerformed(ActionEvent e) {
   Object source=e.getSource();
   if (source==makeModulus) {
      try {
         msg.setText("");
         len=Integer.parseInt(minByteLengthField.getText());
         PrimeGenerator pg=new PrimeGenerator(4*len,10,sr);
         do {
            p=pg.getStrongPrime();
         } while (!p.mod(BigIntegerMath.FOUR).equals(BigIntegerMath.THREE));
         do {
            q=pg.getStrongPrime();
         } while (!q.mod(BigIntegerMath.FOUR).equals(BigIntegerMath.THREE));
         m=p.multiply(q);
         pArea.setText(p.toString());
         qArea.setText(q.toString());
         mArea.setText(m.toString());
         minByteLengthField.setEditable(false);
         makeModulus.setEnabled(false);
         cipherButton.setEnabled(true);
      } catch (Exception exc) {
         msg.setText(exc.toString());
      }
   } else if (source==cipherButton) {
      try {
         msg.setText("");
         if (!enciphered) {
            plaintext=plaintextField.getText().getBytes();
            ciphertext=Ciphers.rabinEncipherWSalt(plaintext,m,sr);
            ciphertextField.setText(new String(ciphertext));
            plaintextField.setText("");
            enciphered=true;
         } else {
            plaintext=Ciphers.rabinDecipherWSalt(ciphertext,p,q);
            plaintextField.setText(new String(plaintext));
            ciphertextField.setText("");
            enciphered=false;
         }
      } catch (Exception exc) {
         msg.setText(exc.toString());
      }
   }
}

}