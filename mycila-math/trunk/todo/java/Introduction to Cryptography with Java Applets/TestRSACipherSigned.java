import java.io.*;
import java.math.*;
import java.security.*;
public class TestRSACipherSigned {
 static BufferedReader k=new BufferedReader(new InputStreamReader(System.in));
 public static void main(String[] args) throws IOException {
   PrintWriter signedMsgFile=new PrintWriter(new FileWriter("RSASignedMessage.txt"));
   try {

      System.out.println("Enter the message:");
      String s=k.readLine();
      byte[] msg=s.getBytes();
      signedMsgFile.println("Plaintext message:");
      signedMsgFile.println(s);      

      SecureRandom sr=new SecureRandom();
      BigInteger ps=new BigInteger(513,10,sr);
      BigInteger qs=new BigInteger(513,10,sr);
      BigInteger ns=ps.multiply(qs);
      BigInteger psm1=ps.subtract(BigIntegerMath.ONE);
      BigInteger qsm1=qs.subtract(BigIntegerMath.ONE);
      BigInteger es;
      BigInteger prods=psm1.multiply(qsm1);
      do {
         es=new BigInteger(1020,sr);
      } while (!es.gcd(prods).equals(BigIntegerMath.ONE));
      BigInteger ds=es.modInverse(prods);

      signedMsgFile.println("Sender's keys:");
      signedMsgFile.println("n="+ns);
      signedMsgFile.println("e="+es);
      signedMsgFile.println("d="+ds);

      BigInteger pr=new BigInteger(513,10,sr);
      BigInteger qr=new BigInteger(513,10,sr);
      BigInteger nr=pr.multiply(qr);
      BigInteger prm1=pr.subtract(BigIntegerMath.ONE);
      BigInteger qrm1=qr.subtract(BigIntegerMath.ONE);
      BigInteger er;
      BigInteger prodr=prm1.multiply(qrm1);
      do {
         er=new BigInteger(1020,sr);
      } while (!er.gcd(prodr).equals(BigIntegerMath.ONE));
      BigInteger dr=er.modInverse(prodr);

      signedMsgFile.println("Recipient's keys:");
      signedMsgFile.println("n="+nr);
      signedMsgFile.println("e="+er);
      signedMsgFile.println("d="+dr);

      byte[] emsg=Ciphers.RSAEncipherSigned(msg,ds,ns,er,nr,sr);
      signedMsgFile.println("Doubly enciphered signed message:");
      signedMsgFile.println(new String(emsg));

      byte[] msgrecover=Ciphers.RSADecipherSigned(emsg,dr,nr,es,ns);
      signedMsgFile.println("Doubly deciphered signed message:");
      signedMsgFile.println(new String(msgrecover));

      signedMsgFile.close();

   } catch (Exception exc) {exc.printStackTrace();}

 k.readLine();
 }
}