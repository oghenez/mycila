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

package com.mycila.event.api;

import com.mycila.event.api.annotation.Publish;
import com.mycila.event.api.annotation.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractAnnotationProcessor implements AnnotationProcessor {

    private final Dispatcher dispatcher;

    protected AbstractAnnotationProcessor(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public final <T> T createPublisher(Class<T> abstractClassOrInterface) {
        notNull(abstractClassOrInterface, "Abstract class or interface");
        if (abstractClassOrInterface.isInterface())
            return proxyInterface(dispatcher, abstractClassOrInterface);
        int mods = abstractClassOrInterface.getModifiers();
        if (Modifier.isFinal(mods) || Modifier.isPrivate(mods))
            throw new IllegalArgumentException("Cannot proxy class " + abstractClassOrInterface.getName());
        return proxyClass(dispatcher, abstractClassOrInterface);
    }

    @Override
    public final <T> T process(T instance) {
        notNull(instance, "Instance");
        final Iterable<Method> methods = ClassUtils.getAllDeclaredMethods(instance.getClass());
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, supportedAnnotations()))
            processAnnotatedMethod(dispatcher, instance, method);
        return instance;
    }

    protected abstract <T> T proxyInterface(Dispatcher dispatcher, Class<T> clazz);

    protected abstract <T> T proxyClass(Dispatcher dispatcher, Class<T> clazz);

    protected abstract void processAnnotatedMethod(Dispatcher dispatcher, Object instance, Method method);

    protected Iterable<Class<? extends Annotation>> supportedAnnotations() {
        return Arrays.asList(Subscribe.class, Publish.class);
    }
}