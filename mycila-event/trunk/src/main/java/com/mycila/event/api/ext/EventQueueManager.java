package com.mycila.event.api.ext;

import com.mycila.event.api.topic.TopicMatcher;

import java.util.concurrent.BlockingQueue;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface EventQueueManager {
    <T> BlockingQueue<T> createSynchronousQueue(TopicMatcher topics, Class<T> eventType);
    <T> BlockingQueue<T> createUnboundedQueue(TopicMatcher topics, Class<T> eventType);
    <T> BlockingQueue<T> createBoundedQueue(TopicMatcher topics, Class<T> eventType, int capacity);
    <T> BlockingQueue<T> createPriorityQueue(TopicMatcher topics, Class<T> eventType);
}
