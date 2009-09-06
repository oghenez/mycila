import java.math.*;
public class KeyRebuilder {

	BigInteger masterKey;

   //This constructor reconstructs the master key from a sequence of shadows
   //and moduli.  It is assumed that enough shadows are being used to do this.
   //It is further assumed that the moduli are pairwise relatively prime
   public KeyRebuilder(BigInteger[] shadow, BigInteger[] modulus,
       BigInteger randomMultiplier, BigInteger reconstructingPrime) {
      //Produce a parallel array for each Mi, product of all mj where i!=j for CRT
       BigInteger[] M;
       M=new BigInteger[modulus.length];
      //BigM is the product of all the moduli
       BigInteger BigM=new BigInteger("1");
       for (int index=0;index<modulus.length;index++) {
                //Multiply BigM by modulus[index]
                BigM=BigM.multiply(modulus[index]);
                //Start forming each M[index]
                M[index]=new BigInteger("1");
                for (int index2=0;index2<modulus.length;index2++) {
                       //If index=index2, do not multiply M[index] by m[index2]
                       if(index!=index2) {
                           M[index]=M[index].multiply(modulus[index2]);
                       }
                }
       }
       BigInteger K0=new BigInteger("0");
      //Produce K0 using the Chinese Remainder Theorem with the shadows
       for (int index=0;index<modulus.length;index++) {
              BigInteger MInv=M[index].modInverse(modulus[index]);
              K0=K0.add(shadow[index].multiply(M[index].multiply(MInv))).mod(BigM);
       }
       //The master key is K0 - tp where t is multiplier, p is reconstructing prime
       masterKey=K0.subtract(randomMultiplier.multiply(reconstructingPrime));
   }

   //Method to return the master key
   public BigInteger getMasterKey() {
       return masterKey;
   }

}
