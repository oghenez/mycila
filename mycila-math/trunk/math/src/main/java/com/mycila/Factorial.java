package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

    private Factorial() {
    }

    public static int of(int n) {
        int res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }

}
