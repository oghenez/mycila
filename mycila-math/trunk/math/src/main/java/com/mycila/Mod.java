package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Mod {

    private Mod() {
    }

    public static int multiply(int a, int b, int mod) {
        long c = (a % mod) * (b % mod);
        return (int) (c % (long) mod);
    }

    public static long multiply(long a, long b, long mod) {
        return ((a % mod) * (b % mod)) % mod;
    }

    public static int add(int a, int b, int mod) {
        long c = (a % mod) + (b % mod);
        return (int) (c % (long) mod);
    }

    public static long add(long a, long b, long mod) {
        return ((a % mod) + (b % mod)) % mod;
    }

    public static int pow(int a, int exp, int mod) {
        long power = a;
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * power) % mod;
            power = (power * power) % mod;
            exp >>= 1;
        }
        return (int) result;
    }

    public static long pow(long a, long exp, long mod) {
        long power = a;
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * power) % mod;
            power = (power * power) % mod;
            exp >>= 1;
        }
        return result;
    }

}

