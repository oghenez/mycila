package com.mycila.event.api.topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TopicMatcher {
    boolean matches(Topic topic);
}
