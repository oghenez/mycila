package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TopicMatcher {
    boolean matches(String topic);
}
