package com.mycila.event.topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TopicMatcher {
    boolean matches(Topic topic);
}
