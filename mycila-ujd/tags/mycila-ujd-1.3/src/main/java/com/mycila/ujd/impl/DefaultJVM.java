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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;
import com.mycila.ujd.api.UJD;

import java.net.URLClassLoader;
import java.util.Arrays;

import static com.google.common.collect.Iterables.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVM implements JVM {

    final JavaClassRegistry classRegistry = new JavaClassRegistry(this);
    final LoaderRegistry loaderRegistry = new LoaderRegistry(this);
    final ContainerRegistry containerRegistry = new ContainerRegistry();

    public void clear() {
        classRegistry.clear();
        loaderRegistry.clear();
        containerRegistry.clear();
    }

    public JVM addClasses(Class<?>... classes) {
        return addClasses(Arrays.asList(classes));
    }

    public JVM addClasses(Iterable<Class<?>> classes) {
        // only keep classes we can analyze.
        // This also skips bootstrap classes, JDK ones and
        // proxy-generated classes and classlaoders 
        for (Class<?> aClass : classes)
            if (!aClass.isArray()
                    && aClass.getClassLoader() != null
                    && aClass.getClassLoader() instanceof URLClassLoader)
                classRegistry.add(aClass);
        return this;
    }

    public Iterable<? extends JavaClass<?>> getClasses() {
        return classRegistry.getJavaClasses();
    }

    public <T extends JavaClass<?>> Iterable<T> getClasses(Predicate<? super T> predicate) {
        return Iterables.<T>filter((Iterable<T>) getClasses(), predicate);
    }

    public Iterable<? extends Loader> getLoaders() {
        return UJD.memoize(filter(
                transform(getClasses(), UJD.JAVACLASS_TO_LOADER),
                Predicates.notNull()));
    }

    public Iterable<? extends Container> getContainers() {
        return UJD.memoize(concat(transform(getLoaders(), UJD.LOADER_TO_CONTAINER)));
    }

    public Iterable<? extends ContainedClass> getContainedClasses() {
        return UJD.memoize(concat(transform(getContainers(), UJD.CONTAINER_TO_CONTAINED_CLASSES)));
    }

    public Iterable<? extends ContainedClass> getContainedClasses(Predicate<? super ContainedClass> predicate) {
        return filter(getContainedClasses(), predicate);
    }

}
