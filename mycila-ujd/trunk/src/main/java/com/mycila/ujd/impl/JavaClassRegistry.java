/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassRegistry {

    private final ConcurrentHashMap<Class<?>, JavaClassImpl<?>> classes = new ConcurrentHashMap<Class<?>, JavaClassImpl<?>>();
    private final JVMImpl jvm;

    JavaClassRegistry(JVMImpl jvm) {
        this.jvm = jvm;
    }

    <T> JavaClass<T> add(Class<T> aClass) {
        JavaClassImpl<?> jc = classes.get(aClass);
        if (jc == null)
            classes.put(aClass, jc = create(aClass));
        return (JavaClass<T>) jc;
    }

    Iterable<? extends JavaClass<?>> getJavaClasses() {
        return classes.values();
    }

    private <T> JavaClassImpl<T> create(Class<T> aClass) {
        return ClassUtils.isGeneratedClass(aClass) ?
                new JavaClassImpl<T>(jvm, aClass) :
                new ContainedJavaClassImpl<T>(jvm, aClass);
    }

}
