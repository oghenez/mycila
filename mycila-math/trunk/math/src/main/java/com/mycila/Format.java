package com.mycila;

import static java.util.Arrays.*;

/**
 * @author Mathieu Carbou
 */
public final class Format {

    private static final String ONES[] = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",};
    private static final String MILES[] = {"hundred", "thousand", "lakh", "crore"};
    private static final String TEENS[] = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",};
    private static final String TWENTIES[] = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    private Format() {
    }

    public static String leftPad(String s, int length, char c) {
        if (s.length() >= length) return s;
        char[] missing = new char[length - s.length()];
        fill(missing, c);
        return new String(missing).concat(s);
    }

    public static String asWords(int number) {
        if (number == 0) return "zero";
        int c = 1;
        int rm;
        StringBuilder sb = new StringBuilder();
        while (number != 0) {
            switch (c) {
                case 1:
                    rm = number % 100;
                    pass(rm, sb);
                    if (number > 100 && number % 100 != 0) sb.insert(0, "and ");
                    number /= 100;
                    break;
                case 2:
                    if ((rm = number % 10) != 0) pass(rm, sb.insert(0, " ").insert(0, MILES[0]).insert(0, " "));
                    number /= 10;
                    break;
                case 3:
                    if ((rm = number % 100) != 0) pass(rm, sb.insert(0, " ").insert(0, MILES[1]).insert(0, " "));
                    number /= 100;
                    break;
                case 4:
                    if ((rm = number % 100) != 0) pass(rm, sb.insert(0, " ").insert(0, MILES[2]).insert(0, " "));
                    number /= 100;
                    break;
                case 5:
                    if ((rm = number % 100) != 0) pass(rm, sb.insert(0, " ").insert(0, MILES[3]).insert(0, " "));
                    number /= 100;
                    break;
            }
            c++;
        }
        return sb.toString();
    }

    private static void pass(int number, StringBuilder sb) {
        if (number < 10) sb.insert(0, ONES[number]);
        if (number > 9 && number < 20) sb.insert(0, TEENS[number - 10]);
        if (number > 19) {
            int rm = number % 10;
            if (rm == 0) sb.insert(0, TWENTIES[((number / 10) - 2)]);
            else sb.insert(0, ONES[rm]).insert(0, " ").insert(0, TWENTIES[((number / 10) - 2)]);
        }
    }

}
