import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.security.*;
public class CipherChatServer extends Frame implements ActionListener {

   private Button disconnectButton=new Button("Disconnect Client");
   private TextField enterField=new TextField();
   private TextArea displayArea=new TextArea();
   private Panel top=new Panel();
   private Panel bottom=new Panel();

   private DataOutputStream output;
   private DataInputStream input;
   private String message="";

   private static ServerSocket server;
   private Socket connection;

   private BigInteger p,q,modulus,decipherExp,recipModulus;
   private SecureRandom sr=new SecureRandom();
   private int ciphertextBlockSize;
   private int plaintextBlockSize;

   public CipherChatServer() {
      //Lay components on frame and display it
      super("Cipher Chat Server");
      //Establish keys for RSA cipher
      makeKeys();
      setLayout(new GridLayout(2,1));
      top.setLayout(new GridLayout(2,1));
      bottom.setLayout(new GridLayout(1,1));
      add(top);
      add(bottom);
      disconnectButton.setEnabled(false);
      disconnectButton.addActionListener(this);
      top.add(disconnectButton);
      enterField.setEnabled(false);
      enterField.addActionListener(this);
      top.add(enterField,BorderLayout.NORTH);
      bottom.add(displayArea);
      setSize(400,300);
      show();
   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      //User pressed enter in message entry field-send it
      if (source==enterField) {
         //Get the message
         message=e.getActionCommand();
         try {
            //Encipher the message
            if (message.length()>plaintextBlockSize) message=message.substring(0,plaintextBlockSize);
            byte[] ciphertext=Ciphers.RSAEncipherWSalt(message.getBytes(),BigIntegerMath.THREE,recipModulus,sr);
            //Send to the client
            output.write(ciphertext);
            output.flush();
            //Display same message in output area
            displayArea.append("\n"+message);
            enterField.setText("");
         } catch ( IOException ioe ) {
            displayArea.append("\nError writing message");
         }
      //Server wishes disconnect from the client
      } else if (source==disconnectButton) {
            try {
               byte[] lastMsg=new byte[1];
               lastMsg[0]=0;
               output.write(Ciphers.RSAEncipherWSalt(lastMsg,BigIntegerMath.THREE,recipModulus,sr));
               output.flush();
               closeAll();
            } catch (IOException ioe) {
               displayArea.append("\nError in disconnecting");
            }
      }
   }

   private void makeKeys() {
      PrimeGenerator pg=new PrimeGenerator(513,10,sr);
      do {
         p=pg.getStrongPrime();
      } while(p.subtract(BigIntegerMath.ONE).mod(BigIntegerMath.THREE).equals(BigIntegerMath.ZERO));
      do {
         q=pg.getStrongPrime();
      } while(q.subtract(BigIntegerMath.ONE).mod(BigIntegerMath.THREE).equals(BigIntegerMath.ZERO));
      modulus=p.multiply(q);
      //Use 3 as enciphering exponent - OK since we are using salt
      decipherExp=BigIntegerMath.THREE.modInverse(p.subtract(BigIntegerMath.ONE).multiply(q.subtract(BigIntegerMath.ONE)));
      ciphertextBlockSize=(modulus.bitLength()-1)/8+1;
      //Maximum size of plaintext is 6 bytes less than ciphertext
      //1 to get under modulus
      //4 for the salt
      //1 for a pad byte
      plaintextBlockSize=ciphertextBlockSize-6;
   }

   private void exchangeKeys() {
      try {
         byte[] buffer=new byte[ciphertextBlockSize];
         input.read(buffer);
         recipModulus=new BigInteger(1,buffer);
         output.write(modulus.toByteArray());
      } catch (IOException ioe) {
         System.err.println("Error establishing keys");
      }
   }

   public void go() {
      try {
         while (true) {
            displayArea.setText("Waiting for connection");
            //accept() halts execution until a connection is made from a client
            connection = server.accept();

            displayArea.append("\nConnection received from:"+connection.getInetAddress().getHostName());

            //Set up the IO streams
            output = new DataOutputStream(connection.getOutputStream());
            output.flush();
            input = new DataInputStream(connection.getInputStream() );

            //Exchange public keys with the client-send yours, get theirs
            exchangeKeys();

            //Send connection message to client
            message = connection.getLocalAddress().getLocalHost()+":Connection successful";
            byte[] ciphertext=Ciphers.RSAEncipherWSalt(message.getBytes(),BigIntegerMath.THREE,recipModulus,sr);
            //Send to the client
            output.write(ciphertext);
            output.flush();

            //Enable disconnect button
            disconnectButton.setEnabled(true);
            //Messages may now be entered
            enterField.setEnabled(true);

            try {
               //Read as long as there is input
               byte[] buffer=new byte[ciphertextBlockSize];
               boolean disconnectMsgSent=false;
               while (!disconnectMsgSent&&input.read(buffer)!=-1) {
                  //Decipher the bytes read in
                  byte[] plaintext=Ciphers.RSADecipherWSalt(buffer,decipherExp,modulus);
                  if (plaintext.length==1&&plaintext[0]==0) {
                     disconnectMsgSent=true;
                     closeAll();
                  } else {
                     //convert to a string and display
                     message = new String(plaintext);
                     displayArea.append("\n"+message);
                  }
               }
            //Socket was closed from client side
            } catch (SocketException se) {
               //close connection and IO streams, change some components
               closeAll();
            }
            closeAll();
         }
      } catch (Exception exc) {
         exc.printStackTrace();
      }
   }

   //Close socket and IO streams, change appearance/functionality of some components
   private void closeAll() throws IOException {
      displayArea.append("\nConnection closing");
      output.close();
      input.close();
      connection.close();
      //Disable message entry
      enterField.setEnabled(false);
      //We are not connected-turn off the disconnect button
      disconnectButton.setEnabled(false);
   }

   public static void main(String[] args) throws IOException {
      CipherChatServer ccs=new CipherChatServer();
      ccs.addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               System.exit(0);
            }
         }
      );
      server = new ServerSocket(55555);
      ccs.go();
      server.close();
   }

}
