import java.math.*;
import java.security.*;
public class MASH2 {

   //Define some handy values
   static BigInteger two=BigInteger.valueOf(2);
   static BigInteger ten=BigInteger.valueOf(10);//=binary 1010
   static BigInteger fifteen=BigInteger.valueOf(15);//=binary 1111
   static BigInteger sixteen=BigInteger.valueOf(16);
   static BigInteger exp=BigInteger.valueOf(257);

   BigInteger modulus;

   public MASH2(BigInteger modulus) {
      this.modulus=modulus;
   }

   public byte[] digestOf(byte[] msg) {
      //Convert message to BigInteger-easier to work with
      //Ensure the BigInteger is positive using BigInteger(int signum,byte[] b) constructor
      BigInteger msgInt=new BigInteger(1,msg);
      //b is bitlength of msg
      BigInteger b=BigInteger.valueOf(msgInt.bitLength());
      //n is largest multiple of 16 not exceeding bitlength of modulus
      int n=modulus.bitLength()/16*16;
      //Check that msg is not too large for use with MASH2
      if (b.compareTo(two.pow(n/2))>0) throw new IllegalArgumentException("Message is too large");
      //Pad msg with enough zeros to make it a multiple of n/2
      int amountToShift=msgInt.bitLength()%(n/2)==0?0:(n/2)-msgInt.bitLength()%(n/2);
      msgInt=msgInt.shiftLeft(amountToShift);

      //Define variable for 2 raised to n power
      BigInteger twon=two.pow(n);
      //Define inititalization vector H
      BigInteger H=BigInteger.valueOf(0);
      //Define n bit binary numeric constant A=11110000...0000
      BigInteger A=BigInteger.valueOf(15).multiply(two.pow(n-4));

      //Process the first t blocks
      int t=msgInt.bitLength()/(n/2);
      BigInteger prevH;
      for (int i=0;i<t;i++) {
         prevH=H;
         H=BigInteger.valueOf(0);
         //Process the 4 bit nybbles-there are n/8 of them
         BigInteger rem;
         for (int j=n/2-4;j>=0;j-=4) {
            //Each byte begins with 1111B
            H=H.shiftLeft(4).or(fifteen);
            //Shift msg to right and keep last 4 bits
            rem=msgInt.shiftRight(j+n/2*(t-1-i)).mod(sixteen);
            //Append this remainder to H
            H=H.shiftLeft(4).or(rem);
         }
         //Compute the new digest value for H
         H=prevH.xor(H).or(A).modPow(exp,modulus).mod(twon).xor(prevH);
      }
      //Process the t+1 block
      prevH=H;
      H=BigInteger.valueOf(0);
      //Process the 4 bit nybbles
      BigInteger rem;
      for (int j=n/2-4;j>=0;j-=4) {
         //Each byte in last block begins with 1010B
         H=H.shiftLeft(4).or(ten);
         //Shift b to right and keep last 4 bits
         rem=b.shiftRight(j).mod(sixteen);
         //Append this remainder to H
         H=H.shiftLeft(4).or(rem);
      }
      //Compute the final digest value as a BigInteger
      H=prevH.xor(H).or(A).modPow(exp,modulus).mod(twon).xor(prevH);
      //Convert to a byte array and return-call helper method getBytes().
      return getBytes(H);
   }

   //Converting BigInteger to a byte array may force an extra byte for a sign bit.
   //We remove this byte if it is produced
   private static byte[] getBytes(BigInteger big) {
      byte[] bigBytes=big.toByteArray();
      if (big.bitLength()%8!=0) return bigBytes;
      else {
         byte[] smallerBytes=new byte[big.bitLength()/8];
         System.arraycopy(bigBytes,1,smallerBytes,0,smallerBytes.length);
         return smallerBytes;
      }
   }

}
