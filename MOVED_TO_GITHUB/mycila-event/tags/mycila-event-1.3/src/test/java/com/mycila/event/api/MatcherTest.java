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

package com.mycila.event.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.mycila.event.api.Topics.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class MatcherTest {

    @Test
    public void test_toString() {
        System.out.println(only("prog/events/a").or(only("b")));
        System.out.println(only("prog/events/a").or(matching("prog/events/**")));
    }

    @Test
    public void test_match_topic() {
        assertTrue(only("prog/events/a").matches(topic("prog/events/a")));
        assertTrue(matching("prog/events/**").matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("b")));
        assertTrue(any().matches(topic("b")));
        assertTrue(any().matches(topic("")));
        assertTrue(not(matching("prog/events/**")).matches(topic("a")));
        assertFalse(not(matching("prog/events/**")).matches(topic("prog/events/a")));
    }

    @Test
    public void test_equals() {
        assertEquals(
                only("prog/events/a").or(only("b")),
                only("prog/events/a").or(only("b")));
        assertEquals(
                only("prog/events/a").or(matching("prog/events/**")),
                only("prog/events/a").or(matching("prog/events/**")));
    }

    @Test
    public void test_hashcode() {
        assertEquals(
                only("prog/events/a").or(only("b")).hashCode(),
                only("prog/events/a").or(only("b")).hashCode());
        assertEquals(
                only("prog/events/a").or(matching("prog/events/**")).hashCode(),
                only("prog/events/a").or(matching("prog/events/**")).hashCode());
    }
}