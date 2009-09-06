/*************************************************************************
 *  Compilation:  javac RandomPrime.java
 *  Execution:    java RandomPrime N
 *  
 *  Generate a N-bit integer that is (probably) prime.
 *
 *************************************************************************/

import java.math.BigInteger;
import java.util.Random;
import java.security.SecureRandom;
    
public class RandomPrime {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        SecureRandom random = new SecureRandom();
        BigInteger prime = BigInteger.probablePrime(N, random);
        System.out.println(prime);
    }
}

