import java.util.*;
import java.math.*;
public class IntCRT {

   //IntCRT objects are based on a series of ints modulo a series of long primes
   //The primes are stored here in this static array-all IntCRT objects will share this array
   static long[] moduli=null;

   //A parallel array of residues for each IntCRT object holds its mixed radix representation
   long[] residues=null;

   //The maximum size of the modulus - computations must not exceed this modulus
   int maxModulusBitLength=0;

   //This constructor produces the residues from the string of decimal digits
   //Also produces the prime moduli
   public IntCRT(String digitString, int maxModulusBitLength) {

      //If modulus<=64 bits, we might as well be using ints
      if (maxModulusBitLength<65) throw new IllegalArgumentException("The maximum modulus bit length must be at least 65 bits");
      this.maxModulusBitLength=maxModulusBitLength;

      //If the prime moduli are not yet set up, set them up
      if (moduli==null) setupModuli();

      //The residues are long, but each will be no larger than an int
      //This is because multiplication of residues may exceed the size of an int, requiring a long to store
      residues=new long[moduli.length];

      //Get the string and make it into a BigInteger; BigInteger only used for IO conversions, not for calculations
      BigInteger n=new BigInteger(digitString);
      if (n.compareTo(BigIntegerMath.ZERO)<0) throw new IllegalArgumentException("IntCRT objects must be nonnegative.");
      if (n.bitLength()>=maxModulusBitLength) throw new IllegalArgumentException("IntCRT objects must be less than maximum modulus bit length = "+maxModulusBitLength+".");
      //Make each residue
      for (int i=0;i<residues.length;i++) residues[i]=n.mod(BigInteger.valueOf(moduli[i])).longValue();
   }

   //Private constructor to make IntCRT object by passing in residues
   private IntCRT(long[] residues) {
      this.residues=residues;
   }

   //Set up the prime moduli
   private void setupModuli() {
      //Don't know how long array should be-start with a Vector
      Vector vector=new Vector();
      BigInteger two=BigInteger.valueOf(2);
      //Start with the largest possible int-this is an odd number
      BigInteger test=BigInteger.valueOf(Integer.MAX_VALUE);
      //bigBubba will be the product of all the primes
      BigInteger bigBubba=BigInteger.valueOf(1);
      //When this product is big enough (has long enough bit length) we have enough primes
      while (bigBubba.bitLength()<maxModulusBitLength) {
         //If test is prime, add it to the vector, and mutiply bigBubba by it
         if (test.isProbablePrime(10)) {
            vector.addElement(test);
            bigBubba=bigBubba.multiply(test);
         }
         //Subtract two from the test number-test is always odd
         test=test.subtract(two);
      }
      //We know the size of our array of primes-create the array
      moduli=new long[vector.size()];
      //Copy the prime moduli into the araay
      for (int i=0;i<vector.size();i++) moduli[i]=((BigInteger)vector.elementAt(i)).longValue();
   }

   public IntCRT add(IntCRT other) {
      //IntCRT objects must be using the same moduli
      if (maxModulusBitLength!=other.maxModulusBitLength) throw new IllegalArgumentException("IntCRT objects must have same maximum modulus bit length to be added together");
      long[] answer=new long[residues.length];
      //Add i-th residue of this to i-th residue of other, take residue mod i-th moduli
      for (int i=0;i<residues.length;i++) answer[i]=(residues[i]+other.residues[i])%moduli[i];
      return new IntCRT(answer);
   }

   public IntCRT multiply(IntCRT other) {
      //IntCRT objects must be using the same moduli
      if (maxModulusBitLength!=other.maxModulusBitLength) throw new IllegalArgumentException("IntCRT objects must have same maximum modulus bit length to be multiplied together");
      long[] answer=new long[residues.length];
      //Multiply i-th residue of this by i-th residue of other, may produce a long
      //Take residue mod i-th moduli
      for (int i=0;i<residues.length;i++) answer[i]=(residues[i]*other.residues[i])%moduli[i];
      return new IntCRT(answer);
   }

   public String toString() {
      //Make an array of BigIntegers for each modulus
      BigInteger[] m=new BigInteger[moduli.length];
      //We reconstruct a BigInteger from the residues by using the Chinese Remainder Theorem
      for (int i=0;i<moduli.length;i++) m[i]=BigInteger.valueOf(moduli[i]);
      //Make an array of BigIntegers for each residue
      BigInteger[] r=new BigInteger[residues.length];
      for (int i=0;i<residues.length;i++) r[i]=BigInteger.valueOf(residues[i]);
      //Reconstruct the BigInteger and return it as a string
      BigInteger whopper=BigIntegerMath.solveCRT(r,m)[0];
      return whopper.toString();
   }

}
