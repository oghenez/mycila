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

import com.mycila.math.sequence.ReadOnlySequenceIterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a triplet of three values.
 *
 * @author Mathieu Carbou
 */
public final class LongTriplet implements Iterable<Long> {
    public final long a;
    public final long b;
    public final long c;

    private LongTriplet(long a, long b, long c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTriplet triplet = (LongTriplet) o;
        return a == triplet.a && b == triplet.b && c == triplet.c;
    }

    @Override
    public int hashCode() {
        int result = ((int) (a ^ (a >>> 32)));
        result = 31 * result + ((int) (b ^ (b >>> 32)));
        result = 31 * result + ((int) (c ^ (c >>> 32)));
        return result;
    }

    @Override
    public String toString() {
        return "(" + a + ',' + b + ',' + c + ')';
    }

    public List<Long> asList() {
        return Arrays.asList(a, b, c);
    }

    @Override
    public Iterator<Long> iterator() {
        return ReadOnlySequenceIterator.on(new long[]{a, b, c});
    }

    public long sum() {
        return a + b + c;
    }

    public static LongTriplet of(long a, long b, long c) {
        return new LongTriplet(a, b, c);
    }
}