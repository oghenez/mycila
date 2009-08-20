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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PolygonTest {

    @Test
    public void test_isPentagonal() {
        assertTrue(Polygon.isPentagonal(1) != -1);
        assertTrue(Polygon.isPentagonal(5) != -1);
        assertTrue(Polygon.isPentagonal(12) != -1);
        assertTrue(Polygon.isPentagonal(22) != -1);
        assertTrue(Polygon.isPentagonal(35) != -1);
        assertTrue(Polygon.isPentagonal(51) != -1);
        assertTrue(Polygon.isPentagonal(2) == -1);
        assertTrue(Polygon.isPentagonal(4) == -1);
        assertTrue(Polygon.isPentagonal(6) == -1);
    }

    @Test
    public void test_isHexagonal() {
        assertTrue(Polygon.isHexagonal(1) != -1);
        assertTrue(Polygon.isHexagonal(6) != -1);
        assertTrue(Polygon.isHexagonal(15) != -1);
        assertTrue(Polygon.isHexagonal(28) != -1);
        assertTrue(Polygon.isHexagonal(45) != -1);
        assertTrue(Polygon.isHexagonal(66) != -1);
        assertTrue(Polygon.isHexagonal(2) == -1);
        assertTrue(Polygon.isHexagonal(4) == -1);
        assertTrue(Polygon.isHexagonal(7) == -1);
    }

    @Test
    public void test_isTriangle() {
        assertTrue(Polygon.isTriangle(1) != -1);
        assertTrue(Polygon.isTriangle(3) != -1);
        assertTrue(Polygon.isTriangle(6) != -1);
        assertTrue(Polygon.isTriangle(10) != -1);
        assertTrue(Polygon.isTriangle(15) != -1);
        assertTrue(Polygon.isTriangle(21) != -1);
        assertTrue(Polygon.isTriangle(28) != -1);
        assertTrue(Polygon.isTriangle(2) == -1);
        assertTrue(Polygon.isTriangle(4) == -1);
    }

    @Test
    public void test_isHeptagonal() {
        assertTrue(Polygon.isHeptagonal(1) != -1);
        assertTrue(Polygon.isHeptagonal(7) != -1);
        assertTrue(Polygon.isHeptagonal(18) != -1);
        assertTrue(Polygon.isHeptagonal(34) != -1);
        assertTrue(Polygon.isHeptagonal(55) != -1);
        assertTrue(Polygon.isHeptagonal(6) == -1);
        assertTrue(Polygon.isHeptagonal(8) == -1);
        assertTrue(Polygon.isHeptagonal(2) == -1);
        assertTrue(Polygon.isHeptagonal(33) == -1);
        assertTrue(Polygon.isHeptagonal(35) == -1);
    }

    @Test
    public void test_heptagonal() {
        assertEquals(Polygon.heptagonal(0), 0);
        assertEquals(Polygon.heptagonal(1), 1);
        assertEquals(Polygon.heptagonal(2), 7);
        assertEquals(Polygon.heptagonal(3), 18);
        assertEquals(Polygon.heptagonal(4), 34);
        assertEquals(Polygon.heptagonal(5), 55);
    }

    @Test
    public void test_isOctogonal() {
        assertTrue(Polygon.isOctagonal(1) != -1);
        assertTrue(Polygon.isOctagonal(8) != -1);
        assertTrue(Polygon.isOctagonal(21) != -1);
        assertTrue(Polygon.isOctagonal(40) != -1);
        assertTrue(Polygon.isOctagonal(65) != -1);
        assertTrue(Polygon.isOctagonal(7) == -1);
        assertTrue(Polygon.isOctagonal(9) == -1);
        assertTrue(Polygon.isOctagonal(39) == -1);
        assertTrue(Polygon.isOctagonal(41) == -1);
    }

    @Test
    public void test_octogonal() {
        assertEquals(Polygon.octagonal(0), 0);
        assertEquals(Polygon.octagonal(1), 1);
        assertEquals(Polygon.octagonal(2), 8);
        assertEquals(Polygon.octagonal(3), 21);
        assertEquals(Polygon.octagonal(4), 40);
        assertEquals(Polygon.octagonal(5), 65);
    }

    @Test
    public void test() {
        System.out.println(Polygon.isHeptagonal(8256));
        System.out.println(Polygon.isHexagonal(8256));
        System.out.println(Polygon.isOctagonal(8256));
        System.out.println(Polygon.isPentagonal(8256));
        System.out.println(Polygon.isSquare(8256));
        System.out.println(Polygon.isTriangle(8256));
        System.out.println(Polygon.triangle(128));
    }

}