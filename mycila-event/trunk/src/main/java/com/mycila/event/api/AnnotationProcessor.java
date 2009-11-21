package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface AnnotationProcessor {
    public abstract <T> T process(T instance);

    public abstract <T> T createPublisher(Class<T> abstractClassOrInterface);
}
