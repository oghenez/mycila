import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestCaesarCipherApplet extends Applet implements ActionListener {

   int shift=0;
   byte[] msgArray=null;
   byte[] encmsgArray=null;

   Label titleLabel=new Label("Caesar Cipher Demonstration");
   Label Label1=new Label("Plaintext");
   TextField msg=new TextField(40);
   Label Label2=new Label("Ciphertext");
   TextField encmsg=new TextField(40);
   Label shiftLabel=new Label("Shift value (0-255):");
   TextField entryShiftValue=new TextField(40);
   Button encipherButton=new Button("Encipher");
   Button decipherButton=new Button("Decipher");

   public void init() {
      setLayout(new GridLayout(9,1));
      add(titleLabel);
      add(Label1);
      add(msg);
      add(Label2);
      add(encmsg);
      encmsg.setEditable(false);
      add(shiftLabel);
      add(entryShiftValue);
      add(encipherButton);
      encipherButton.addActionListener(this);
      add(decipherButton);
      decipherButton.addActionListener(this);
      decipherButton.setEnabled(false);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==encipherButton) {
         try {
            shift=Integer.parseInt(entryShiftValue.getText());
         } catch (NumberFormatException nfe) {
            shift=0;
         }
         msgArray=msg.getText().getBytes();
         encmsgArray=caesarEncipher(msgArray,shift);
         encmsg.setText(new String(encmsgArray));
         msg.setText("");
         encipherButton.setEnabled(false);
         decipherButton.setEnabled(true);
      } else if (e.getSource()==decipherButton) {
         msgArray=caesarDecipher(encmsgArray,shift);
         msg.setText(new String(msgArray));
         encmsg.setText("");
         decipherButton.setEnabled(false);
         encipherButton.setEnabled(true);
      }
   }

//The enciphering method.
   private static byte[] caesarEncipher(byte[] message,int shift) {
      byte[] m2=new byte[message.length];
      for (int i=0;i<message.length;i++) {
         m2[i]=(byte)((message[i]+shift)%256);
      }
      return m2;
   }

//The deciphering method.
   private static byte[] caesarDecipher(byte[] message,int shift) {
      byte[] m2=new byte[message.length];
      for (int i=0;i<message.length;i++) {
         m2[i]=(byte)((message[i]+(256-shift))%256);
      }
      return m2;
   }

}
