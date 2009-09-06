import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.security.*;
public class CipherChatClient extends Frame implements ActionListener {

   private TextField serverField=new TextField();
   private Button connectButton=new Button("Connect to server above");
   private TextField enterField=new TextField();
   private TextArea displayArea = new TextArea();
   private Panel top=new Panel();
   private Panel bottom=new Panel();

   private DataOutputStream output;
   private DataInputStream input;

   private String message="";
   private String chatServer;

   private Socket connection=null;

   private BigInteger p,q,modulus,decipherExp,recipModulus;
   private SecureRandom sr=new SecureRandom();
   private int ciphertextBlockSize;
   private int plaintextBlockSize;

   private static Thread listener=null;

   public CipherChatClient() {
      super("Cipher Chat Client");
      //Establish keys for RSA cipher
      makeKeys();
      //Lay out the components and display the frame
      setLayout(new GridLayout(2,1));
      top.setLayout(new GridLayout(3,1));
      add(top);
      bottom.setLayout(new GridLayout(1,1));
      add(bottom);
      connectButton.addActionListener(this);
      enterField.setEnabled(false);
      enterField.addActionListener(this);
      top.add(serverField);
      top.add(connectButton);
      top.add(enterField);
      bottom.add(displayArea);
      setSize(400,300);
      show();
   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      //Client pressed enter in the message entry field-send it
      if (source==enterField) {
         //Get the message
         message=e.getActionCommand();
         try {
            //Encipher the message
            if (message.length()>plaintextBlockSize) message=message.substring(0,plaintextBlockSize);
            byte[] ciphertext=Ciphers.RSAEncipherWSalt(message.getBytes(),BigIntegerMath.THREE,recipModulus,sr);
            //Send to the server
            output.write(ciphertext);
            output.flush();
            //Display same message in client output area
            displayArea.append("\n"+message);
            enterField.setText("");
         } catch (IOException ioe) {
            displayArea.append("\nError writing message");
         }
      } else if (source==connectButton) {
         if (connection!=null) { //Already connected-button press now means disconnect
            try {
               //Send final message of 0
               byte[] lastMsg=new byte[1];
               lastMsg[0]=0;
               output.write(Ciphers.RSAEncipherWSalt(lastMsg,BigIntegerMath.THREE,recipModulus,sr));
               output.flush();
               //close connection and IO streams, change some components
               closeAll();
            } catch (IOException ioe) {
               displayArea.append("\nError closing connection");
            }
         } else {//Not connected-connect
            //Get name of server to connect to
            chatServer=serverField.getText();
            displayArea.setText("Attempting connection to "+chatServer);
            try {
               //Set up the socket
               connection = new Socket(chatServer,55555);

               displayArea.append("\nConnected to: "+connection.getInetAddress().getHostName());

               //Set up the IO streams
               output = new DataOutputStream(connection.getOutputStream());
               output.flush();
               input = new DataInputStream(connection.getInputStream());

               //Exchange public keys with the server-send yours, get theirs
               exchangeKeys();

               //Change appearance/functionality of some components
               serverField.setEditable(false);
               connectButton.setLabel("Disconnect from server above");
               enterField.setEnabled(true);
               //Set up a thread to listen for the connection
               listener = new Thread(
                  new Runnable() {
                     public void run() {
                        go();
                     }
                  }
               );
               listener.start();
            } catch (IOException ioe) {
               displayArea.append("\nError connecting to "+chatServer);
            }
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
         output.write(modulus.toByteArray());
         byte[] buffer=new byte[ciphertextBlockSize];
         input.read(buffer);
         recipModulus=new BigInteger(1,buffer);
      } catch (IOException ioe) {
         System.err.println("Error establishing keys");
      }
   }

   private void go() {
      try {
         //Read as long as there is input
         byte[] buffer=new byte[ciphertextBlockSize];
         boolean disconnectMsgSent=false;
         while (connection!=null&&!disconnectMsgSent&&input.read(buffer)!=-1) {
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
      } catch (IOException ioe) {
         //Server disconnected-we can reconnect if we wish
      }
   }

   //Close socket and IO streams, change appearance/functionality of some components
   private void closeAll() throws IOException {
      displayArea.append("\nConnection closing");
      output.close();
      input.close();
      connection.close();
      //We are no longer connected
      connection=null;
      //Change components
      serverField.setEditable(true);
      connectButton.setLabel("Connect to server above");
      enterField.setEnabled(false);
   }

   public static void main( String args[] ) throws IOException {
      final CipherChatClient ccc = new CipherChatClient();
      ccc.addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               System.exit(0);
            }
         }
      );
   }

}

