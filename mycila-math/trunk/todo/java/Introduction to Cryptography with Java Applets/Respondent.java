import java.math.*;
import java.net.*;
import java.io.*;
import java.security.*;
public class Respondent {
   static BufferedReader k=new BufferedReader(new InputStreamReader(System.in));
   public static void main(String[] args) throws IOException {
      //Define some handy values
      BigInteger zero=BigInteger.valueOf(0);
      BigInteger one=BigInteger.valueOf(1);
      BigInteger two=BigInteger.valueOf(2);
      BigInteger three=BigInteger.valueOf(3);
      BigInteger four=BigInteger.valueOf(4);
      //Generate two strong primes congruent to 3 mod 4
      SecureRandom sr=new SecureRandom();
      PrimeGenerator pg=new PrimeGenerator(513,10,sr);
      BigInteger p=null,q=null;
      do {
         p=pg.getStrongPrime();
      } while (!p.mod(four).equals(three));
      do {
         q=pg.getStrongPrime();
      } while (!q.mod(four).equals(three));
      //Form the modulus as the product of these primes
      BigInteger modulus=p.multiply(q);
      //Choose a random value t and square it modulo the modulus to form s
      BigInteger t=new BigInteger(modulus.bitLength()-1,sr);
      //s is your identifying number
      BigInteger s=t.modPow(two,modulus);
      //The values of s and the modulus should be made publicly available
      //with a Trusted Third Party (TTP)
      System.out.println("Enter host name or IP address of challenger:");
      String host=k.readLine();
      Socket socket=new Socket(host,12345);
      PrintStream out=new PrintStream(socket.getOutputStream());
      BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      //Send the values for the modulus, and s, to the challenger-we do not send t
      out.println(modulus.toString());
      out.println(s.toString());
      String approved="N";
      //The challenges begin
      do {
         System.out.println("You have yet to be approved.");
         //Generate the random value r
         BigInteger r=new BigInteger(modulus.bitLength()-1,sr);
         //Compute z1 and z2
         BigInteger z1=r.modPow(two,modulus);
         BigInteger z2=s.multiply(z1.modInverse(modulus)).mod(modulus);
         //Send z1 and z2 to the challenger
         out.println(z1.toString());
         out.println(z2.toString());
         //Challenger will send back a 0 or a 1
         int challenge=Integer.parseInt(in.readLine());
         //If a 0 was sent, return r to the challenger
         if (challenge==0) out.println(r.toString());
         //Otherwise, send tr' modulo the modulus to the challenger
         else out.println(t.multiply(r.modInverse(modulus)).mod(modulus).toString());
         //Challenger will now either send "Y" (approved) or "N" (not approved)
         approved=in.readLine().toUpperCase();
      } while (!approved.equals("Y"));
      //If we get here, we succeeded
      System.out.println("Your claim of identity has been accepted.");
      k.readLine();
   }
}
