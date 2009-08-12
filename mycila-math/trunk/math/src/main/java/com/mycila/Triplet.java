package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Triplet {
    public final int a;
    public final int b;
    public final int c;

    private Triplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return a == triplet.a && b == triplet.b && c == triplet.c;
    }

    @Override
    public int hashCode() {
        int result = a;
        result = 31 * result + b;
        result = 31 * result + c;
        return result;
    }

    @Override
    public String toString() {
        return "(" + a + ',' + b + ',' + c + ')';
    }

    public int sum() {
        return a + b + c;
    }

    public static Triplet of(int a, int b, int c) {
        return new Triplet(a, b, c);
    }
}
