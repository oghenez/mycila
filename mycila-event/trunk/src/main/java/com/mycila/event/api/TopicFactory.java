package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TopicFactory implements DestinationFactory<Topic> {
    @Override
    public Topic create(String name) {
        return Topic.valueOf(name);
    }
}
