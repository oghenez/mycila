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
package com.mycila.math.range;

/**
 * @author Mathieu Carbou
 */
public final class IntRange {

    private static final IntRange EMPTY = new IntRange(Integer.MAX_VALUE, Integer.MIN_VALUE);

    public final int from;
    public final int to;

    private IntRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntRange range = (IntRange) o;
        if (o == EMPTY || this == EMPTY) return this == o;
        return !(from != range.from || to != range.to);
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }

    public boolean contains(int value) {
        return from <= value && to >= value;
    }

    public int length() {
        return isEmpty() ? 0 : to - from + 1;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public IntRange extendTo(int to) {
        return to > this.to ? new IntRange(from, to) : this;
    }

    @Override
    public String toString() {
        return isEmpty() ? "[]" : "[" + from + ", " + to + ']';
    }

    public static IntRange range(int from, int to) {
        return new IntRange(from, to);
    }

    public static IntRange empty() {
        return EMPTY;
    }

}