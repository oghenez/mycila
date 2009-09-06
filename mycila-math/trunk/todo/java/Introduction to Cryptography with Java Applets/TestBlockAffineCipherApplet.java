import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;                                    
public class TestBlockAffineCipherApplet extends Applet implements ActionListener {

   BigInteger shift=null;
   BigInteger multiplier=null;
   int blockSize=0;
   byte[] msgArray=null;
   byte[] encmsgArray=null;

   Label Label1=new Label("Plaintext");
   TextField msg=new TextField(40);
   Label Label2=new Label("Ciphertext");
   TextField encmsg=new TextField(40);
   Label shiftLabel=new Label("Shift value:");
   TextField entryShiftValue=new TextField(40);
   Label blockLabel=new Label("Block size (in bytes):");
   TextField entryBlockValue=new TextField(40);
   Label multLabel=new Label("Multiplier (must be relatively prime to modulus):");
   TextField entryMultValue=new TextField(40);
   Button encipherButton=new Button("Encipher");
   Button decipherButton=new Button("Decipher");
   TextField outmsg=new TextField("");

   public void init() {
      setLayout(new GridLayout(13,1));
      add(Label1);
      add(msg);
      add(Label2);
      add(encmsg);
      encmsg.setEditable(false);
      add(blockLabel);                                        
      add(entryBlockValue);
      add(shiftLabel);
      add(entryShiftValue);
      add(multLabel);
      add(entryMultValue);
      add(encipherButton);
      encipherButton.addActionListener(this);
      add(decipherButton);
      decipherButton.addActionListener(this);
      decipherButton.setEnabled(false);
      add(outmsg);
      outmsg.setEditable(false);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==encipherButton) {
         outmsg.setText("");
         try {
            blockSize=Integer.parseInt(entryBlockValue.getText());
         } catch (NumberFormatException nfe) {
            blockSize=1;
            entryBlockValue.setText("1");
         }
         try {
            shift=new BigInteger(entryShiftValue.getText());
         } catch (NumberFormatException nfe) {
            shift=BigInteger.valueOf(0);
            entryShiftValue.setText("0");
         }
         try {
            multiplier=new BigInteger(entryMultValue.getText());
         } catch (NumberFormatException nfe) {
            multiplier=BigInteger.valueOf(1);
            entryMultValue.setText("1");
         }
         msgArray=msg.getText().getBytes();
         try {
            encmsgArray=Ciphers.affineEncipher(msgArray,blockSize,multiplier,shift);
         } catch (IllegalArgumentException iae) {
            outmsg.setText(iae.toString());
         }
         try {
            encmsg.setText(new String(encmsgArray));
         } catch (NullPointerException npe) {
            outmsg.setText(npe.toString());
         }
         msg.setText("");
         encipherButton.setEnabled(false);
         decipherButton.setEnabled(true);
      } else if (e.getSource()==decipherButton) {
         outmsg.setText("");
         try {
            msgArray=Ciphers.affineDecipher(encmsgArray,blockSize,multiplier,shift);
         } catch (IllegalArgumentException iae) {
            outmsg.setText(iae.toString());
         }
         msg.setText(new String(msgArray));
         encmsg.setText("");
         decipherButton.setEnabled(false);
         encipherButton.setEnabled(true);
      }
   }

}
