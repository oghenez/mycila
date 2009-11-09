package com.mycila.event.api;

import static com.mycila.event.api.topic.Topics.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class MatcherTest {

    @Test
    public void test_toString() {
        System.out.println(only("prog/events/a").or(only("b")));
        System.out.println(only("prog/events/a").or(topics("prog/events/**")));
    }

    @Test
    public void test_match_topic() {
        assertTrue(only("prog/events/a").matches(topic("prog/events/a")));
        assertTrue(topics("prog/events/**").matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("b")));
        assertTrue(any().matches(topic("b")));
        assertTrue(any().matches(topic("")));
        assertTrue(not(topics("prog/events/**")).matches(topic("a")));
        assertFalse(not(topics("prog/events/**")).matches(topic("prog/events/a")));
    }

    @Test
    public void test_equals() {
        assertEquals(
                only("prog/events/a").or(only("b")),
                only("prog/events/a").or(only("b")));
        assertEquals(
                only("prog/events/a").or(topics("prog/events/**")), 
                only("prog/events/a").or(topics("prog/events/**")));
    }

    @Test
    public void test_hashcode() {
        assertEquals(
                only("prog/events/a").or(only("b")).hashCode(),
                only("prog/events/a").or(only("b")).hashCode());
        assertEquals(
                only("prog/events/a").or(topics("prog/events/**")).hashCode(),
                only("prog/events/a").or(topics("prog/events/**")).hashCode());
    }
}