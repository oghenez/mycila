package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Palindroms {

    private final Digits digits;

    private Palindroms(int base) {
        digits = Digits.base(base);
    }

    public boolean isPalindromic(int number) {
        return number == digits.reverse(number);
    }

    public static Palindroms base(int base) {
        return new Palindroms(base);
    }
}
