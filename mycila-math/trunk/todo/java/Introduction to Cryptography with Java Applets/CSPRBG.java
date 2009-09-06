import java.math.*;
import java.security.*;
public class CSPRBG {
   BigInteger p,q,n,seed;
   public CSPRBG(byte[] seed) {
      this.seed=new BigInteger(seed);
      if (this.seed.bitLength()<515) throw new IllegalArgumentException("Seed too small");
      SecureRandom sr=new SecureRandom(seed);
      //Use a secureRandom object to get the strong primes
      PrimeGenerator pg=new PrimeGenerator(513,16,sr);
      do {p=pg.getStrongPrime();}
      while (!p.mod(BigIntegerMath.FOUR).equals(BigIntegerMath.THREE));
      do {q=pg.getStrongPrime();}
      while (!q.mod(BigIntegerMath.FOUR).equals(BigIntegerMath.THREE));
      n=p.multiply(q);
   }
   //Fills an array of bytes with random data
   public void fillBytes(byte[] array) {
      for (int i=0;i<array.length;i++) {
         //Seed is continually squared
         seed=seed.multiply(seed).mod(n);
         //Least significant byte of residue is the i-th random byte
         byte b=seed.byteValue();
         array[i]=b;
      }
   }
   //Returns a single byte of pseudorandom data
   public byte getRandomByte() {
         seed=seed.multiply(seed).mod(n);
         return seed.byteValue();
   }
}