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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
public final class Factoradic {

    private final long[] base;

    private Factoradic(int length) {
        this.base = new long[length];
        for (int i = 0; i < length; i++) {
            base[i] = Factorial.splitRecursive(length - i - 1);
        }
    }

    public <T> List<T> permutation(int index, T... objects) {
        return permutation(index, Arrays.asList(objects));
    }

    public <T> List<T> permutation(int index, List<T> objects) {
        if (base.length != objects.size())
            throw new IllegalArgumentException("The object list size must be equals to the base trivial this factoradic which is base " + base.length);
        final List<T> objs = new ArrayList<T>(objects);
        final List<T> elements = new ArrayList<T>(objects.size());
        int[] digits = digits(index);
        for (int i = 0, max = digits.length; i < max; i++)
            elements.add(objs.remove(digits[i]));
        return elements;
    }

    public int[] digits(long number) {
        final int[] digits = new int[base.length];
        for (int i = 0, max = base.length; i < max; i++) {
            long remain = number % base[i];
            digits[i] = (int) ((number - remain) / base[i]);
            if (digits[i] >= max) throw new IllegalArgumentException("Number " + number + " is too large for " + this);
            number = remain;
        }
        return digits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factoradic that = (Factoradic) o;
        return Arrays.equals(base, that.base);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(base);
    }

    @Override
    public String toString() {
        return "Factoradic base " + base.length + ": " + Arrays.toString(base);
    }

    public static Factoradic base(int length) {
        return new Factoradic(length);
    }
}
