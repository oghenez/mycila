package com.mycila.event;

import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;

import java.lang.reflect.Method;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InstanceAnnotationProcessor {
    final AnnotationProcessor processor;
    final Object instance;
    final Iterable<Method> methods;

    InstanceAnnotationProcessor(AnnotationProcessor processor, Object instance) {
        notNull(instance, "Instance");
        notNull(processor, "AnnotationProcessor");
        this.processor = processor;
        this.instance = instance;
        methods = ClassUtils.getAllDeclaredMethods(instance.getClass());
    }

    @SuppressWarnings({"unchecked"})
    public InstanceAnnotationProcessor registerSubscribers() {
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Subscribe.class)) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            processor.dispatcher.subscribe(Topics.anyOf(subscribe.topics()), subscribe.eventType(), Subscriptions.createSubscriber(instance, method));
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public InstanceAnnotationProcessor registerVetoers() {
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Veto.class)) {
            Veto veto = method.getAnnotation(Veto.class);
            processor.dispatcher.subscribe(Topics.anyOf(veto.topics()), veto.eventType(), Subscriptions.createVetoer(instance, method));
        }
        return this;
    }

    public InstanceAnnotationProcessor injectPublishers() {
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Publish.class)) {
            Publish annotation = method.getAnnotation(Publish.class);
            uniqueArg(Publisher.class, method);
            Publisher publisher = Publishers.create(processor.dispatcher, Topics.topic(annotation.topic()));
            method.setAccessible(true);
            try {
                method.invoke(instance, publisher);
            } catch (Exception e) {
                throw ExceptionUtils.toRuntime(e);
            }
        }
        return this;
    }

}
