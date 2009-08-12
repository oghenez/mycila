package euler;

/**
 * http://projecteuler.net/index.php?section=problems&id=30
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem030 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        int[] powers = new int[10];
        for (int i = 0; i < 10; i++) powers[i] = (int) Math.pow(i, 5);
        int res = 0;
        for (int i = 100; i <= 354294; i++) {
            int powsum = 0;
            for (int q = i; q > 0; q = q / 10) powsum += powers[q % 10];
            if (i == powsum) res += i;
        }
        System.out.println(res + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}

/*

We must find the sum of the numbers N = abc...z = a^5 + b^5 + ... + z^5

If n has 1 digit, there is no possible sum => N has at least 2 digits

To find the maximum value, we check when the sum of the biggest possible digits is not the expected number of digits.
Example:

3 digits numbers will be found by checking from 1^5 + 0^5 + 0^5 to 9^5 + 9^5 + 9^5 = 177147
4 digits numbers will be found by checking from 1 to 9^5 + 9^5 + 9^5 + 9^5 = 236196
5 digits numbers will be found by checking from 1 to 9^5 + 9^5 + 9^5 + 9^5 + 9^5 = 295245
6 digits numbers will be found by checking from 1 to 9^5 + 9^5 + 9^5 + 9^5 + 9^5 + 9^5 = 354294 (354294 is 6 digits long)
7 digits numbers will be found by checking from 1 to 9^5 + 9^5 + 9^5 + 9^5 + 9^5 + 9^5 + 9^5 = 413343

Oups ! 413343 is 6 digits long. So it means that even if we have the maximum digit value 9, we cannot have any number composed of 7 digits with powers of 5.

So we will check numbers up to 354294.

*/