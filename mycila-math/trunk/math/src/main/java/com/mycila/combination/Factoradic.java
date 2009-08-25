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
package com.mycila.combination;

import com.mycila.math.Factorial;

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

    public <T> PermutationSet<T> permutations(T... objects) {
        return permutations(Arrays.asList(objects));
    }

    public <T> PermutationSet<T> permutations(List<T> objects) {
        if (base.length != objects.size())
            throw new IllegalArgumentException("The object list size must be equals to the base length of this factoradic: " + base.length);
        return new PermutationSet<T>(this, objects);
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
