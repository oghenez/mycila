package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Pandigital {

    private final int base;

    private Pandigital(int base) {
        this.base = base;
    }

    public boolean isPandigital(long number, int from, int to) {
        final int length = to - from + 1;
        int bitset = 0;
        for (; number > 0; number /= base) {
            int digit = (int) (number % base);
            if (digit < from || digit > to) return false;
            int bit = 1 << digit - from;
            if ((bitset & bit) != 0) return false;
            bitset |= bit;
        }
        final int mask = (1 << length) - 1;
        return (bitset & mask) == mask;
    }

    public Range range(long number) {
        if (number == 0) return Range.range(0, 0);
        int bitset = 0;
        for (; number > 0; number /= base) {
            int digit = (int) (number % base);
            int bit = 1 << digit;
            if ((bitset & bit) != 0) return null;
            bitset |= bit;
        }
        int from = 0;
        int mask = 1;
        for (; mask <= 512 && (bitset & mask) == 0; mask <<= 1) from++;
        int to = from;
        for (; mask <= 512 && (bitset & mask) == mask; mask <<= 1) to++;
        return (bitset >> to) == 0 ? Range.range(from, to - 1) : null;
    }

    public static Pandigital base(int base) {
        return new Pandigital(base);
    }

}
