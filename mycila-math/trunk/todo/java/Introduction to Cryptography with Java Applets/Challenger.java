import java.math.*;
import java.net.*;
import java.io.*;
import java.security.*;
public class Challenger extends Thread {
   static BufferedReader k=new BufferedReader(new InputStreamReader(System.in));

   //socket is the connection between challenger and respondent
   Socket socket=null;
   //trials is the number of challenges the respondent must satisfy
   static int trials=0;
   static SecureRandom r=null;

   //The constructor only sets the socket field
   public Challenger(Socket s) {
      socket=s;
   }

   public static void main(String[] args) throws IOException {
      System.out.println("Enter the number of challenges to issue per respondent:");
      trials=Integer.parseInt(k.readLine());
      r=new SecureRandom();
      //Bind the challenger to port 12345
      ServerSocket ss=new ServerSocket(12345);
      //Loop forever
      while (true) {
         //Create a new thread for every incoming connection
         //this allows challenger to handle multiple respondents
         Challenger c=new Challenger(ss.accept());
         c.start();
      }
   }

   public void run() {
      try {
         System.out.println("Request received from "+socket.getInetAddress().toString());
         //Create the IO streams
         PrintStream out=new PrintStream(socket.getOutputStream());
         BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
         //Read in modulus and s key values
         //These should be checked against a database with a TTP
         BigInteger modulus=new BigInteger(in.readLine());
         BigInteger s=new BigInteger(in.readLine());
         BigInteger[] z=new BigInteger[2];
         //Begin challenging the respondent
         for (int i=0;i<trials;i++) {
            //Read in z1 and z2; here labeled z0 and z1 for convenience
            z[0]=new BigInteger(in.readLine());
            z[1]=new BigInteger(in.readLine());
            //Check that their product = s
            if (!z[0].multiply(z[1]).mod(modulus).equals(s)) {
               System.out.println("Product not congruent to s-closing connection");
               break;
            }
            //Issue the challenge-a random 0 or 1
            int challenge=Math.abs(r.nextInt())%2;
            out.println(challenge);
            //Get the response
            BigInteger response=new BigInteger(in.readLine());
            //Check the response, based on the value of challenge
            if (!response.modPow(BigIntegerMath.TWO,modulus).equals(z[challenge])) {
               System.out.println("Response does not check-closing connection");
               break;
            }
            if (i<trials-1) out.println("N");
            else {
               out.println("Y");
               System.out.println("Respondent approved.");
            }
         }
         //Close the connection with this respondent
         socket.close();
      } catch (IOException ioe) {
         System.out.println(ioe.toString());
      }
   }

}
