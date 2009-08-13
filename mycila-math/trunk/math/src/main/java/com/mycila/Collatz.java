/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

