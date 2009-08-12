package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class PrimaltyTest {

    private PrimaltyTest() {
    }

    public static boolean trialDivision(int number) {
        if (number == 2 || number == 3) return true;
        if (number < 2 || (number & 1) == 0) return false;
        if (number < 9) return true;
        if (number % 3 == 0) return false;
        int r = (int) Math.sqrt(number);
        int f = 5;
        while (f <= r) {
            if (number % f == 0 || number % (f + 2) == 0)
                return false;
            f += 6;
        }
        return true;
    }

    public static boolean millerRabin(int number) {
        return number > 1
                && (number == 2
                || millerRabinPass(2, number)
                && (number <= 7 || millerRabinPass(7, number))
                && (number <= 61 || millerRabinPass(61, number)));
    }

    private static boolean millerRabinPass(int a, int n) {
        int d = n - 1;
        int s = Integer.numberOfTrailingZeros(d);
        d >>= s;
        int a_to_power = (int) Mod.pow((long) a, (long) d, (long) n);
        if (a_to_power == 1) return true;
        for (int i = 0; i < s - 1; i++) {
            if (a_to_power == n - 1) return true;
            a_to_power = Mod.pow(a_to_power, 2, n);
        }
        return a_to_power == n - 1;
    }
}
