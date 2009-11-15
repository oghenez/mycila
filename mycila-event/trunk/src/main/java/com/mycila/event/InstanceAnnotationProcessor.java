/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            Publisher publisher = Publishers.create(processor.dispatcher, Topics.topics(annotation.topics()));
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
