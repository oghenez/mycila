import java.security.*;
import java.math.*;
import java.net.*;
import java.io.*;
public class DiffieHellmanListener {
   public static void main(String[] args) throws IOException {
      //Start by listening on port 11111
      ServerSocket ss=new ServerSocket(11111);
      //Wait for a connection
      Socket socket=ss.accept();
      //Open input and output streams on the socket
      BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintStream out=new PrintStream(socket.getOutputStream());
      //Capture p,g,gtox values from client
      BigInteger p=new BigInteger(in.readLine());
      BigInteger g=new BigInteger(in.readLine());
      BigInteger gtox=new BigInteger(in.readLine());
      //Produce your own secret exponent
      SecureRandom sr=new SecureRandom();
      BigInteger y=new BigInteger(p.bitLength()-1,sr);
      //Raise g to this power
      BigInteger gtoy=g.modPow(y,p);
      //Send this to client
      out.println(gtoy);
      //Raise gtox to y power-this is the secret key
      BigInteger key=gtox.modPow(y,p);
      System.out.println("The secret key with "+socket.getInetAddress().toString()+" is:\n"+key);
      int c=System.in.read();
   }
}