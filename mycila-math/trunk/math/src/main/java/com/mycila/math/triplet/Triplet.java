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
package com.mycila.math.triplet;

import com.mycila.math.list.ReadOnlySequenceIterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a triplet of three values.
 *
 * @author Mathieu Carbou
 */
public final class Triplet<T> implements Iterable<T> {
    public final T a;
    public final T b;
    public final T c;

    private Triplet(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return a.equals(triplet.a) && b.equals(triplet.b) && c.equals(triplet.c);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + c.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + a + ',' + b + ',' + c + ')';
    }

    @SuppressWarnings({"unchecked"})
    public List<T> asList() {
        return Arrays.asList(a, b, c);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Iterator<T> iterator() {
        return ReadOnlySequenceIterator.on(a, b, c);
    }

    public static <T> Triplet<T> of(T a, T b, T c) {
        return new Triplet<T>(a, b, c);
    }
}