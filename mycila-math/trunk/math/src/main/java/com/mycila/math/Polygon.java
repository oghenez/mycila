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
package com.mycila.math;

/**
 * Class for <a href="http://en.wikipedia.org/wiki/Figurate_number">Figurate numbers (polygon numbers)</a>
 *
 * @author Mathieu Carbou
 */
public final class Polygon {

    private Polygon() {
    }

    public static long square(long index) {
        return index * index;
    }

    public static long isSquare(long number) {
        switch ((int) (number & 0xF)) {
            case 0:
            case 1:
            case 4:
            case 9:
                long tst = (long) Math.sqrt(number);
                return tst * tst == number ? tst : -1;
            default:
                return -1;
        }
    }

    public static long pentagonal(long index) {
        return (index * (3 * index - 1)) >>> 1;
    }

    public static long isPentagonal(long number) {
        double test = (Math.sqrt(24 * number + 1) + 1) / 6;
        return test == (long) test ? (long) test : -1;
    }

    public static long triangle(long index) {
        return (index * (index + 1)) >>> 1;
    }

    public static long isTriangle(long number) {
        double test = Math.sqrt((number << 1) + 0.25) - 0.5;
        return test == (long) test ? (long) test : -1;
    }

    public static long hexagonal(long index) {
        return index * ((index << 1) - 1);
    }

    public static long isHexagonal(long number) {
        double test = (Math.sqrt(1 + (number << 3)) + 1) / 4;
        return test == (long) test ? (long) test : -1;
    }

    public static long heptagonal(long index) {
        return (index * (5 * index - 3)) >>> 1;
    }

    public static long isHeptagonal(long number) {
        double test = (1.5 + Math.sqrt(2.25 + 10 * number)) / 5;
        return test == (long) test ? (long) test : -1;
    }

    public static long octagonal(long index) {
        return index * (3 * index - 2);
    }

    public static long isOctagonal(long number) {
        double test = (1 + Math.sqrt(1 + 3 * number)) / 3;
        return test == (long) test ? (long) test : -1;
    }

}
