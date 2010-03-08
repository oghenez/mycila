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
import com.google.common.collect.Sets;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.ContainedJavaClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.InexistingClassLoaderException;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMAnalyzer;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.UJD;

import java.util.NoSuchElementException;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Iterables.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMAnalyzer implements JVMAnalyzer {

    private final JVM jvm;

    public DefaultJVMAnalyzer(JVM jvm) {
        this.jvm = jvm;
    }

    public JVM getJVM() {
        return jvm;
    }

    public int getClassCount() {
        return size(jvm.getClasses());
    }

    public int getLoaderCount() {
        return size(jvm.getLoaders());
    }

    public Iterable<String> getLoaderNames() {
        return UJD.memoize(transform(jvm.getLoaders(), UJD.LOADER_NAME));
    }

    public Iterable<String> getLoaderNames(final String packagePrefix) {
        return UJD.memoize(
                transform(
                        UJD.memoize(
                                filter(
                                        transform(
                                                jvm.getClasses(UJD.javaClassStartsWith(packagePrefix)),
                                                UJD.JAVACLASS_TO_LOADER),
                                        notNull())),
                        UJD.LOADER_NAME));
    }

    public Iterable<? extends Container> getClassPath(final String loaderName) {
        if (loaderName == null)
            throw new IllegalArgumentException("Loader name parameter is required");
        try {
            return find(jvm.getLoaders(), UJD.isLoaderNamed(loaderName)).getContainers();
        } catch (NoSuchElementException e) {
            throw new InexistingClassLoaderException(loaderName);
        }
    }

    public Iterable<? extends ContainedClass> getContainedClasses(String loaderName, final String packagePrefix) {
        return filter(
                concat(transform(getClassPath(loaderName), UJD.CONTAINER_TO_CONTAINED_CLASSES)),
                packagePrefix == null ? Predicates.<ContainedClass>alwaysTrue() : UJD.containedClassStartsWith(packagePrefix));
    }

    public Iterable<? extends ContainedJavaClass<?>> getUsedClasses(final String loaderName, final String packagePrefix) {
        if (loaderName == null)
            throw new IllegalArgumentException("Loader name parameter is required");
        return jvm.getClasses(new Predicate<JavaClass<?>>() {
            public boolean apply(JavaClass<?> input) {
                return input instanceof ContainedJavaClass
                        && input.getLoader().getName().equals(loaderName)
                        && (packagePrefix == null || input.getClass().getName().startsWith(packagePrefix));
            }
        });
    }

    public Iterable<? extends Container> getUsedClassPath(final String loaderName) {
        if (loaderName == null)
            throw new IllegalArgumentException("Loader name parameter is required");
        return UJD.memoize(transform(getUsedClasses(loaderName, null), UJD.CONTAINED_CLASS_TO_CONTAINER));
    }

    public Iterable<? extends ContainedClass> getUnusedClasses(String loaderName, String packagePrefix) {
        return filter(getContainedClasses(loaderName, packagePrefix), not(UJD.containedClassNameIn(
                Sets.newHashSet(UJD.memoize(transform(getUsedClasses(loaderName, packagePrefix), UJD.CONTAINED_CLASS_NAME))))));
    }

    public Iterable<? extends Container> getUnusedClassPath(final String loaderName) {
        if (loaderName == null)
            throw new IllegalArgumentException("Loader name parameter is required");
        return UJD.memoize(transform(getUnusedClasses(loaderName, null), UJD.CONTAINED_CLASS_TO_CONTAINER));
    }

    public Iterable<? extends Container> getUsedContainers(final String packagePrefix) {
        if (packagePrefix == null)
            throw new IllegalArgumentException("packagePrefix parameter is required");
        return UJD.memoize(transform(
                jvm.<ContainedJavaClass<?>>getClasses(Predicates.<ContainedJavaClass<?>>and(
                        instanceOf(ContainedClass.class),
                        UJD.javaClassStartsWith(packagePrefix))),
                UJD.CONTAINED_CLASS_TO_CONTAINER));
    }

    public Iterable<? extends Container> getContainers(final String packagePrefix) {
        if (packagePrefix == null)
            throw new IllegalArgumentException("packagePrefix parameter is required");
        return UJD.memoize(transform(
                jvm.getContainedClasses(UJD.containedClassStartsWith(packagePrefix)),
                UJD.CONTAINED_CLASS_TO_CONTAINER));
    }

}
