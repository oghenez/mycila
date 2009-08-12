package euler;

/**
 * http://projecteuler.net/index.php?section=problems&id=5
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem005 {
    public static void main(String[] args) {
        // 2520 is the smallest number having [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] as divisors
        // => prime decomposition gives: 2520 = 2^3 * 3^2 * 5 * 7, which includes all divisors

        // from 11 to 20:
        // - 11, 13, 17, 19 are primes => we will add them
        // - 12, 14, 15, 18, 20 are already divisors of 2520 (prime decomposition of these numbers is included in prime decomposition of 2520)
        // - 16 is 2^4, so we have to add only one 2

        // => our number N = 2^4 * 3^2 * 5 * 7 * 11 * 13 * 17 * 19 = 232792560
    }
}