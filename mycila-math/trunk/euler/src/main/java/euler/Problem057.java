package euler;

import com.mycila.old.MathsOld;

import static java.lang.System.*;
import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=57
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem057 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();

        BigInteger p = BigInteger.valueOf(1);
        BigInteger q = BigInteger.valueOf(1);
        final BigInteger TWO = BigInteger.valueOf(2);
        int count = 0;
        for (long i = 1; i <= 1000; i++) {
            final BigInteger nextP = p.add(q.multiply(TWO));
            q = p.add(q);
            p = nextP;
            if (MathsOld.length(p) > MathsOld.length(q)) {
                count++;
                System.out.println(p + "/" + q);
            }
        }

        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

}

/*

http://en.wikipedia.org/wiki/Square_root_of_2#Continued_fraction_representation

We define the fraction p/q for each iteration.

p(0)/q(0)=>1/1
p(1)/q(1)=>3/2
p(2)/q(2)=>7/5
p(3)/q(3)=>17/12
p(4)/q(4)=>41/29

p(n)/q(n)=>(p(n-1)+2*q(n-1))/(p(n-1)+q(n-1))

*/