package com.mycila.event;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationProcessor {

    final Dispatcher dispatcher;

    private AnnotationProcessor(Dispatcher dispatcher) {
        notNull(dispatcher, "Dispatcher");
        this.dispatcher = dispatcher;
    }

    public InstanceAnnotationProcessor process(Object instance) {
        return new InstanceAnnotationProcessor(this, instance);
    }

    public <T> T process(Class<T> c) {
        notNull(c, "Class");//TODO
        // interface => jdk proxy
        // abstract ou concrete => cglib
        return null;
    }

    public static AnnotationProcessor create(Dispatcher dispatcher) {
        return new AnnotationProcessor(dispatcher);
    }

}
