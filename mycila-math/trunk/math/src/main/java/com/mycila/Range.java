package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Range {
    public final int from;
    public final int to;

    private Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return !(from != range.from || to != range.to);
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }

    @Override
    public String toString() {
        return "[" + from + ", " + to + ']';
    }

    public static Range range(int from, int to) {
        return new Range(from, to);
    }

}