package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Factor {
    private final int p;
    private int exp;

    private Factor(int p, int times) {
        this.p = p;
        this.exp = times;
    }

    public int prime() {
        return p;
    }

    public int exponent() {
        return exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return p == triplet.a && exp == triplet.b;
    }

    @Override
    public int hashCode() {
        int result = p;
        result = 31 * result + exp;
        return result;
    }

    @Override
    public String toString() {
        return p + "^" + exp;
    }

    public void incrementExponent() {
        this.exp++;
    }

    public int value() {
        return (int) Math.pow(p, exp);
    }

    public static Factor valueOf(int p) {
        return new Factor(p, 1);
    }

    public static Factor valueOf(int p, int times) {
        return new Factor(p, times);
    }
}