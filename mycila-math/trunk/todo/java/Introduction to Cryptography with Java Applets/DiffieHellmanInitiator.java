import java.security.*;
import java.math.*;
import java.net.*;
import java.io.*;
public class DiffieHellmanInitiator {
   static BufferedReader k=new BufferedReader(new InputStreamReader(System.in));
   public static void main(String[] args) throws IOException {
      //Make a safe prime and generator
      SecureRandom sr=new SecureRandom();
      PrimeGenerator pg=new PrimeGenerator(1025,10,sr);
      BigInteger[] pandg=pg.getSafePrimeAndGenerator();
      //Make your secret exponent
      BigInteger x=new BigInteger(pandg[0].bitLength()-1,sr);
      //Raise g to this power
      BigInteger gtox=pandg[1].modPow(x,pandg[0]);
      //Open a connection with a server waiting for info
      System.out.println("Enter host name or IP address of server:");
      String host=k.readLine();
      //Server should be listening on port 11111
      Socket socket=new Socket(host,11111);
      //Open input and output streams on the socket
      BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintStream out=new PrintStream(socket.getOutputStream());
      //Send the values p,g,gtox to server
      out.println(pandg[0]);
      out.println(pandg[1]);
      out.println(gtox);
      //Get the gtoy value from server
      BigInteger gtoy=new BigInteger(in.readLine());
      //Raise gtoy to x power-this is the secret key
      BigInteger key=gtoy.modPow(x,pandg[0]);
      System.out.println("The secret key is:\n"+key);
      k.readLine();
   }
}