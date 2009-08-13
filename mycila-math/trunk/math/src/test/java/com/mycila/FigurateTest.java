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
public final class FigurateTest {

    @Test
    public void test_isPentagonal() {
        assertTrue(Figurate.isPentagonal(1) != -1);
        assertTrue(Figurate.isPentagonal(5) != -1);
        assertTrue(Figurate.isPentagonal(12) != -1);
        assertTrue(Figurate.isPentagonal(22) != -1);
        assertTrue(Figurate.isPentagonal(35) != -1);
        assertTrue(Figurate.isPentagonal(51) != -1);
        assertTrue(Figurate.isPentagonal(2) == -1);
        assertTrue(Figurate.isPentagonal(4) == -1);
        assertTrue(Figurate.isPentagonal(6) == -1);
    }

    @Test
    public void test_isHexagonal() {
        assertTrue(Figurate.isHexagonal(1) != -1);
        assertTrue(Figurate.isHexagonal(6) != -1);
        assertTrue(Figurate.isHexagonal(15) != -1);
        assertTrue(Figurate.isHexagonal(28) != -1);
        assertTrue(Figurate.isHexagonal(45) != -1);
        assertTrue(Figurate.isHexagonal(66) != -1);
        assertTrue(Figurate.isHexagonal(2) == -1);
        assertTrue(Figurate.isHexagonal(4) == -1);
        assertTrue(Figurate.isHexagonal(7) == -1);
    }

    @Test
    public void test_isTriangle() {
        assertTrue(Figurate.isTriangle(1) != -1);
        assertTrue(Figurate.isTriangle(3) != -1);
        assertTrue(Figurate.isTriangle(6) != -1);
        assertTrue(Figurate.isTriangle(10) != -1);
        assertTrue(Figurate.isTriangle(15) != -1);
        assertTrue(Figurate.isTriangle(21) != -1);
        assertTrue(Figurate.isTriangle(28) != -1);
        assertTrue(Figurate.isTriangle(2) == -1);
        assertTrue(Figurate.isTriangle(4) == -1);
    }

}