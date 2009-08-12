package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PalindromsTest {

    @Test
    public void test_isPalindromic() {
        assertTrue("isPalindromic(0)", Palindroms.base(10).isPalindromic(0));
        assertTrue("isPalindromic(1)", Palindroms.base(10).isPalindromic(1));
        assertTrue("isPalindromic(11)", Palindroms.base(10).isPalindromic(11));
        assertTrue("isPalindromic(121)", Palindroms.base(10).isPalindromic(121));
        assertTrue("isPalindromic(1221)", Palindroms.base(10).isPalindromic(1221));
        assertFalse("isPalindromic(1231)", Palindroms.base(10).isPalindromic(1231));
    }

}
