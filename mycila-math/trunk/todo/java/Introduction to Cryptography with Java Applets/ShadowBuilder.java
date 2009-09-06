import java.math.*;
import java.security.*;
public class ShadowBuilder {

   //The shadows
   BigInteger[] shadow;

   //The moduli
   //The i-th modulus is for the i-th shadow
   BigInteger[] modulus;

   //Two other values needed to reconstruct the master key
   BigInteger randomMultiplier;
   BigInteger reconstructingPrime;

   //This constructor accepts the master key, the number of shadows to
   //generate, and the tolerance to set for the primes.  These will be
   //used for the moduli, rather than a set of pairwise relatively prime
   //integers.  The minimum number of shadows required to reconstruct
   //is set at r/2+1.
   public ShadowBuilder(BigInteger K, int r, int primeTolerance) {
       int s=r/2+1;
       int KeySize=K.bitLength();
       //Generate a probable prime reconstructingPrime larger than K
       SecureRandom sr=new SecureRandom();
       reconstructingPrime=new BigInteger(KeySize+1,primeTolerance,sr);
       //Generate r primes for the moduli
       modulus=new BigInteger[r];
       for (int index=0;index<r;index++) {
           modulus[index]=new BigInteger(KeySize+2,primeTolerance,sr);
       }
       //Choose a random multiplier less than the product of any s of the primes.
       //This will be so if the bitlength is less than (s-1)*(KeySize-1).
       randomMultiplier=new BigInteger(s*(KeySize-1)-KeySize-1,sr);
       //Compute K0=K+randomMultiplier*reconstructingPrime
       BigInteger K0=K.add(randomMultiplier.multiply(reconstructingPrime));
       //Generate the r shadows
       shadow=new BigInteger[r];
       for (int index=0;index<r;index++) {
           shadow[index]=K0.mod(modulus[index]);
       }
   }

   //Methods to return the values the constructor generated.
   public BigInteger[] getShadows() {
      return shadow;
   }
   public BigInteger[] getModuli() {
      return modulus;
   }
   public BigInteger getRandomMultiplier() {
      return randomMultiplier;
   }
   public BigInteger getReconstructingPrime() {
      return reconstructingPrime;
   }

}
