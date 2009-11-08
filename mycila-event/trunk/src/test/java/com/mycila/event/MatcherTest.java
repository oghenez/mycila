package com.mycila.event;

import static com.mycila.event.topic.Topics.*;
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
    public void test_match_topic() {
        assertTrue(only("prog/events/a").matches(topic("prog/events/a")));
        assertTrue(topics("prog/events/**").matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("prog/events/a")));
        assertTrue(only("prog/events/a").or(only("b")).matches(topic("b")));
        assertTrue(any().matches(topic("b")));
        assertTrue(any().matches(topic("")));
        assertTrue(not(topics("prog/events/**")).matches(topic("a")));
        assertFalse(not(topics("prog/events/**")).matches(topic("prog/events/a")));

        System.out.println(only("prog/events/a").or(only("b")));
    }

}