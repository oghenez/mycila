import javax.swing.*;
public class CaesarCipherDemo {
   public static void main(String[] args) {
      int shift=0;
      try {
        shift=new Integer(JOptionPane.showInputDialog("Enter the Caesar shift (0-255):")).intValue();
      } catch(NumberFormatException e) {
        JOptionPane.showMessageDialog(null,e.toString());
        System.exit(1);
      }
      String plaintext=JOptionPane.showInputDialog("Enter the plaintext:");
      //Convert the plaintext into an array of bytes.
      byte[] b=plaintext.getBytes();
      //Encipher the byte array.
      b=caesarEncipher(b,shift);
      //Convert the byte array back to a string.
      String ciphertext=new String(b);
      JOptionPane.showMessageDialog(null,"The ciphertext is: "+ciphertext);
      //Decipher the enciphered byte array.
      b=caesarDecipher(b,shift);
      //Convert the byte array back to a string.
      plaintext=new String(b);
      JOptionPane.showMessageDialog(null,"The recovered plaintext is: "+plaintext);
      System.exit(0);
   }

   //The enciphering method.
   public static byte[] caesarEncipher(byte[] message,int shift) {
      byte[] m2=new byte[message.length];
      for (int i=0;i<message.length;i++) {
         m2[i]=(byte)((message[i]+shift)%256);
      }
      return m2;
   }

   //The deciphering method.
   public static byte[] caesarDecipher(byte[] message,int shift) {
      byte[] m2=new byte[message.length];
      for (int i=0;i<message.length;i++) {
         m2[i]=(byte)((message[i]+(256-shift))%256);
      }
      return m2;
   }
}
