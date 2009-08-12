package com.mycila;

import com.mycila.sequence.LongSequence;

/**
 * @author Mathieu Carbou
 */
public final class Collatz {

    private final long number;
    private final LongSequence sequence = new LongSequence();

    private Collatz(long number) {
        this.number = number;
        if (number > 0) {
            for (; number > 1; sequence.add(number), number = (number & 1) == 0 ?
                    number >> 1 :
                    (number << 1) + number + 1)
                ;
            sequence.add(1L);
        }
    }

    public int size() {
        return sequence.size();
    }

    public long startNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collatz collatz = (Collatz) o;
        return sequence.equals(collatz.sequence);
    }

    @Override
    public int hashCode() {
        return sequence.hashCode();
    }

    @Override
    public String toString() {
        return "Collatz(" + number + ")=" + sequence;
    }

    public static Collatz from(long number) {
        return new Collatz(number);
    }

}

