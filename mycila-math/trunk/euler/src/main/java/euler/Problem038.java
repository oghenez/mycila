package euler;

import com.mycila.Pandigital;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=38
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem038 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        Pandigital pandigital = Pandigital.base(10);
        for (int n = 9876; n >= 9183; n--) {
            // equivalent to 100000*n + 2*n to create the concatenation of the pandigital number
            final int number = (n << 5) * 3125 + (n << 1);
            if (n % 5 != 0 && pandigital.isPandigital(number, 1, 10)) {
                System.out.println(number);
            }
        }
        out.println(" in " + (currentTimeMillis() - time) + "ms");
    }
}
/*

Samples:

192 X (1,2,3) = 192384576

192*1=192
192*2=384
192*3 576

9 X (1,2,3,4,5) = 918273645

9*1=9
9*2=18
9*3=27
9*4=36
9*5=45

We call M the multiplicator we need to find.
- 918273645 is not the greatest (you can test it). So M will start with 9.
- M cannot end with 5 or 0, otherwise it will be divisible by 10 and will contain a 0 when multiplied by 2.

We know that n > 1. So for the minimal value n=2 we have:

- 9 X (1,2) => too few digits
- 98 X (1,2) => too few digits (98196)
- 987 X (1,2) => too few digits (98196294)
- 9876 X (1,2) => 9 digits (987619752) !
- 9183 X (1,2) => 9 digits (918318366) !

918273645 is not the greatest. So for a 4-digits number M, the maximum is between 9183... and 9876...  

for n = 3:

- 98 X (1,2,3) => 8 digits (98196294)
- 987 X (1,2,3) => 11 digits (98719742961)
- 919 X (1,2,3) => 11 digits (91918382757)

for n = 3, when we try with the minimal number M = 919 to give a pandigital number just after 918273645, we end up with 11 digits.

So M is clearly between 9183 and 9876, and n = 2.

*/
